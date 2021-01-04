package it.progettois.brewday.model;

import it.progettois.brewday.model.constant.IngredientType;
import it.progettois.brewday.model.constant.IngredientUnit;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ingredient_id")
    private Integer ingredientId;

    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    private IngredientUnit unit;

    @Enumerated(EnumType.STRING)
    private IngredientType type;

    @OneToMany(mappedBy = "recipe")
    private List<RecipeIngredient> recipe;

    @ManyToOne
    private Brewer owner;

}
