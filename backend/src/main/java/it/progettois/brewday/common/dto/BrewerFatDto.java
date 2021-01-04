package it.progettois.brewday.common.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class BrewerFatDto {
    private Integer brewerId;
    private String username;
    private String name;
    private String email;
    private List<ToolDto> tools;
    private List<RecipeDto> recipes;
    private List<BrewerIngredientDto> ingredients;
}


