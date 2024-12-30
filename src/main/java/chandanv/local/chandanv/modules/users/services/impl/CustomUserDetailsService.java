package chandanv.local.chandanv.modules.users.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import chandanv.local.chandanv.modules.users.entities.User;
import chandanv.local.chandanv.modules.users.repositories.UserRepository;
import chandanv.local.chandanv.modules.users.resources.CustomUserDetail;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements  UserDetailsService {
 
    private final UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException{

        User user = userRepository.findById(Long.valueOf(userId)).orElseThrow(() -> new UsernameNotFoundException("User không tồn tại"));

        

        List<GrantedAuthority> authorities = user.getUserCatalogues().stream()
            .flatMap(catalogue -> catalogue.getPermissions().stream())
            .map(permission -> new SimpleGrantedAuthority(permission.getName()))
            .collect(Collectors.toList());

        
        logger.info("authorities: {}", authorities.size());

        return new CustomUserDetail(
            user.getId(),
            user.getEmail(),
            user.getPassword(),
            authorities
        );

        // return new org.springframework.security.core.userdetails.User(
        //     user.getEmail(),
        //     user.getPassword(),
        //     authorities
        // );
    }


}
