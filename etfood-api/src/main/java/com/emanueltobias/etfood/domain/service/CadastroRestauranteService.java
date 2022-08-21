package com.emanueltobias.etfood.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.emanueltobias.etfood.domain.exception.RestauranteNaoEncontradoException;
import com.emanueltobias.etfood.domain.model.Cozinha;
import com.emanueltobias.etfood.domain.model.Restaurante;
import com.emanueltobias.etfood.domain.repository.RestauranteRepository;

@Service
public class CadastroRestauranteService {
		
	@Autowired
	RestauranteRepository restauranteRepository;
	
	@Autowired
	CadastroCozinhaService cadastroCozinhaService;
	
	public Restaurante salvar(Restaurante restaurante) {
		 Long cozinhaId = restaurante.getCozinha().getId();
		    
		    Cozinha cozinha = cadastroCozinhaService.buscarOuFalhar(cozinhaId);
		    
		    restaurante.setCozinha(cozinha);
		    
		    return restauranteRepository.save(restaurante);
	}
	
	public void excluir(Long idRestaurante) {
		try {
			restauranteRepository.deleteById(idRestaurante);

		} catch (EmptyResultDataAccessException e) {
			throw new RestauranteNaoEncontradoException(idRestaurante);
		}
	}
	
	public Restaurante buscarOuFalhar(Long idRestaurante) {
	    return restauranteRepository.findById(idRestaurante)
	        .orElseThrow(() -> new RestauranteNaoEncontradoException(idRestaurante));
	}

}
