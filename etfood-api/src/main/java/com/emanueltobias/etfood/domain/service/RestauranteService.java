package com.emanueltobias.etfood.domain.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emanueltobias.etfood.domain.exception.RestauranteNaoEncontradoException;
import com.emanueltobias.etfood.domain.model.Cozinha;
import com.emanueltobias.etfood.domain.model.Restaurante;
import com.emanueltobias.etfood.domain.repository.RestauranteRepository;

@Service
public class RestauranteService {
		
	@Autowired
	RestauranteRepository restauranteRepository;
	
	@Autowired
	CozinhaService cadastroCozinhaService;
	
	@Transactional
	public Restaurante salvar(Restaurante restaurante) {
		 Long cozinhaId = restaurante.getCozinha().getId();
		    
		    Cozinha cozinha = cadastroCozinhaService.buscarOuFalhar(cozinhaId);
		    
		    restaurante.setCozinha(cozinha);
		    
		    return restauranteRepository.save(restaurante);
	}
	
	@Transactional
	public void excluir(Long idRestaurante) {
		try {
			restauranteRepository.deleteById(idRestaurante);
			restauranteRepository.flush();

		} catch (EmptyResultDataAccessException e) {
			throw new RestauranteNaoEncontradoException(idRestaurante);
		}
	}
	
	public Restaurante buscarOuFalhar(Long idRestaurante) {
	    return restauranteRepository.findById(idRestaurante)
	        .orElseThrow(() -> new RestauranteNaoEncontradoException(idRestaurante));
	}

}
