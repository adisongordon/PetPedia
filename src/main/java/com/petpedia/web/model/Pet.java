package com.petpedia.web.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Setter
@Getter
@Entity(name = "pets")
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;
    @Column
    private String species;
    @Column(unique = true)
    private String breed;
    @Column
    private String imgUrl;
    @Column
    private String infoUrl;
    @Column(length = 4000)
    private String description;
    @Column(length = 4000)
    private String tableInfo;

    // private Map<String, String> tableInfo;

//    public String getFromTable(String key) {
//        return this.tableInfo.get(key);
//    }
//
//    public void setTableValue(String key, String value) {
//        this.tableInfo.put(key, value);
//    }
}

