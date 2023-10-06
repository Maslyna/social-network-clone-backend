package net.maslyna.secutiry.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

//import static org.junit.jupiter.api.Assertions.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Testcontainers
@AutoConfigureMockMvc
@SpringBootTest
public class SergviceIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest");

    @BeforeEach
    void prepare() {
        postgreSQLContainer.start();
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
    public void registration_ReturnsBadRequest() throws Exception {
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


    private String createRegistrationRequest() {
        return """
            {
            "id" : %s,
            "email" : %s,
            "password" : %s
            }
            """.formatted(1L, jsonFormat("dummymail@mail.net"), jsonFormat("password"));
    }

    private String jsonFormat(String str) {
        return str == null ? "null" : "\"%s\"".formatted(str);
    }
}