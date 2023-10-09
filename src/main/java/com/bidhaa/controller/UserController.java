package com.bidhaa.controller;

import com.bidhaa.dto.*;
import com.bidhaa.model.User;
import com.bidhaa.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("create")
    public ResponseEntity<GenericResponse> signup(@RequestBody @Valid CreateUserRequest request, Errors errors){
        return userService.create(request,errors);
    }

    @PostMapping("login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginUserRequest request,Errors errors){

        return userService.login(request,errors);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<User>> getOne(@PathVariable String id){
        return userService.getOne(id);
    }

    @GetMapping
    public ResponseEntity<GetEntitiesResponse<User>> getAll(@RequestParam(name = "page", defaultValue = "0") int page,
                                                            @RequestParam(name = "size", defaultValue = "10") int size,
                                                            @RequestParam(name = "sort", required = false) String sortBy,
                                                            @RequestParam(name = "order", defaultValue = "asc") String sortOrder){
        return userService.getAll(page,size,sortBy,sortOrder);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GenericResponse> update(@RequestBody @Valid UpdateUserRequest request,@PathVariable String id,Errors errors){
        return userService.update(id,request,errors);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GenericResponse> delete(@PathVariable String id){
        return userService.delete(id);
    }
}
