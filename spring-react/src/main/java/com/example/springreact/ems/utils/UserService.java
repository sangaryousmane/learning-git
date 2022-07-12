package com.example.springreact.ems.utils;
import com.example.springreact.ems.entities.Role;
import com.example.springreact.ems.entities.User;
import com.example.springreact.ems.entities.UserDto;
import com.example.springreact.ems.utils.user_role_repo.RoleRepository;
import com.example.springreact.ems.utils.user_role_repo.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;


    public List<User> findAllUsers() {
        log.info("Fetching All Users");
        return userRepository.findAll();
    }

    public List<Role> roleList() {
        log.info("Fetching a user");
        return roleRepository.findAll();
    }

    public boolean deleteUser(Long id) {
        if (id == null) {
            log.error("User id {} is null", id);
            return false;
        } else {
            userRepository.deleteById(id);
            userRepository.flush();
            log.info("Deleting user with id {} ", id);
            return true;
        }
    }

    public User saveUser(User user) {
        encodePassword(user.getPassword());
        log.info("Saving user {} ", user);
        return userRepository.saveAndFlush(user);
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    public User getUserById(Long id) {
        return userRepository.findAll()
                .stream()
                .filter(user -> id.equals(user.getId()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("User with Id " + id + " not found."));
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public UserDto update(Long id, UserDto userDto) {
        User user1 = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(id + " not found."));
        user1.setId(userDto.getId());
        user1.setName(userDto.getName());
        user1.setPassword(encodePassword(userDto.getPassword()));
        user1.setEmail(userDto.getEmail());
        userRepository.save(user1);
        return userDto;
    }
}
