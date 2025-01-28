package com.rishwi.familyapp.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IService<T>{

    public ResponseEntity<List<T>> findAll();

    public ResponseEntity<T> getById(long id);

    public ResponseEntity<HttpStatus> remove(long id);

    public ResponseEntity<T> add(T entity);

    public ResponseEntity<HttpStatus> removeAll();

    public ResponseEntity<T> update(long id, T entity);
}
