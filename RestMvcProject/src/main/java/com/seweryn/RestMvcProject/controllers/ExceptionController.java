package com.seweryn.RestMvcProject.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

//@ControllerAdvice
public class ExceptionController {
//    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity handleNotFoundException() {
        System.out.println("in exception handler");
        return ResponseEntity.notFound().build();
    }
}
