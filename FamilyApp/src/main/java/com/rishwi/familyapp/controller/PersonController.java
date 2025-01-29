package com.rishwi.familyapp.controller;


import com.rishwi.familyapp.model.Person;
import com.rishwi.familyapp.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
// define a controller
@RestController
// declares that all Api's url in the controller start with /api
@RequestMapping("/api")
public class PersonController implements IController<Person> {

    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/persons")
    @Override
    public ResponseEntity<List<Person>> getAllEntities() {
       return this.personService.findAll();
    }

    @GetMapping("/persons/filtered")
    public ResponseEntity<String> getAllFilteredEntities() {
        return this.personService.filterdFindAll();
    }

    @GetMapping("/persons/{id}")
    @Override
    public ResponseEntity<Person> getEntityById(long id) {
        return this.personService.getById(id);
    }

    @PostMapping("/persons")
    @Override
    public ResponseEntity<Person> createEntity(Person entity) {
        return personService.add(entity);
    }

    @PutMapping("/persons/{id}")
    @Override
    public ResponseEntity<Person> updateEntity(long id, Person entity) {
        return this.personService.update(id, entity);
    }

    @DeleteMapping("/persons/{id}")
    @Override
    public ResponseEntity<HttpStatus> deleteEntity(long id) {
        return this.personService.remove(id);
    }

    @DeleteMapping("/persons")
    @Override
    public ResponseEntity<HttpStatus> deleteEntities() {
        return this.personService.removeAll();
    }
}
