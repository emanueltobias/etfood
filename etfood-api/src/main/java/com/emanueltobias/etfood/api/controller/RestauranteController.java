package com.emanueltobias.etfood.api.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.emanueltobias.etfood.api.model.CozinhaModel;
import com.emanueltobias.etfood.api.model.RestauranteModel;
import com.emanueltobias.etfood.api.model.input.RestauranteInput;
import com.emanueltobias.etfood.domain.exception.CozinhaNaoEncontradaException;
import com.emanueltobias.etfood.domain.exception.EntidadeNaoEncontradaException;
import com.emanueltobias.etfood.domain.exception.NegocioException;
import com.emanueltobias.etfood.domain.model.Cozinha;
import com.emanueltobias.etfood.domain.model.Restaurante;
import com.emanueltobias.etfood.domain.repository.RestauranteRepository;
import com.emanueltobias.etfood.domain.service.RestauranteService;

@RestController
@RequestMapping("/restaurantes")
public class RestauranteController {
	
	@Autowired
	RestauranteRepository restauranteRepository;
	
	@Autowired
	RestauranteService restauranteService;
	
	@GetMapping
	public List<RestauranteModel> listar() {
		return toCollectionModel(restauranteRepository.findAll());
	}
	
	@GetMapping("/{idRestaurante}")
	public RestauranteModel buscar(@PathVariable Long idRestaurante) {
	    Restaurante restaurante = restauranteService.buscarOuFalhar(idRestaurante);

		return toModel(restaurante);
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public RestauranteModel adicionar(@RequestBody @Valid RestauranteInput restauranteInput) {
	    try {
			Restaurante restaurante = toDomainObject(restauranteInput);
			
			return toModel(restauranteService.salvar(restaurante));
	    } catch (CozinhaNaoEncontradaException e) {
	        throw new NegocioException(e.getMessage(), e);
	    }
	}

	@PutMapping("/{idRestaurante}")
	public RestauranteModel atualizar(@PathVariable Long idRestaurante,
	        @RequestBody @Valid RestauranteInput restauranteInput) {
	    try {
	    	Restaurante restaurante = toDomainObject(restauranteInput);
	    	
	    	Restaurante restauranteAtual = restauranteService.buscarOuFalhar(idRestaurante);
	    	
	    	BeanUtils.copyProperties(restaurante, restauranteAtual, 
	    			"id", "formasPagamento", "endereco", "dataCadastro", "produtos");
	    	
	        return toModel(restauranteService.salvar(restauranteAtual));
	    } catch (CozinhaNaoEncontradaException e) {
	        throw new NegocioException(e.getMessage(), e);
	    }
	}
	
	@DeleteMapping("/{idRestaurante}")
	public ResponseEntity<Restaurante> remover(@PathVariable Long idRestaurante) {
		try {
			restauranteService.excluir(idRestaurante);
			return ResponseEntity.noContent().build();

		} catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.notFound().build();
		}
	}	
	
	private RestauranteModel toModel(Restaurante restaurante) {
		CozinhaModel cozinhaModel = new CozinhaModel();
		cozinhaModel.setId(restaurante.getCozinha().getId());
		cozinhaModel.setNome(restaurante.getCozinha().getNome());
		
		RestauranteModel restauranteModel = new RestauranteModel();
		restauranteModel.setId(restaurante.getId());
		restauranteModel.setNome(restaurante.getNome());
		restauranteModel.setTaxaFrete(restaurante.getTaxaFrete());
		restauranteModel.setCozinha(cozinhaModel);
		return restauranteModel;
	}
	
	private List<RestauranteModel> toCollectionModel(List<Restaurante> restaurantes) {
		return restaurantes.stream()
				.map(restaurante -> toModel(restaurante))
				.collect(Collectors.toList());
	}
	
	private Restaurante toDomainObject(RestauranteInput restauranteInput) {
		Restaurante restaurante = new Restaurante();
		restaurante.setNome(restauranteInput.getNome());
		restaurante.setTaxaFrete(restauranteInput.getTaxaFrete());
		
		Cozinha cozinha = new Cozinha();
		cozinha.setId(restauranteInput.getCozinha().getId());
		
		restaurante.setCozinha(cozinha);
		
		return restaurante;
	}
	
}
