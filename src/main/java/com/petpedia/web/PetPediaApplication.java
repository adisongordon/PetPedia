package com.petpedia.web;

import com.petpedia.web.model.WikiDataRepository;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PetPediaApplication {

    @Autowired
    private WikiDataRepository wikiDataRepository;

    public static void main(String[] args) {
        SpringApplication.run(PetPediaApplication.class, args);
    }

    @Bean
    InitializingBean populateDatabase() {
        return () -> {
            // TODO Populate wiki data here
        };
    }

}
