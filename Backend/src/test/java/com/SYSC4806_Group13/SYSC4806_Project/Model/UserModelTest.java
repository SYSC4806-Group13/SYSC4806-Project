package com.SYSC4806_Group13.SYSC4806_Project.Model;

import com.SYSC4806_Group13.SYSC4806_Project.Model.DataModel.User;
import com.SYSC4806_Group13.SYSC4806_Project.Model.Repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.util.Optional;

@SpringBootTest
public class UserModelTest {

    @Autowired
    UserRepository userRepository;

    @Test
    public void testFindByEmail() {
        userRepository.deleteAll();

        User user = new User();
        user.setEmail("email");
        user.setId(1L);

        userRepository.save(user);

        Optional<User> userToTest = userRepository.findByEmail("email");
        Assert.isTrue(userToTest.isPresent(), "There should be a user");

        Assert.isTrue(userToTest.get().getEmail().equals("email"), "The email should be 'email'");
    }

    @Test
    public void testFindUserById() {
        userRepository.deleteAll();

        User user = new User();
        user.setEmail("email");
        userRepository.save(user);


        User userToTest = userRepository.findUserById(user.getId());
        Assert.isTrue(userToTest != null, "There should be a user");

        Assert.isTrue(userToTest.getId() == user.getId(), "The id of the retrieved user should match the saved user");
    }
}
