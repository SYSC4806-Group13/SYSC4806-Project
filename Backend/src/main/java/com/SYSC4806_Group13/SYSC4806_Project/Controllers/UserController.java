package com.SYSC4806_Group13.SYSC4806_Project.Controllers;

import com.SYSC4806_Group13.SYSC4806_Project.Exception.ResourceNotFoundException;
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
    public Map becomeSeller(@CurrentUser UserPrincipal userPrincipal){
        Map<String, String> response = new HashMap<>();
        User user = userRepository.findUserById(userPrincipal.getId());
        if(user != null){
            user.setIsSeller(true);
            userRepository.save(user);
            response.put("id", userPrincipal.getId().toString());
            response.put("isSeller", "true");
        }
        else{
            response.put("error",new ResourceNotFoundException("user", "id", userPrincipal.getId()).toString());
        }
        return response;

    }

}
