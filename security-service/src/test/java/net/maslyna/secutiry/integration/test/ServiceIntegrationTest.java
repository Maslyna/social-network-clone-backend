package net.maslyna.secutiry.integration.test;

import net.maslyna.secutiry.config.AuthenticationType;
import net.maslyna.secutiry.integration.*;
import net.maslyna.secutiry.integration.config.TestConfig;
import net.maslyna.secutiry.integration.model.TestRegistrationRequest;
import net.maslyna.secutiry.integration.service.JsonService;
import net.maslyna.secutiry.service.BasicService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@Import(value = {TestConfig.class, JsonService.class})
public class ServiceIntegrationTest extends BasicIntegrationTest {

    @Autowired
    private BasicService basicService;
    @Autowired
    private JsonService jsonService;

    @Autowired
    public ServiceIntegrationTest(MockMvc mockMvc) {
        super(mockMvc);
    }

    private static final TestRegistrationRequest DEFAULT_TEST_USER =
            new TestRegistrationRequest(1L, "dummymail@mail.net", "password");

    @Test
    public void registrationTests() throws Exception {
        normalRegistration_ReturnsCreated();
        emptyRegistration_ReturnsBadRequest();
        emailOccupiedRegistration_ReturnsConflict();
    }

    @Test
    public void authenticationTests() throws Exception {
        createDefaultUser();
        basicAuth_ReturnsOk();
        emptyAuth_ReturnsUnauthorized();
        notValidAuth_ReturnsUnauthorized();
    }

    @Test
    public void validationTests() throws Exception {
        MockHttpServletResponse response = createDefaultUser();
        String token = jsonService.extract(response.getContentAsString(), "token");
        jwtValidation_ReturnsOk(token);
        basicValidation_ReturnsOk();
        emptyBasicValidation_ReturnsForbidden();
        emptyJwtValidation_ReturnsForbidden();
        notSupportedTypeValidation_ReturnsForbidden();
    }

    @Test
    public void logoutTests() throws Exception {
        MockHttpServletResponse response = createDefaultUser();
        String token = jsonService.extract(response.getContentAsString(), "token");
        jwtLogout_ReturnsOk(token);
        basicLogout_ReturnsOk();
    }


    private void normalRegistration_ReturnsCreated() throws Exception {
        createDefaultUser();
    }

    private MockHttpServletResponse createDefaultUser() throws Exception {
        return mockMvc.perform(
                MockMvcRequestBuilders.post(ServiceURI.REGISTRATION)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonService.toJson(DEFAULT_TEST_USER))
        ).andExpectAll(
                status().isCreated(),
                content().contentType(MediaType.APPLICATION_JSON),
                jsonPath("$.token").exists()
        ).andReturn().getResponse();
    }


    private void emptyRegistration_ReturnsBadRequest() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.post(ServiceURI.REGISTRATION)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
        ).andExpectAll(
                status().isBadRequest()
        );
    }


    private void emailOccupiedRegistration_ReturnsConflict() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.post(ServiceURI.REGISTRATION)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonService.toJson(DEFAULT_TEST_USER))
        ).andExpectAll(
                status().isConflict()
        );
    }

    private void basicAuth_ReturnsOk() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.post(ServiceURI.AUTHENTICATION)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION,
                                basicService.generateBasicAuth(DEFAULT_TEST_USER.email(), DEFAULT_TEST_USER.password()))
        ).andExpectAll(
                status().isOk()
        );
    }

    private void emptyAuth_ReturnsUnauthorized() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.post(ServiceURI.AUTHENTICATION)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "")
        ).andExpectAll(
                status().isUnauthorized()
        );
    }

    private void notValidAuth_ReturnsUnauthorized() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.post(ServiceURI.AUTHENTICATION)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION,
                                basicService.generateBasicAuth("notvalidusername", "badpassword"))
        ).andExpectAll(
                status().isUnauthorized()
        );
    }

    private void jwtValidation_ReturnsOk(String token) throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get(ServiceURI.VALIDATION)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION,
                                AuthenticationType.BEARER.prefix() + token)
        ).andExpectAll(
                status().isOk(),
                content().contentType(MediaType.APPLICATION_JSON),
                jsonPath("$.id").exists(),
                jsonPath("$.email").exists()
        );
    }

    private void basicValidation_ReturnsOk() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get(ServiceURI.VALIDATION)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION,
                                basicService.generateBasicAuth(DEFAULT_TEST_USER.email(), DEFAULT_TEST_USER.password()))
        ).andExpectAll(
                status().isOk(),
                content().contentType(MediaType.APPLICATION_JSON),
                jsonPath("$.id").exists(),
                jsonPath("$.email").exists()
        );
    }

    private void emptyBasicValidation_ReturnsForbidden() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get(ServiceURI.VALIDATION)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION,
                                basicService.generateBasicAuth("not_valid", ""))
        ).andExpectAll(
                status().isForbidden(),
                content().contentType(MediaType.APPLICATION_JSON)
        );
    }

    private void emptyJwtValidation_ReturnsForbidden() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get(ServiceURI.VALIDATION)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION,
                                AuthenticationType.BEARER.prefix() + " ")
        ).andExpectAll(
                status().isForbidden(),
                content().contentType(MediaType.APPLICATION_JSON)
        );
    }

    private void notSupportedTypeValidation_ReturnsForbidden() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get(ServiceURI.VALIDATION)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION,
                                "Not valid type" + " ")
        ).andExpectAll(
                status().isForbidden()
        );
    }

    private void basicLogout_ReturnsOk() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get(ServiceURI.LOGOUT)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION,
                                basicService.generateBasicAuth(DEFAULT_TEST_USER.email(), DEFAULT_TEST_USER.password()))
        ).andExpectAll(
                status().isOk()
        );
    }

    private void jwtLogout_ReturnsOk(String token) throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get(ServiceURI.LOGOUT)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, AuthenticationType.BEARER.prefix() + token)
        ).andExpectAll(
                status().isOk()
        );
    }
}