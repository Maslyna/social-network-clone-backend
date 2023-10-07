package net.maslyna.secutiry.controller;

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
    public void normalRegistration_ReturnsCreated() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.post(ServiceURI.REGISTRATION)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createRegistrationRequest())
        ).andExpectAll(
                status().isCreated(),
                content().contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();
    }

    @Test
    public void emptyRegistration_ReturnsBadRequest() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.post(ServiceURI.REGISTRATION)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
        ).andExpectAll(
                status().isBadRequest()
        );
    }

    @Test
    public void emailOccupiedRegistration_ReturnsConflict() throws Exception {
        normalRegistration_ReturnsCreated();
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.post(ServiceURI.REGISTRATION)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createRegistrationRequest())
        ).andExpectAll(
                status().isConflict(),
                content().contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();
    }

    @Test
    public void normalAuth_ReturnsOk() throws Exception {
        normalRegistration_ReturnsCreated();
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

    @Test
    public void emptyAuth_ReturnsUnauthorized() throws Exception {
        normalRegistration_ReturnsCreated();
        mockMvc.perform(
                MockMvcRequestBuilders.post(ServiceURI.AUTHENTICATION)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "")
        ).andExpectAll(
                status().isUnauthorized()
        );
    }

    @Test
    public void notValidAuth_ReturnsUnauthorized() throws Exception {
        normalRegistration_ReturnsCreated();
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

    private String createRegistrationRequest() throws Exception {
        return toJSON(defaultTestUser);
    }

    private String createRegistrationRequest(Long id, String email, String password) throws Exception {
        return toJSON(new TestRegistrationRequest(id, email, password));
    }

    private String toJSON(Object obj) throws Exception {
        return objectMapper.writeValueAsString(obj);
    }
}