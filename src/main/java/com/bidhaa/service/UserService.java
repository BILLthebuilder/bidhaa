package com.bidhaa.service;


import com.bidhaa.dto.*;
import com.bidhaa.mappers.UserMapper;
import com.bidhaa.mappers.UserMapperUpdate;
import com.bidhaa.model.Privilege;
import com.bidhaa.model.Product;
import com.bidhaa.model.Role;
import com.bidhaa.model.User;
import com.bidhaa.repository.PrivilegeRepository;
import com.bidhaa.repository.RoleRepository;
import com.bidhaa.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
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
import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;


    private final PrivilegeRepository privilegeRepository;
    private final PasswordEncoder passwordEncoder;

    private final CustomUserDetailsService customUserDetailsService;

    private final AuthenticationManager authenticationManager;
    private final JwtEncoder jwtEncoder;

    private final UserMapper userMapper;

    private final GenericService genericService;

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
            if (!emailExists(request.email())) {
                var user = userMapper.toUser(request);
                Privilege readPrivilege = createPrivilegeIfNotFound("READ_PRIVILEGE");
                Privilege writePrivilege = createPrivilegeIfNotFound("WRITE_PRIVILEGE");
                log.info(request.role());
                if (request.role().equals("ADMIN")) {
                    List<Privilege> adminPrivileges = Arrays.asList(readPrivilege, writePrivilege);
                    createRoleIfNotFound("ROLE_ADMIN", adminPrivileges);
                    Role adminRole = roleRepository.findByName("ROLE_ADMIN");
                    log.warn(String.valueOf(adminRole));
                    user.setRoles(Arrays.asList(adminRole));
                } else {
                    Role userRole = roleRepository.findByName("ROLE_USER");
                    user.setRoles(Arrays.asList(userRole));
                    createRoleIfNotFound("ROLE_USER", Arrays.asList(readPrivilege));
                }
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
            log.error("Unable to create user", sw);
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
                response = new LoginResponse(fieldError.getDefaultMessage(), token.orElse(""), Status.FAILED);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(response);
            }

            var authentication =
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(request.email(), request.password()));
            if (!authentication.isAuthenticated()) {
                response = new LoginResponse("Unable to Login", token.orElse(""), Status.FAILED);
                return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
            } else {
                final UserDetails userDetails = customUserDetailsService.loadUserByUsername(request.email());
                userDetails.getAuthorities().stream().map(s ->
                                new SimpleGrantedAuthority(s.getAuthority())).
                        filter(Objects::nonNull).
                        collect(Collectors.toList());
                String roles = userDetails.getAuthorities().stream().map(s ->
                                new SimpleGrantedAuthority(s.getAuthority())).
                        filter(Objects::nonNull).
                        collect(Collectors.toList()).toString();
//                log.warn(roles);
                var now = Instant.now();
                var expiry = 36000L;

                var claims =
                        JwtClaimsSet.builder()
                                .issuer("bidhaa")
                                .issuedAt(now)
                                .expiresAt(now.plusSeconds(expiry))
                                .subject("something")
                                .claim("roles", roles)
                                .build();
                token = this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue().describeConstable();
                response = new LoginResponse("User logged in successfully", token.orElse(""), Status.SUCCESS);
                return ResponseEntity.ok()
                        .header(HttpHeaders.AUTHORIZATION, token.orElse(""))
                        .body(response);
            }
        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            log.error("Login Error", ex);
            response = new LoginResponse("Login Error", token.orElse(""), Status.FAILED);
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
    }


    public ResponseEntity<Optional<User>> getOne(String id) {
        return genericService.getOne(id, userRepository);
    }

    public ResponseEntity<GetEntitiesResponse<User>> getAll(int page, int size, String sortBy, String sortOrder) {
        return genericService.getAll( userRepository,page, size,sortBy,sortOrder);
    }


    public ResponseEntity<GenericResponse> update(String id, UpdateUserRequest request, Errors errors) {
        return genericService.update(UUID.fromString(id), request, errors, User.class, userRepository);
    }

    public ResponseEntity<GenericResponse> delete(String id) {
        return genericService.delete(id, userRepository);
    }

    public boolean emailExists(String email) {
        boolean isPresent = false;
        if (userRepository.findByEmail(email) != null) {
            isPresent = true;
        }
        return isPresent;
    }

    Privilege createPrivilegeIfNotFound(String name) {

        Privilege privilege = privilegeRepository.findByName(name);
        if (privilege == null) {
            privilege = new Privilege();
            privilege.setName(name);
            privilege.setStatus(true);
            privilegeRepository.save(privilege);
        }
        return privilege;
    }


    void createRoleIfNotFound(String name, Collection<Privilege> privileges) {

        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role();
            role.setName(name);
            role.setStatus(true);
            role.setPrivileges(privileges);
            roleRepository.save(role);
        }
    }
}
