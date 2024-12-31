package chandanv.local.chandanv.annotations;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import chandanv.local.chandanv.validators.UniqueEmailValidator;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy= UniqueEmailValidator.class)
public @interface UniqueEmail {
    String message() default "Email đã tồn tại trong hệ thống, hãy chọn email khác";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
