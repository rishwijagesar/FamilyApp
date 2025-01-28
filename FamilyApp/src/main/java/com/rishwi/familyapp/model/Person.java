package com.rishwi.familyapp.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY) // Skip empty or null fields
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
@Getter
@Setter
@Entity
@Table(name = "person")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "birthDay", nullable = false)
    private Date birthDay;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent1_id")
    private Person parent1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent2_id")
    private Person parent2;

    @OneToMany(mappedBy = "parent1", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Person> children1 = new ArrayList<>();

    @OneToMany(mappedBy = "parent2", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Person> children2 = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partner_id")
    private Person partner;

    // by default any parent has a parent
    @Column(name = "topParent", nullable = false, columnDefinition = "boolean default false")
    private boolean topParent = false;
}

