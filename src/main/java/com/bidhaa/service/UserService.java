package com.bidhaa.service;


import com.bidhaa.dto.*;
import com.bidhaa.mappers.UserMapper;
import com.bidhaa.mappers.UserMapperUpdate;
import com.bidhaa.model.User;
import com.bidhaa.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;



@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;
    private final JwtEncoder jwtEncoder;

    private final UserMapper userMapper;

    private final UserMapperUpdate userMapperUpdate;

    @Transactional
    public ResponseEntity<GenericResponse> create(CreateUserRequest request, Errors errors) {
        GenericResponse response;

        if (errors.hasFieldErrors()) {
            FieldError fieldError = errors.getFieldError();
            response = new GenericResponse(fieldError.getDefaultMessage(), Status.FAILED);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(response);
        }
        try {

            var user = userMapper.toUser(request);
            if (!emailExists(request.email())) {
                user.setStatus(true);
                user.setPassword(passwordEncoder.encode(request.password()));
                userRepository.save(user);
                response = new GenericResponse("User created successfully", Status.SUCCESS);
                return new ResponseEntity<>(response, HttpStatus.CREATED);
            } else {
                response = new GenericResponse("User already exists", Status.FAILED);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            log.error("Unable to create user",sw);
            response = new GenericResponse(ex.getMessage(), Status.FAILED);
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    public ResponseEntity<LoginResponse> login(LoginUserRequest request, Errors errors) {
        Optional<String> token = Optional.empty();
        LoginResponse response = null;
        try {
            if (errors.hasFieldErrors()) {
                FieldError fieldError = errors.getFieldError();
                response = new LoginResponse(fieldError.getDefaultMessage(), token.orElse(""),Status.FAILED);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(response);
            }

            var authentication =
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(request.email(), request.password()));
            if (!authentication.isAuthenticated()) {
                response = new LoginResponse("Unable to Login", token.orElse(""),Status.FAILED);
                return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
            } else {
                var now = Instant.now();
                var expiry = 36000L;
                var claims =
                        JwtClaimsSet.builder()
                                .issuer("example.io")
                                .issuedAt(now)
                                .expiresAt(now.plusSeconds(expiry))
                                .subject("something")
                                .claim("roles", "ROLE_ADMIN")
                                .build();
                token = this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue().describeConstable();
                response = new LoginResponse("User logged in successfully", token.orElse(""),Status.SUCCESS);
                return ResponseEntity.ok()
                        .header(HttpHeaders.AUTHORIZATION, token.orElse(""))
                        .body(response);
            }
        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            log.error("Login Error", sw);
            response = new LoginResponse("Login Error", token.orElse(""),Status.FAILED);
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
    }


    @Transactional
    public ResponseEntity<GenericResponse> update(String id, UpdateUserRequest request, Errors errors) {
        GenericResponse response;
        try {
            if (errors.hasFieldErrors()) {
                FieldError fieldError = errors.getFieldError();
                response = new GenericResponse(fieldError.getDefaultMessage(), Status.FAILED);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(response);
            }
            var user = userRepository.findById(UUID.fromString(id));
            userMapperUpdate.toUpdate(request, user.get());
            userRepository.save(user.get());
            response = new GenericResponse("User updated successfully", Status.SUCCESS);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            log.error("Unable to update user=%s", sw);
            response = new GenericResponse("update failed", Status.FAILED);
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @Transactional
    public ResponseEntity<GenericResponse> delete(String id) {
        GenericResponse response;
        try {
            if (!userExists(id)) {
                response = new GenericResponse("User not Found", Status.FAILED);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            userRepository.deleteById(UUID.fromString(id));
            response = new GenericResponse("User deleted successfully", Status.SUCCESS);
            return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            log.error("Unable to delete user=%s", sw);
            response = new GenericResponse("deleting failed", Status.FAILED);
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }

    }


    public boolean emailExists(String email) {
        boolean isPresent = false;
        if (userRepository.findByEmail(email) != null) {
            isPresent = true;
        }
        return isPresent;
    }

    public boolean userExists(String id) {
        boolean isPresent = false;
        if (userRepository.findById(UUID.fromString(id)) != null) {
            isPresent = true;
        }
        return isPresent;
    }

    public ResponseEntity<GetUsersResponse> getAll() {
        GetUsersResponse response;
        List<User> users = List.of();
        try {
            users = userRepository.findAllByStatus(true);
            if (!users.isEmpty()) {
                response = new GetUsersResponse(Status.SUCCESS, users);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response = new GetUsersResponse(Status.SUCCESS, users);
                return new ResponseEntity<>(response, HttpStatus.OK);
            }

        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            log.error("Error has occured", sw);
            response = new GetUsersResponse(Status.SUCCESS, users);
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }


}
