package com.jeonggolee.helpanimal.common.handler;

import com.jeonggolee.helpanimal.common.exception.AnimalNotFoundException;
import com.jeonggolee.helpanimal.common.exception.RecruitmentNotFoundException;
import com.jeonggolee.helpanimal.common.exception.UserNotFoundException;
import com.jeonggolee.helpanimal.domain.user.exception.ExceptionStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CommonExceptionHandler {
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ExceptionStatus> userNotFoundException(UserNotFoundException e) {
        ExceptionStatus response = new ExceptionStatus(e.getMessage(), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<ExceptionStatus>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionStatus> methodArgumentNotValidExcepton(MethodArgumentNotValidException e) {
        ExceptionStatus response = new ExceptionStatus(e.getBindingResult()
                .getAllErrors()
                .get(0)
                .getDefaultMessage(), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<ExceptionStatus>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AnimalNotFoundException.class)
    public ResponseEntity<ExceptionStatus> animalNotFoundException(AnimalNotFoundException e) {
        ExceptionStatus response = new ExceptionStatus(e.getMessage(), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<ExceptionStatus>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RecruitmentNotFoundException.class)
    public ResponseEntity<ExceptionStatus> recruitmentNotFoundException(RecruitmentNotFoundException e) {
        ExceptionStatus response = new ExceptionStatus(e.getMessage(), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<ExceptionStatus>(response, HttpStatus.BAD_REQUEST);
    }
}

