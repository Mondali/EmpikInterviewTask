package com.empik.empikinterviewtask.controller;

import com.empik.empikinterviewtask.mapper.UserMapper;
import com.empik.empikinterviewtask.model.dto.UserDto;
import com.empik.empikinterviewtask.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User", description = "User API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserMapper userMapper;
    private final UserService userService;

    @GetMapping("/{login}")
    @Operation(
            summary = "Get user by login",
            description = "Endpoint is responsible for getting user by login from GitHub API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public UserDto getByLogin(
            @Parameter(description = "User login", required = true, example = "octocat")
            @PathVariable String login) {
        return userMapper.mapToUserDto(userService.getByLogin(login));
    }

}
