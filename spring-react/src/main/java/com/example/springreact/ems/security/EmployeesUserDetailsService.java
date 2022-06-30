package com.example.springreact.ems.security;
import com.example.springreact.ems.entities.User;
import com.example.springreact.ems.utils.user_role_repo.UserRepository;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@NoArgsConstructor
public class EmployeesUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user=userRepository.findByEmail(email);
        if (user !=null){
            return new EmployeesUserDetails(user);
        }
        throw new UsernameNotFoundException("Ouch! User not found...");
    }
}
