package com.petpedia.web.model;

import jakarta.persistence.*;

import java.util.HashMap;
import java.util.Map;

@Entity
public class WikiData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String breed;
    private String species;
    private String imgUrl;
    private String description;

    @ElementCollection(fetch = FetchType.EAGER)
    private Map<String, String> tableInfo = new HashMap<String, String>();

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Map<String, String> getTableInfo() {
        return tableInfo;
    }

    public void setTableInfo(Map<String, String> tableInfo) {
        this.tableInfo = tableInfo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
