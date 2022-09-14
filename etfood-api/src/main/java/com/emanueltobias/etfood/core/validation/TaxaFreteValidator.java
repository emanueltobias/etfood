package com.emanueltobias.etfood.core.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class TaxaFreteValidator implements ConstraintValidator<TaxaFrete, Number> {
	
	@Override
	public void initialize(TaxaFrete constraintAnnotation) {
	}
	
	@Override
	public boolean isValid(Number value, ConstraintValidatorContext context) {
		boolean valida = false;
		
		if(value != null) {
			valida = true;
			if((value.doubleValue()) < 0) {
				valida = false;
			}
			if((value.doubleValue()) >= 10) {
				return valida = false;
			}
		}
		
		return valida;
	}

}