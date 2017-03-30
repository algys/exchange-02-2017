package com.cyclic.controllers;

import com.cyclic.models.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcPrint;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by algys on 30.03.17.
 */

@SuppressWarnings({"SameParameterValue", "DefaultFileTemplate", "StringBufferReplaceableByString"})
@SpringBootTest(webEnvironment = RANDOM_PORT)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc(print = MockMvcPrint.NONE)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@Transactional
public class RestApiTestMock {
    @Autowired
    private MockMvc mockMvc;
    private final Random rand = new Random();
    private final ObjectMapper mapper = new ObjectMapper();

    private String email;
    private String login;
    private String password;

    @NotNull
    private String getRandomString(int len ){
        String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        StringBuilder sb = new StringBuilder( len );
        for( int i = 0; i < len; i++ )
            sb.append( chars.charAt( rand.nextInt(chars.length()) ) );
        return sb.toString();
    }

    @NotNull
    private String getRandomEmail(){
        return new StringBuilder()
                .append(getRandomString(3))
                .append("@")
                .append(getRandomString(3))
                .append(".com")
                .toString();
    }

    @Before
    public void setFields(){
        email = getRandomEmail();
        login = getRandomString(8);
        password = getRandomString(8);
    }

    @Test
    public void registrationTest() throws Exception{
        User user;
        String userStr;

        user = new User(null, login, password);
        userStr = mapper.writeValueAsString(user);
        mockMvc.perform(
                put("/api/user")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(userStr))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());

        user = new User(email, null, password);
        userStr = mapper.writeValueAsString(user);
        mockMvc.perform(
                put("/api/user")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(userStr))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());

        user = new User(email, login, null);
        userStr = mapper.writeValueAsString(user);
        mockMvc.perform(
                put("/api/user")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(userStr))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());

        user = new User(email, login, password);
        userStr = mapper.writeValueAsString(user);
        mockMvc.perform(
                put("/api/user")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(userStr))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }

    @Test
    public void loginTest() throws Exception{
        User user;
        String userStr;

        user = new User(email, login, password);
        userStr = mapper.writeValueAsString(user);
        mockMvc.perform(
                put("/api/user")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(userStr))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

         MockHttpServletResponse response = mockMvc.perform(
                post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(userStr))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn().getResponse();
/*
        mockMvc.perform(
                get("/api/login")
                        .header(HttpHeaders.COOKIE, cookie))
                .andExpect(status().isOk());*/
    }





}
