package com.emanueltobias.etfood.api.controller;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.util.ReflectionUtils;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.SmartValidator;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.emanueltobias.etfood.core.validation.ValidacaoException;
import com.emanueltobias.etfood.domain.exception.CozinhaNaoEncontradaException;
import com.emanueltobias.etfood.domain.exception.EntidadeNaoEncontradaException;
import com.emanueltobias.etfood.domain.exception.NegocioException;
import com.emanueltobias.etfood.domain.model.Restaurante;
import com.emanueltobias.etfood.domain.repository.RestauranteRepository;
import com.emanueltobias.etfood.domain.service.RestauranteService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/restaurantes")
public class RestauranteController {
	
	@Autowired
	RestauranteRepository restauranteRepository;
	
	@Autowired
	RestauranteService cadastroRestauranteService;
	
	@Autowired
	private SmartValidator smartValidator;
	
	@GetMapping
	public List<Restaurante> listar() {
		return restauranteRepository.findAll();
	}
	
	@GetMapping("/{idRestaurante}")
	public Restaurante buscar(@PathVariable Long idRestaurante) {
	    return cadastroRestauranteService.buscarOuFalhar(idRestaurante);
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Restaurante adicionar(@RequestBody @Valid Restaurante restaurante) {
	    try {
	        return cadastroRestauranteService.salvar(restaurante);
	    } catch (CozinhaNaoEncontradaException e) {
	        throw new NegocioException(e.getMessage(), e);
	    }
	}

	@PutMapping("/{idRestaurante}")
	public Restaurante atualizar(@PathVariable Long idRestaurante,
	        @RequestBody @Valid Restaurante restaurante) {
	    try {
	    	Restaurante restauranteAtual = cadastroRestauranteService.buscarOuFalhar(idRestaurante);
	    	
	    	BeanUtils.copyProperties(restaurante, restauranteAtual, 
	    			"id", "formasPagamento", "endereco", "dataCadastro", "produtos");
	    	
	        return cadastroRestauranteService.salvar(restauranteAtual);
	    } catch (CozinhaNaoEncontradaException e) {
	        throw new NegocioException(e.getMessage(), e);
	    }
	}
	
	@DeleteMapping("/{idRestaurante}")
	public ResponseEntity<Restaurante> remover(@PathVariable Long idRestaurante) {
		try {
			cadastroRestauranteService.excluir(idRestaurante);
			return ResponseEntity.noContent().build();

		} catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.notFound().build();
		}
	}
	
	@PatchMapping("/{idRestaurante}")
	public Restaurante atualizarParcial(@PathVariable Long idRestaurante,
	        @RequestBody Map<String, Object> campos, HttpServletRequest request) {
	    Restaurante restauranteAtual = cadastroRestauranteService.buscarOuFalhar(idRestaurante);
	    
	    merge(campos, restauranteAtual, request);
	    validate(restauranteAtual, "restaurante");
	    
	    return atualizar(idRestaurante, restauranteAtual);
	}
	
	private void validate(Restaurante restaurante, String objetctName) {
		BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(restaurante, objetctName);
		smartValidator.validate(restaurante, bindingResult);
		
		if (bindingResult.hasErrors()) {
			throw new ValidacaoException(bindingResult);
		}
		
	}

	private void merge(Map<String, Object> dadosOrigem, Restaurante restauranteDestino, HttpServletRequest request) {
		
		ServletServerHttpRequest serverHttpRequest = new ServletServerHttpRequest(request);
		
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
			objectMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, true);
			
			Restaurante restauranteOrigem = objectMapper.convertValue(dadosOrigem, Restaurante.class);
			
			dadosOrigem.forEach((nomePropriedade, valorPropriedade) -> {
				Field field = ReflectionUtils.findField(Restaurante.class, nomePropriedade);
				field.setAccessible(true);
				
				Object novoValor = ReflectionUtils.getField(field, restauranteOrigem);
				
				ReflectionUtils.setField(field, restauranteDestino, novoValor);
			});
		} catch(IllegalArgumentException e) {
			Throwable rootCause = ExceptionUtils.getRootCause(e);
			throw new HttpMessageNotReadableException(e.getMessage(), rootCause, serverHttpRequest);
		}
	}
	
}
