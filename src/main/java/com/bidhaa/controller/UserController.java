package com.bidhaa.controller;

import com.bidhaa.dto.*;
import com.bidhaa.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("signup")
    public ResponseEntity<GenericResponse> signup(@RequestBody @Valid CreateUserRequest request, Errors errors){
        return userService.create(request,errors);
    }

    @PostMapping("login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginUserRequest request,Errors errors){

        return userService.login(request,errors);
    }

    @GetMapping
    public ResponseEntity<GetUsersResponse> getAll(){
        return userService.getAll();
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
