package com.bidhaa.configuration.loader;

import com.bidhaa.mappers.UserMapper;
import com.bidhaa.model.Privilege;
import com.bidhaa.model.Role;
import com.bidhaa.model.User;
import com.bidhaa.repository.PrivilegeRepository;
import com.bidhaa.repository.RoleRepository;
import com.bidhaa.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SetupDataLoader implements
        ApplicationListener<ContextRefreshedEvent> {

    boolean alreadySetup = false;


    private final UserRepository userRepository;


    private final RoleRepository roleRepository;


    private final PrivilegeRepository privilegeRepository;


    private final PasswordEncoder passwordEncoder;

    private final UserMapper userMapper;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {

        if (alreadySetup)
            return;
        Privilege readPrivilege = createPrivilegeIfNotFound("READ_PRIVILEGE");
        Privilege writePrivilege = createPrivilegeIfNotFound("WRITE_PRIVILEGE");

        List<Privilege> adminPrivileges = Arrays.asList(readPrivilege, writePrivilege);
        createRoleIfNotFound("ROLE_ADMIN", adminPrivileges);
        createRoleIfNotFound("ROLE_USER", Arrays.asList(readPrivilege));

        Role adminRole = roleRepository.findByName("ROLE_ADMIN");
        User user = new User();
        user.setFirstName("Bill");
        user.setLastName("Kariri");
        user.setPassword(passwordEncoder.encode("Pass@1234"));
        user.setEmail("test@gmail.com");
        user.setPhoneNumber("0722000000");
        user.setRoles(Arrays.asList(adminRole));
        user.setStatus(true);
        userRepository.save(user);

        alreadySetup = true;
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
