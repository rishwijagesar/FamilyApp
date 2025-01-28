package com.rishwi.familyapp.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)

@Entity
@Table(name = "person")
public class Person {

    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Getter
    @Setter
    @Column(name = "name", nullable = false)
    private String name;

    @Getter
    @Setter
    @Column(name = "birthDay", nullable = false)
    private Date birthDay;

    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "parent1_id")
    private Person parent1; // Parent1 is optional, nullable = true

    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "parent2_id")
    private Person parent2; // Parent2 is optional, nullable = true

    @Getter
    @Setter
    @OneToMany(mappedBy = "parent1", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Person> children1 = new ArrayList<>(); // Initialize to prevent NullPointerException

    @Getter
    @Setter
    @OneToMany(mappedBy = "parent2", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Person> children2 = new ArrayList<>(); // Initialize to prevent NullPointerException

    @Getter
    @Setter
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "partner_id")
    private Person partner; // Partner is optional
}
