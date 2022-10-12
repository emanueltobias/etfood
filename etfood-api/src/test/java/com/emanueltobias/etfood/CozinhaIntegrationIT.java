package com.emanueltobias.etfood;

import static org.assertj.core.api.Assertions.assertThat;

import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.emanueltobias.etfood.domain.exception.CozinhaNaoEncontradaException;
import com.emanueltobias.etfood.domain.exception.EntidadeEmUsoException;
import com.emanueltobias.etfood.domain.model.Cozinha;
import com.emanueltobias.etfood.domain.service.CozinhaService;

@SpringBootTest
class CozinhaIntegrationIT {
	
	@Autowired
	private CozinhaService cozinhaService;

	@Test
	public void testarCadastroCozinhaComSucesso() {
		// cenário
		Cozinha novaCozinha = new Cozinha();
		novaCozinha.setNome("Chinesa");
		
		// ação
		novaCozinha = cozinhaService.salvar(novaCozinha);
		
		// validação
		assertThat(novaCozinha).isNotNull();
		assertThat(novaCozinha.getId()).isNotNull();
	}
	
	@Test()
	public void testarCadastroCozinhaSemNome() {
		Cozinha novaCozinha = new Cozinha();
		novaCozinha.setNome("");

		ConstraintViolationException erroEsperado = 
				Assertions.assertThrows(ConstraintViolationException.class, () -> {
			cozinhaService.salvar(novaCozinha);
		});

		assertThat(erroEsperado).isNotNull();
	}
	
	@Test()
	public void testarExcluirCozinhaInexistente() {

		CozinhaNaoEncontradaException erroEsperado = 
				Assertions.assertThrows(CozinhaNaoEncontradaException.class, () -> {
			cozinhaService.excluir(9999999L);
		});

		assertThat(erroEsperado).isNotNull();
	}
	
	@Test()
	public void testarExcluirCozinhaEmUso() {

		EntidadeEmUsoException erroEsperado = 
				Assertions.assertThrows(EntidadeEmUsoException.class, () -> {
			cozinhaService.excluir(1L);
		});

		assertThat(erroEsperado).isNotNull();
	}

}
