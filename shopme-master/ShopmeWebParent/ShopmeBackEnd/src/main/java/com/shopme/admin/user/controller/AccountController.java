package com.shopme.admin.user.controller;

import com.shopme.admin.FileUploadUtil;
import com.shopme.admin.user.util.UserService;
import com.shopme.admin.security.ShopmeUserDetails;
import com.shopme.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.io.IOException;

@Controller
public class AccountController {

    @Autowired
    private UserService userService;


    @GetMapping("/account")
    public String viewDetails(
            @AuthenticationPrincipal ShopmeUserDetails loggedUser,
            Model model) {
        String username = loggedUser.getUsername();
        User user = userService.getUserByEmail(username);
        model.addAttribute("user", user);

        return "users/account_form";
    }

    private void fileAndPhotoUpload(
            User user,
            @RequestParam("image") MultipartFile multipartFile) throws IOException {
        // Upload File to the form if not empty.
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        user.setPhotos(fileName);
        User savedUser = userService.updateAccount(user);
        String uploadDir = "user-photos/" + savedUser.getId();
        FileUploadUtil.cleanDir(uploadDir);
        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
    }

    @PostMapping("/account/update")
    public String saveDetails(
            User user, RedirectAttributes redirectAttributes,
            @AuthenticationPrincipal ShopmeUserDetails loggedUser,
            @RequestParam("image") MultipartFile multipartFile) throws IOException {

        if (!multipartFile.isEmpty()) {
            fileAndPhotoUpload(user, multipartFile);
        } else {
            if (user.getPhotos().isEmpty())
                user.setPhotos(null);
            userService.updateAccount(user);
        }

        loggedUser.setFirstName(user.getFirstName());
        loggedUser.setLastName(user.getLastName());
        redirectAttributes.addFlashAttribute("message",
                "Your account details have been updated successfully.");
        return "redirect:/account";
    }

}