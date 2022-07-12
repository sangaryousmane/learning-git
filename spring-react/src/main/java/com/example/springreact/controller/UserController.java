package com.example.springreact.controller;

import com.example.springreact.ems.entities.Role;
import com.example.springreact.ems.entities.User;
import com.example.springreact.ems.security.EmployeesUserDetails;
import com.example.springreact.ems.utils.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public String findUsers(Model model) {
        List<User> users = userService.findAllUsers();
        List<Role> listRoles = userService.roleList();
        model.addAttribute("users", users);
        model.addAttribute("listRoles", listRoles);
        return "users";
    }

    // Endpoint For to create new form
    @GetMapping("/users/addNewUsers")
    public String createNewForm(Model model) {
        User user = new User();
        List<Role> listRoles=userService.roleList();
        model.addAttribute("user", user);
        model.addAttribute("listRoles", listRoles);
        return "user_form";
    }

    // Handle User Details Info
    @GetMapping("/users/details")
    public String userDetails(
            @AuthenticationPrincipal EmployeesUserDetails userDetails,
            Model model){
        String email=userDetails.getUsername();
        User user=userService.getUserByEmail(email);
        model.addAttribute("user", user);
        return "congrats";
    }
    // Handle User create account
    @GetMapping("/users/register")
    public String newAccount(Model model){
        User user=new User();
        List<Role> listRoles=userService.roleList();
        model.addAttribute("user", user);
        model.addAttribute("listRoles", listRoles);
        return "new_account";
    }

    // Endpoint For to get the update form
    @GetMapping("/users/update/{id}")
    public String userById(@PathVariable("id") Long id,
                           Model model) {
        User user = userService.getUserById(id);
        List<Role> listRoles=userService.roleList();
        model.addAttribute("user", user);
        model.addAttribute("listRoles", listRoles);
        return "update_users";
    }

    // Delete Endpoint
    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return "redirect:/users";
    }

    // Endpoint to save a user
    @PostMapping("/users/save")
    public String saveUser(@ModelAttribute("user") User user) {
        userService.saveUser(user);
        return "redirect:/users";
    }

    @PostMapping("/users/register")
    public String registerNewUser(@ModelAttribute("user") User user){
        userService.saveUser(user);
        return "congrats";
    }
}
