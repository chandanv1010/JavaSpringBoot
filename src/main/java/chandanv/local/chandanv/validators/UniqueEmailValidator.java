package chandanv.local.chandanv.validators;
import chandanv.local.chandanv.modules.users.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import chandanv.local.chandanv.annotations.UniqueEmail;

public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {
    

    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context){
        return !userRepository.existsByEmail(email);
    }

}
