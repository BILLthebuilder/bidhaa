package com.bidhaa.login;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.bidhaa.dto.CreateUserRequest;
import com.bidhaa.dto.LoginUserRequest;
import com.bidhaa.dto.UpdateUserRequest;
import com.bidhaa.repository.UserRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class UserCreationAndLoginTest {
    @Autowired
    private MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();

    CreateUserRequest createUserRequest = new CreateUserRequest("Bill", "Testing", "test@email.com", "Password@123", "254708123456","ADMIN");

    @Autowired
    UserRepository repository;

    @Test
    @Order(1)
    void userShouldBeCreated() throws Exception {

        mockMvc.perform(post("/api/v1/users/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createUserRequest))
        ).andExpect(status().isCreated());
    }

    @Test
    @Order(2)
    void userShouldLogin() throws Exception {
        var loginUserRequest = new LoginUserRequest(createUserRequest.email(), createUserRequest.password());

      mockMvc.perform(post("/api/v1/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginUserRequest))
        ).andExpect(status().isOk());
    }

    @Test
    @Order(3)
    void userShouldGetAll() throws Exception {
        mockMvc.perform(get("/api/v1/users").header("Authorization")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @Test
    @Order(4)
    void userShouldUpdate() throws Exception {
        var updateuserRequest = new UpdateUserRequest("fname", "lname", "test@email.com", "254722000000");
        var user = repository.findByEmail(createUserRequest.email());
        UUID id = user.getId();
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/users/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateuserRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @Order(5)
    void userShouldDelete() throws Exception {
        var user = repository.findByEmail(createUserRequest.email());
        UUID id = user.getId();
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/users/{id}",id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}
