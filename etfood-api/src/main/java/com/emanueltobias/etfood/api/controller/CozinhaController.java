package com.emanueltobias.etfood.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.emanueltobias.etfood.api.assembler.CozinhaInputDisassembler;
import com.emanueltobias.etfood.api.assembler.CozinhaModelAssembler;
import com.emanueltobias.etfood.api.model.CozinhaModel;
import com.emanueltobias.etfood.api.model.input.CozinhaInput;
import com.emanueltobias.etfood.domain.model.Cozinha;
import com.emanueltobias.etfood.domain.repository.CozinhaRepository;
import com.emanueltobias.etfood.domain.service.CozinhaService;

@RestController
@RequestMapping("/cozinhas")
public class CozinhaController {
	
	@Autowired
	private CozinhaRepository cozinhaRepository;
	
	@Autowired
	private CozinhaService cozinhaService;
	
	@Autowired
	private CozinhaModelAssembler cozinhaModelAssembler;

	@Autowired
	private CozinhaInputDisassembler cozinhaInputDisassembler;  
	
	@GetMapping
	public List<CozinhaModel> listar() {	
		return cozinhaModelAssembler.toCollectionModel(cozinhaRepository.findAll());	
	}
	 
	@GetMapping("/{idCozinha}") 
	public CozinhaModel buscar(@PathVariable Long idCozinha) {
		Cozinha cozinha = cozinhaService.buscarOuFalhar(idCozinha);	
		
	    return cozinhaModelAssembler.toModel(cozinha);
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public CozinhaModel adicionar(@RequestBody @Valid CozinhaInput cozinhaInput) {
		Cozinha cozinha = cozinhaInputDisassembler.toDomainObject(cozinhaInput);
		cozinha = cozinhaService.salvar(cozinha);

		return cozinhaModelAssembler.toModel(cozinha);
	}
	
	@PutMapping("/{idCozinha}")
	public CozinhaModel atualizar(@PathVariable Long idCozinha,
			@RequestBody @Valid CozinhaInput cozinhaInput) {
		Cozinha cozinhaAtual = cozinhaService.buscarOuFalhar(idCozinha);

		cozinhaInputDisassembler.copyToDomainObject(cozinhaInput, cozinhaAtual);
		cozinhaAtual = cozinhaService.salvar(cozinhaAtual);

		return cozinhaModelAssembler.toModel(cozinhaAtual);
	}
	
	@DeleteMapping("/{idCozinha}") 
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long idCozinha) {
		cozinhaService.excluir(idCozinha);
	}
	
}
