package com.emanueltobias.etfood.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.emanueltobias.etfood.domain.exception.EntidadeNaoEncontradaException;
import com.emanueltobias.etfood.domain.model.Cozinha;
import com.emanueltobias.etfood.domain.model.Restaurante;
import com.emanueltobias.etfood.domain.repository.CozinhaRepository;
import com.emanueltobias.etfood.domain.repository.RestauranteRepository;

@Service
public class CadastroRestauranteService {
	
	@Autowired
	RestauranteRepository restauranteRepository;
	
	@Autowired
	CozinhaRepository cozinhaRepository;
	
	public Restaurante salvar(Restaurante restaurante) {
		Long cozinhaId = restaurante.getCozinha().getId();

		Cozinha cozinha = cozinhaRepository.buscar(cozinhaId);

		if (cozinha == null) {
			throw new EntidadeNaoEncontradaException(
					String.format("Não existe cadastro de cozinha com o código %d", cozinhaId));
		}
		
		restaurante.setCozinha(cozinha);

		return restauranteRepository.salvar(restaurante);
	}
	
	public void excluir(Long restauranteId) {
		try {
			restauranteRepository.remover(restauranteId);

		} catch (EmptyResultDataAccessException e) {
			throw new EntidadeNaoEncontradaException(
					String.format("Não existe um cadastro de restaurante com o código %d", restauranteId));

		}
	}

}
