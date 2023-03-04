package com.SYSC4806_Group13.SYSC4806_Project;

import com.SYSC4806_Group13.SYSC4806_Project.Model.DataModel.User;
import com.SYSC4806_Group13.SYSC4806_Project.Model.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationSuperUserUtil {

    @Autowired
    UserRepository userRepository;
    private String superUserToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNjc3OTA2MDg3LCJleHAiOjE2Nzg3NzAwODd9.DrW5m8RYEerm4zZXv01-8uzbtF3x7CzpR4lNg_ynxUKejtIEynHuhZjoblPJsPdnHp89hVSoaKKVZKDpu6K9_A";
    public void setSuperUserInRepo(){
        User user = new User();
        user.setId(1L);
        user.setName("Super user");
        user.setEmail("SuperEmail");
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
