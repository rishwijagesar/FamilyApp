package com.rishwi.familyapp.service;

import com.rishwi.familyapp.model.Person;
import com.rishwi.familyapp.repository.PersonRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.StringWriter;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
public class PersonService implements IService<Person> {

    private final PersonRepository personRepository;
    private final static int CHILDEREN_LIMIT = 3;
    private final static int MAX_CHILD_AGE = 18;

    @Autowired
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }


    @Override
    public ResponseEntity<List<Person>> findAll() {
        try{
            return new ResponseEntity<>(new ArrayList<>(this.personRepository.findAll()), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // filtered findAll Method
    public ResponseEntity<String> filterdFindAll(){
        try {
            List<Person> filteredPersons = personRepository.findAll().stream()
                    .filter(person -> person.getPartner() != null
                            && getTotalChildrenCount(person) == CHILDEREN_LIMIT)
                    .toList();

            return ResponseEntity.ok(csvConverter(filteredPersons));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Count direct children from children1 and children2
    private int getTotalChildrenCount(Person person) {
        List<Person> allChildren = Stream.of(person.getChildren1(), person.getChildren2())
                .filter(Objects::nonNull) // Ensure non-null lists
                .flatMap(List::stream) // Flatten both lists into a single stream
                .filter(Objects::nonNull) // Ensure non-null child objects
                .toList();

        long countBelow18 = allChildren.stream()
                .filter(child -> calculateAge(child.getBirthDay()) < MAX_CHILD_AGE) // Count only children below 18
                .count();

        return (int) countBelow18;
    }


    // calculate age from given DateStamp till now
    private int calculateAge(Date birthDate) {
        return Period.between(birthDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), LocalDate.now()).getYears();
    }

    // Convert the filtered list to CSV format
    private String csvConverter(List<Person> filteredPersons){
        StringWriter csvWriter = new StringWriter();
        csvWriter.append("ID,Name,Partner,Child1,Child2,Child3\n");
        for (Person person : filteredPersons) {
            String childrenNames = person.getChildren1().stream()
                    .map(Person::getName)
                    .collect(Collectors.joining(", "));
            csvWriter.append(String.format("%d,%s,%s,%s\n",
                    person.getId(),
                    person.getName(),
                    person.getPartner().getName(),
                    childrenNames
            ));
        }

        // Encode the CSV to Base64
       return Base64.getEncoder().encodeToString(csvWriter.toString().getBytes());
    }

    @Override
    public ResponseEntity<Person> getById(long id) {
        Person person = this.personRepository.findById(id).orElse(null);
        if (person != null) {
            return new ResponseEntity<>(person, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<HttpStatus> remove(long id) {
        try {
            this.personRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Person> add(Person person) {
        try {
            this.personRepository.save(person);
            return new ResponseEntity<>(person, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<HttpStatus> removeAll() {
        try {
            this.personRepository.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Person> update(long id, Person entity) {
        Person person = this.personRepository.findById(id).orElse(null);
        if (person != null) {
            return new ResponseEntity<>(this.personRepository.save(entity), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }


}
