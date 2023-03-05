package com.SYSC4806_Group13.SYSC4806_Project;

import com.SYSC4806_Group13.SYSC4806_Project.Model.DataModel.User;
import com.SYSC4806_Group13.SYSC4806_Project.Model.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationSuperUserUtil {

    @Autowired
    UserRepository userRepository;
    private String superUserToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxfGtldmluZGFmb3hAZ21haWwuY29tIiwiaWF0IjoxNjc3OTc0MDY4LCJleHAiOjEwMDAxNjc3OTc0MDY4fQ.o8344BIRE1Zoqcy28csynjkL2baU1F4oZRrjbJYQMHcPHCmz2ZsTxViwx86gvkaRADe7zNrrKUhox7J2un1wZQ";
    public void setSuperUserInRepo(){
        User user = new User();
        user.setId(1L);
        user.setName("Super user");
        user.setEmail("kevindafox@gmail.com");
        user.setImageUrl("SuperImage");
        user.setIsSeller(false);
        user.setEmailVerified(false);
        user.setPassword("SuperPassword");
        user.setProviderId("115204114328952648829");

        userRepository.save(user);
    }

    public String getSuperUserToken(){
        return this.superUserToken;
    }
}
