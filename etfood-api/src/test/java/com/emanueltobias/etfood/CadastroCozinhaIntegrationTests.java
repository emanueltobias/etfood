package com.emanueltobias.etfood;

import static org.assertj.core.api.Assertions.assertThat;

import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.emanueltobias.etfood.domain.model.Cozinha;
import com.emanueltobias.etfood.domain.service.CadastroCozinhaService;

@SpringBootTest
class CadastroCozinhaIntegrationTests {
	
	@Autowired
	private CadastroCozinhaService cadastroCozinhaService;

	@Test
	public void testarCadastroCozinhaComSucesso() {
		// cenário
		Cozinha novaCozinha = new Cozinha();
		novaCozinha.setNome("Chinesa");
		
		// ação
		novaCozinha = cadastroCozinhaService.salvar(novaCozinha);
		
		// validação
		assertThat(novaCozinha).isNotNull();
		assertThat(novaCozinha.getId()).isNotNull();
	}
	
	@Test()
	public void testarCadastroCozinhaSemNome() {
		Cozinha novaCozinha = new Cozinha();
		novaCozinha.setNome(null);

		ConstraintViolationException erroEsperado = 
				Assertions.assertThrows(ConstraintViolationException.class, () -> {
			cadastroCozinhaService.salvar(novaCozinha);
		});

		assertThat(erroEsperado).isNotNull();
	}

}
