package com.empik.empikinterviewtask.mapper;

import com.empik.empikinterviewtask.model.User;
import com.empik.empikinterviewtask.model.dto.UserDto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class UserMapper {

    public UserDto mapToUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .login(user.getLogin())
                .name(user.getName())
                .type(user.getType())
                .avatarUrl(user.getAvatarUrl())
                .createdAt(user.getCreatedAt())
                .calculations(calculateCalculations(user.getFollowers(), user.getPublicRepos()))
                .build();
    }

    private BigDecimal calculateCalculations(Integer followers, Integer publicRepos) {
        if (followers == 0) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf(6).divide(BigDecimal.valueOf(followers), 2, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(2 + publicRepos));
    }

}
