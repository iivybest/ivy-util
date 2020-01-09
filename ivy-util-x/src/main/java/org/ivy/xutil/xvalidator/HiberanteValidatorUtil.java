package org.ivy.xutil.xvalidator;

import org.ivy.util.annotation.ThreadSafe;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

public class HiberanteValidatorUtil {

    // 它是线程安全的
    @ThreadSafe
    private static Validator validator;

    static {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    public static <T> void validate(T t) throws FieldValidateException {
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(t);
        if (constraintViolations.size() > 0) {
            StringBuffer errors = new StringBuffer("");
            for (ConstraintViolation<T> cv : constraintViolations) {
                errors.append(cv.getMessage()).append(";");
            }
            throw new FieldValidateException(errors.toString());
        }
    }

}