package chandanv.local.chandanv.modules.users.services.impl;
import java.util.Collections;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import chandanv.local.chandanv.modules.users.entities.User;
import chandanv.local.chandanv.modules.users.repositories.UserRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements  UserDetailsService {
 
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException{

        User user = userRepository.findById(Long.valueOf(userId)).orElseThrow(() -> new UsernameNotFoundException("User không tồn tại"));

        return new org.springframework.security.core.userdetails.User(
            user.getEmail(),
            user.getPassword(),
            Collections.emptyList()  
        );
    }


}
