package com.ssafy.backend.domain.food.dto;

import com.ssafy.backend.domain.food.entity.Food;
import com.ssafy.backend.domain.food.entity.UserFood;
import com.ssafy.backend.domain.user.entity.User;
import lombok.*;

@Data
@Builder
@ToString
public class UserFoodListDto {

    private Long userFoodId;
    private Long userId;
    private Food food;
    private int foodCount;
    private String category;


    public static UserFoodListDto toDto(UserFood userFood){
        return UserFoodListDto.builder()
                .userFoodId(userFood.getUserFoodId())
                .userId(userFood.getUser().getUserId())
                .food(userFood.getFood())
                .foodCount(userFood.getFoodCount())
                .category(userFood.getCategory())
                .build();

    }

}
