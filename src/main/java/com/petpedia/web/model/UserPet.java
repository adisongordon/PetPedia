package com.petpedia.web.model;

import jakarta.persistence.*;

@Entity
@Table(name = "user_pet")
public class UserPet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String species;
    private int age;
    @Column(name = "health_issues")
    private String healthIssues;

    @Lob
    @Column(name= "picture", columnDefinition = "LONGBLOB")
    private byte[] picture;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users owner;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getSpecies() {
        return species;
    }
    public void setSpecies(String species) {
        this.species = species;
    }
    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }
    public String getHealthIssues() {
        return healthIssues;
    }
    public void setHealthIssues(String healthIssues) {
        this.healthIssues = healthIssues;
    }
    public byte[] getPicture() {
        return picture;
    }
    public void setPicture(byte[] picture) {
        this.picture = picture;
    }
    public Users getOwner() {
        return owner;
    }
    public void setOwner(Users owner) {
        this.owner = owner;
    }
}
