package com.booking.accommodations.booking.api.configuration;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.hibernate.validator.parameternameprovider.ReflectionParameterNameProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ValidatorConfig {

    @Bean
    public Validator validator() {
        final ValidatorFactory validatorFactory = Validation.byDefaultProvider()
                                                            .configure()
                                                            .parameterNameProvider(new ReflectionParameterNameProvider())
                                                            .buildValidatorFactory();
        return validatorFactory.getValidator();
    }
}
