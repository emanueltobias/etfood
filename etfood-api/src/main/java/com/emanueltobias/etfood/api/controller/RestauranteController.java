package com.emanueltobias.etfood.api.controller;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.emanueltobias.etfood.domain.exception.EntidadeNaoEncontradaException;
import com.emanueltobias.etfood.domain.model.Restaurante;
import com.emanueltobias.etfood.domain.repository.RestauranteRepository;
import com.emanueltobias.etfood.domain.service.CadastroRestauranteService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/restaurantes")
public class RestauranteController {
	
	@Autowired
	RestauranteRepository restauranteRepository;
	
	@Autowired
	CadastroRestauranteService cadastroRestauranteService;
	
	@GetMapping
	public List<Restaurante> listar() {
		return restauranteRepository.listar();
	}
	
	@GetMapping("/{restauranteId}")
	public ResponseEntity<Restaurante> buscar(@PathVariable("restauranteId") Long id) {
		Restaurante restaurante = restauranteRepository.buscar(id);
		
		if(restaurante == null) {
			return ResponseEntity.notFound().build();
		}
		
		return ResponseEntity.ok(restaurante);
	}
	
	@PostMapping
	public ResponseEntity<?> adicionar(@RequestBody Restaurante restaurante) {
		try {
			cadastroRestauranteService.salvar(restaurante);
			
			return ResponseEntity.status(HttpStatus.CREATED)
					.body(restaurante);
			
		}catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.badRequest()
					.body(e.getMessage());
		}
	}

	@PutMapping("/{restauranteId}")
	public ResponseEntity<?> atualizar(@PathVariable Long restauranteId, @RequestBody Restaurante restaurante) {
		try {
			Restaurante restauranteAtual = restauranteRepository.buscar(restauranteId);
			if (restauranteAtual != null) {
				BeanUtils.copyProperties(restaurante, restauranteAtual, "id");
				restauranteAtual = cadastroRestauranteService.salvar(restauranteAtual);

				return ResponseEntity.ok(restauranteAtual);
			}
			
		} catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		
		return ResponseEntity.notFound().build();
	}
	
	@DeleteMapping("/{restauranteId}")
	public ResponseEntity<Restaurante> remover(@PathVariable Long restauranteId) {
		try {
			cadastroRestauranteService.excluir(restauranteId);
			return ResponseEntity.noContent().build();

		} catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.notFound().build();
		}
	}
	
	@PatchMapping("/{restauranteId}")
	public ResponseEntity<?> atualizarParcial(@PathVariable Long restauranteId, @RequestBody Map<String, Object> campos) {
		Restaurante restauranteAtual = restauranteRepository.buscar(restauranteId);
		
		if(restauranteAtual == null) {
			return ResponseEntity.notFound().build();
			
		}
		
		merge(campos, restauranteAtual);
		
		return atualizar(restauranteId, restauranteAtual);
	}

	private void merge(Map<String, Object> dadosOrigem, Restaurante restauranteDestino) {
		ObjectMapper objectMapper = new ObjectMapper();
		Restaurante restauranteOrigem = objectMapper.convertValue(dadosOrigem, Restaurante.class);
		
		dadosOrigem.forEach((nomePropriedade, valorPropriedade) -> {
			Field field = ReflectionUtils.findField(Restaurante.class, nomePropriedade);
			field.setAccessible(true);
			
			Object novoValor = ReflectionUtils.getField(field, restauranteOrigem);
			
			ReflectionUtils.setField(field, restauranteDestino, novoValor);
		});
	}
}
