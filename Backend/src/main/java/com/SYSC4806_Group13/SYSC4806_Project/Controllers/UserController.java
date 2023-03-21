package com.SYSC4806_Group13.SYSC4806_Project.Controllers;

import com.SYSC4806_Group13.SYSC4806_Project.Exceptions.ResourceNotFoundException;
import com.SYSC4806_Group13.SYSC4806_Project.Model.DataModel.User;
import com.SYSC4806_Group13.SYSC4806_Project.Model.Repositories.UserRepository;
import com.SYSC4806_Group13.SYSC4806_Project.Security.CurrentUser;
import com.SYSC4806_Group13.SYSC4806_Project.Security.UserPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PatchMapping("/becomeSeller")
    public Map becomeSeller(@CurrentUser UserPrincipal userPrincipal) {
        Map<String, String> response = new HashMap<>();
        Optional<User> potentialUser = userRepository.findByIdAndEmail(userPrincipal.getId(), userPrincipal.getEmail());
        if (potentialUser.isPresent()) {
            User user = potentialUser.get();
            user.setIsSeller(true);
            userRepository.save(user);
            response.put("id", user.getId().toString());
            response.put("isSeller", "true");
        } else {
            response.put("error", new ResourceNotFoundException("user", "id", userPrincipal.getId()).toString());
        }
        return response;
    }

    @GetMapping("/userProfile")
    public Map getUserProfile(@CurrentUser UserPrincipal userPrincipal){
        Map<String, String> response = new HashMap<>();
        Optional<User> potentialUser = userRepository.findByIdAndEmail(userPrincipal.getId(), userPrincipal.getEmail());
        if (potentialUser.isPresent()) {
            User user = potentialUser.get();
            userRepository.save(user);
            response.put("id", user.getId().toString());
            response.put("name", user.getName());
            response.put("email", user.getEmail());
            response.put("isSeller", user.getIsSeller().toString());
        } else {
            response.put("error", new ResourceNotFoundException("user", "email", userPrincipal.getEmail()).toString());
        }
        return response;
    }
}
