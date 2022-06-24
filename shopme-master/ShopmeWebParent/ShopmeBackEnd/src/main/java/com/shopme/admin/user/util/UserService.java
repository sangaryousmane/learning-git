package com.shopme.admin.user.util;

import com.shopme.admin.user.repository.RoleRepository;
import com.shopme.admin.user.repository.UserRepository;
import com.shopme.admin.user.util.UserNotFoundException;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
@Transactional
public class UserService {

    public static final int USER_PER_PAGE = 4;

    private final UserRepository userRepository;
    private final RoleRepository roleRepo;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository,
                       RoleRepository roleRepo,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepo = roleRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public User getUserByEmail(String email){
        return userRepository.getUserByEmail(email);
    }

    public List<User> listAllUsers() {
        return (List<User>) userRepository.findAll(Sort.by("firstName").ascending());
    }

    // Saving and updating User.
    public User save(User user) {
        boolean isUpdatingUser = (user.getId() != null);
        if (isUpdatingUser) {
            User existingUser = userRepository.findById(user.getId()).get();

            if (user.getPassword().isEmpty()) {
                user.setPassword(existingUser.getPassword());
            } else {
                encodePassword(user);
            }
        }
        else {
            encodePassword(user);
        }
        return userRepository.save(user);
    }


    // Update User Account Details
    public User updateAccount(User userInForm){
        User userInDatabase=userRepository.findById(userInForm.getId()).get();


        if (!userInForm.getPassword().isEmpty()){
            userInDatabase.setPassword(userInForm.getPassword());
            encodePassword(userInDatabase);
        }
        if (userInForm.getPhotos() !=null){
            userInDatabase.setPhotos(userInForm.getPhotos());
        }
        userInDatabase.setFirstName(userInForm.getFirstName());
        userInDatabase.setLastName(userInForm.getLastName());

        return userRepository.save(userInDatabase);
    }

    // Fetch all roles from the database role relation
    public List<Role> listRoles() {
        return (List<Role>) roleRepo.findAll();
    }

    // encode and pass password to the save method.
    private void encodePassword(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
    }

    // Check if user is unique
    public boolean isEmailUnique(Integer id, String email) {
        User userByEmail = userRepository.getUserByEmail(email);

        if (userByEmail == null) {
            return true;
        }
        boolean isCreatingNewUser = (id == null);
        if (isCreatingNewUser) {
            if (userByEmail != null) {
                return false;
            }
        } else {
            if (!Objects.equals(userByEmail.getId(), id)) {
                return false;
            }
        }
        return true;
    }

    public User get(Integer id) throws UserNotFoundException {
        try {
            return userRepository.findById(id).get();
        } catch (NoSuchElementException e) {
            throw new UserNotFoundException("Couldn't find any user with id of " + id);
        }
    }

    //Logic for deleting user
    public void delete(Integer id) throws UserNotFoundException {
        Long countById = userRepository.countById(id);
        if (countById == null || countById == 0) {
            throw new UserNotFoundException("Couldn't find any user with the Id of " + id);
        }
        userRepository.deleteById(id);
    }

    public void updateUserEnabledStatus(Integer id, boolean enabled) {
        userRepository.updateEnabledStatus(id, enabled);
    }

    // Implementing pagination
    public Page<User> listByPage(
            int pageNum, String sortField,
            String sortDir, String keyword) {

        Sort sort= Sort.by(sortField);
        sort=sortDir.equals("asc") ? sort.ascending() : sort.descending();
        Pageable pageable = PageRequest.of(pageNum - 1, USER_PER_PAGE, sort);

        if (keyword !=null){
            return userRepository.findAll(keyword, pageable);
        }
        return userRepository.findAll(pageable);
    }

}





















