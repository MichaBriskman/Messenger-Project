package hac.controllers;

import hac.repo.User;
import hac.repo.UserRepository;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * Rest api with users name and encrypted passwords
 * { "userName": "asdasd", "password" : "$2a$10$IFg/AK4aU5/Bm3LPTiAj2e7rJrwBv7kuOUNbjTD6JGEiNdD.8mZWK",
 * "confirmPassword" : "$2a$10$IFg/AK4aU5/Bm3LPTiAj2e7rJrwBv7kuOUNbjTD6JGEiNdD.8mZWK"}
 */
@RestController
@RequestMapping("/api")
public class UserRestController {
    @Autowired
    private UserRepository repository;


    @GetMapping("/users")
    public Iterable<User> getUsers() {
        return repository.findAll();
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class, ConstraintViolationException.class})
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
