package com.SYSC4806_Group13.SYSC4806_Project.Controller;

import com.SYSC4806_Group13.SYSC4806_Project.Exception.ResourceNotFoundException;
import com.SYSC4806_Group13.SYSC4806_Project.Model.User;
import com.SYSC4806_Group13.SYSC4806_Project.Repository.UserRepository;
import com.SYSC4806_Group13.SYSC4806_Project.Security.CurrentUser;
import com.SYSC4806_Group13.SYSC4806_Project.Security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/demo")
public class TestController {

    private final UserRepository userRepository;

    public TestController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    /**
     * Test controller method to return a user after they ahve received their JWT token after authorization
     * @param userPrincipal The user store that was created from the incoming JWT token
     * @return the users Information
     */
    @GetMapping("/profile")
    public User getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
        //If JWT is correct, then the UserPrinciple will have the current user from the JWT
        return userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));
    }
}
