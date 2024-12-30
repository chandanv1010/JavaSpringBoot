package chandanv.local.chandanv.modules.users.resources;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.Getter;


@Getter
public class CustomUserDetail extends User {
    

    private final Long id;

    public CustomUserDetail(Long id, String username, String password, Collection<? extends GrantedAuthority> authorities){
        super(username, password, authorities);
        this.id = id;
    }

}
