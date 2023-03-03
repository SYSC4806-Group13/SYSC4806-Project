package com.SYSC4806_Group13.SYSC4806_Project.Service;


import com.SYSC4806_Group13.SYSC4806_Project.Exception.ResourceNotFoundException;
import com.SYSC4806_Group13.SYSC4806_Project.Model.User;
import com.SYSC4806_Group13.SYSC4806_Project.Repository.UserRepository;
import com.SYSC4806_Group13.SYSC4806_Project.Security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with email : " + email)
                );

        return UserPrincipal.create(user);
    }

    public UserDetails loadUserById(Integer id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User", "id", id)
        );

        return UserPrincipal.create(user);
    }
}