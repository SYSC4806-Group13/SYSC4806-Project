package com.SYSC4806_Group13.SYSC4806_Project.Service;


import com.SYSC4806_Group13.SYSC4806_Project.Exceptions.ResourceNotFoundException;
import com.SYSC4806_Group13.SYSC4806_Project.Model.DataModel.User;
import com.SYSC4806_Group13.SYSC4806_Project.Model.Repositories.UserRepository;
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

    public UserDetails loadUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User", "id", id)
        );

        return UserPrincipal.create(user);
    }


    public UserDetails loadUserByIdAndEmail(long id, String email) {
        User user = userRepository.findByIdAndEmail(id, email).orElseThrow(
                () -> new ResourceNotFoundException("User", "id email", id+" "+email)
        );

        return UserPrincipal.create(user);
    }
}
