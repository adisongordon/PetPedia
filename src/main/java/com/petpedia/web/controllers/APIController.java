package com.petpedia.web.controllers;

import com.petpedia.web.model.Pet;
import com.petpedia.web.model.PetDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.List;

@HttpExchange("/api/pets")
interface PetService {

    @GetMapping(path = "/{species}", produces = MediaType.APPLICATION_JSON_VALUE)
    List<Pet> getSpecies(@PathVariable String species);

}

@RestController
public class APIController implements PetService {

    @Autowired
    private PetDataRepository dataRepository;

    @ResponseStatus(HttpStatus.OK)
    public List<Pet> getSpecies(@PathVariable String species)
    {
        return dataRepository.findBySpecies(species);
    }

}
