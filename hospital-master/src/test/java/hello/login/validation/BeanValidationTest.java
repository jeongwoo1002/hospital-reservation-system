package hello.login.validation;

import hello.login.domain.hospital.Hospital;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

public class BeanValidationTest {

    @Test
    void beanValidation() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Hospital hospital = new Hospital();
        hospital.setItemName("  ");
        hospital.setPrice(0);
        hospital.setQuantity(10000);

        Set<ConstraintViolation<Hospital>> violations = validator.validate(hospital);
        for (ConstraintViolation<Hospital> violation : violations) {
            System.out.println("violation=" + violation);
            System.out.println("violation.message=" + violation.getMessage());
        }

    }
}