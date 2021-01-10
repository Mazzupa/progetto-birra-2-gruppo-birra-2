package it.progettois.brewday.persistence.model;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
public class Brew {

    @Id
    @Column(name = "brewer_username", updatable = false, nullable = false, unique = true)
    private String username;

    private Integer quantity;

    @Column(name = "start_date")
    private Timestamp startDate;

    private String duration;

    private String note;

    //Json representation of the recipe
    @Column(length = 1000)
    private String recipe;

    @ManyToOne
    @JoinColumn(name = "brewer_id")
    private Brewer brewer;
}
