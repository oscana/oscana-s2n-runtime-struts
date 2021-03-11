package oscana.s2n.sample.form;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import nablarch.core.validation.ConversionFormat;
import nablarch.core.validation.Validation;

@ConversionFormat
@Validation
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Domain {
    /**
     * Domainの種類
     */
    DomainType value();
}