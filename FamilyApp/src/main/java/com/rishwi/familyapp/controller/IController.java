package com.rishwi.familyapp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface IController<T>{

    public ResponseEntity<List<T>> getAllEntities();

    public ResponseEntity<T> getEntityById(@PathVariable() long id);

    public ResponseEntity<T> createEntity(@RequestBody T entity);

    public ResponseEntity<T> updateEntity(@PathVariable() long id, @RequestBody T entity);

    public ResponseEntity<HttpStatus> deleteEntity(@PathVariable() long id);

    public ResponseEntity<HttpStatus> deleteEntities();
}
