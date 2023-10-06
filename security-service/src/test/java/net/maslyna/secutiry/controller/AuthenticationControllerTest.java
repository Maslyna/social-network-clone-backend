package net.maslyna.secutiry.controller;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

//import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
public class AuthenticationControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    public void emptyregistration_ReturnsBadRequest() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/security/register")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
        ).andExpectAll(
                status().isBadRequest()
        );
    }
}