package com.empik.empikinterviewtask.controller;

import com.empik.empikinterviewtask.mapper.UserMapper;
import com.empik.empikinterviewtask.model.User;
import com.empik.empikinterviewtask.model.dto.UserDto;
import com.empik.empikinterviewtask.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserMapper userMapper;

    @MockBean
    private UserService userService;


    @Test
    void getByLogin_ReturnsUserDto() throws Exception {
        User user = User.builder()
                .id(1)
                .login("octocat")
                .name("The Octocat")
                .type("User")
                .avatarUrl("https://avatars.githubusercontent.com/u/583231?v=4")
                .createdAt(LocalDateTime.parse("2011-01-25T18:44:36"))
                .followers(2877)
                .publicRepos(8)
                .build();

        UserDto userDto = UserDto.builder()
                .id(1)
                .login("octocat")
                .name("The Octocat")
                .type("User")
                .avatarUrl("https://avatars.githubusercontent.com/u/583231?v=4")
                .createdAt(LocalDateTime.parse("2011-01-25T18:44:36"))
                .calculations(BigDecimal.TEN)
                .build();

        when(userService.getByLogin("octocat")).thenReturn(user);
        when(userMapper.mapToUserDto(any(User.class))).thenReturn(userDto);

        mockMvc.perform(get("/api/v1/users/octocat")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.login").value("octocat"))
                .andDo(print());

    }

    @Test
    void getByLogin_UserNotFound_ReturnsNotFound() throws Exception {
        when(userService.getByLogin("octocat")).thenThrow(new EntityNotFoundException("User with login octocat not found"));

        mockMvc.perform(get("/api/v1/users/octocat"))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof EntityNotFoundException))
                .andExpect(jsonPath("$.message").value("User with login octocat not found"))
                .andDo(print());
    }
}
