package com.SYSC4806_Group13.SYSC4806_Project.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ListingExceptionController {
    @ExceptionHandler(value = ListingNotFoundException.class)
    public ResponseEntity<Object> exception(ListingNotFoundException exception) {
        return new ResponseEntity<>("Listing not found", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = ListingIdMissingException.class)
    public ResponseEntity<Object> exception(ListingIdMissingException exception) {
        return new ResponseEntity<>("listingId must be specified in the request body", HttpStatus.BAD_REQUEST);
    }
}