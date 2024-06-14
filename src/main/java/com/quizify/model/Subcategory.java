package com.quizify.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "subcategory")
public class Subcategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subcategory_id")
    private Integer subcategoryId;

    @Column(name = "subcategory_name")
    private String subcategoryName;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

}