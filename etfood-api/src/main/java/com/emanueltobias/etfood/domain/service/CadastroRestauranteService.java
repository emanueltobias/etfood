package com.emanueltobias.etfood.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.emanueltobias.etfood.domain.exception.EntidadeNaoEncontradaException;
import com.emanueltobias.etfood.domain.model.Cozinha;
import com.emanueltobias.etfood.domain.model.Restaurante;
import com.emanueltobias.etfood.domain.repository.RestauranteRepository;

@Service
public class CadastroRestauranteService {
	
	private static final String MSG_RESTAURANTE_NAO_ENCONTRADO = "Não existe um cadastro de restaurante com código %d";
	
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
	
	public void excluir(Long restauranteId) {
		try {
			restauranteRepository.deleteById(restauranteId);

		} catch (EmptyResultDataAccessException e) {
			throw new EntidadeNaoEncontradaException(
					 String.format(MSG_RESTAURANTE_NAO_ENCONTRADO, restauranteId));
		}
	}
	
	public Restaurante buscarOuFalhar(Long restauranteId) {
	    return restauranteRepository.findById(restauranteId)
	        .orElseThrow(() -> new EntidadeNaoEncontradaException(
	                String.format(MSG_RESTAURANTE_NAO_ENCONTRADO, restauranteId)));
	}

}
