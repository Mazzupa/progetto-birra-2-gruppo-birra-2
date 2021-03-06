package it.progettois.brewday.persistence.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Data
@Entity
public class Brewer {

    @Id
    @Column(updatable = false, nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private Integer maxBrew;

    @OneToMany(mappedBy = "brewer")
    private List<Ingredient> ingredients;

    @OneToMany(mappedBy = "brewer")
    private List<Tool> tools;

    @OneToMany(mappedBy = "brewer")
    private List<Recipe> recipes;

    @OneToMany(mappedBy = "brewer")
    private List<Brew> brews;
}
