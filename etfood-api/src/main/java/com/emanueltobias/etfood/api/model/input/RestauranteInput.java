package com.emanueltobias.etfood.api.model.input;

import java.math.BigDecimal;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.emanueltobias.etfood.core.validation.TaxaFrete;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RestauranteInput {

	@NotBlank
	private String nome;
	
	@TaxaFrete
	private BigDecimal taxaFrete;
	
	@Valid
	@NotNull
	private CozinhaIdInput cozinha;
	
}
