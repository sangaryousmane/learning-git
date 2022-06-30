package com.example.springreact.ems.jwts;

import com.example.springreact.ems.entities.User;
import com.example.springreact.ems.entities.UserDto;
import com.example.springreact.ems.utils.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/hello")
@RequiredArgsConstructor
public class HelloController {

    private final UserService userService;

    @GetMapping("/getAllStudents")
    public ResponseEntity<List<User>> users() {
        List<User> userList = userService.findAllUsers();
        return ResponseEntity.ok(userList);
    }

    @GetMapping("/getUser/{id}")
    public ResponseEntity<User> user(@PathVariable("id") Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/getUser/delete/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteUser(@PathVariable("id") Long id) {
        boolean isDeleted = userService.deleteUser(id);
        Map<String, Boolean> deleted = new HashMap<>();
        deleted.put("Deleted", isDeleted);
        return ResponseEntity.ok().body(deleted);
    }

    @PutMapping("/getUser/update/{id}")
    public UserDto updateUser(
            @PathVariable("id") Long id,
            @RequestBody UserDto user) {
        return userService.update(id, user);
    }

    @PostMapping("/getUser/save")
    public ResponseEntity<User> saveGetUser(@RequestBody User user) {
        User saveUser = userService.saveUser(user);
        return ResponseEntity.ok(saveUser);
    }
}
