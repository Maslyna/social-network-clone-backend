package net.maslyna.secutiry.controller;

import net.maslyna.secutiry.config.AuthenticationType;
import net.maslyna.secutiry.service.BasicService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@AutoConfigureMockMvc
@SpringBootTest
@Transactional
public class ServiceIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private BasicService basicService;
    private long counter;

    private TestRegistrationRequest defaultTestUser =
            new TestRegistrationRequest(++counter, "dummymail@mail.net", "password");

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest");

    @DynamicPropertySource
    static void postgresProperties(DynamicPropertyRegistry registry) {
        registry.add("database.driver", postgreSQLContainer::getDriverClassName);
    }

    @BeforeEach
    void prepare() {
        postgreSQLContainer.start();
    }

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
    public void logoutTests() throws Exception {
        logout_ReturnsOk();
    }


    private void logout_ReturnsOk() throws Exception {
        MockHttpServletResponse response = createDefaultUser();
        String token = objectMapper.reader().readTree(response.getContentAsString()).get("token").asText();
        System.out.println(token);
        mockMvc.perform(
                MockMvcRequestBuilders.get(ServiceURI.LOGOUT)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, AuthenticationType.BEARER.prefix() + token)
        ).andExpectAll(
                status().isOk()
        );
    }

    private void normalRegistration_ReturnsCreated() throws Exception {
        createDefaultUser();
    }

    private MockHttpServletResponse createDefaultUser() throws Exception {
        return mockMvc.perform(
                MockMvcRequestBuilders.post(ServiceURI.REGISTRATION)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJSON(defaultTestUser))
        ).andExpectAll(
                status().isCreated(),
                content().contentType(MediaType.APPLICATION_JSON)
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
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.post(ServiceURI.REGISTRATION)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJSON(defaultTestUser))
        ).andExpectAll(
                status().isConflict(),
                content().contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();
    }

    private void basicAuth_ReturnsOk() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.post(ServiceURI.AUTHENTICATION)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION,
                                basicService.generateBasicAuth(defaultTestUser.email(), defaultTestUser.password()))
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

    public void notValidAuth_ReturnsUnauthorized() throws Exception {
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

    private String toJSON(Object obj) throws Exception {
        return objectMapper.writeValueAsString(obj);
    }
}