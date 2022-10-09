package com.emanueltobias.etfood.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
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

import com.emanueltobias.etfood.domain.model.Cozinha;
import com.emanueltobias.etfood.domain.repository.CozinhaRepository;
import com.emanueltobias.etfood.domain.service.CozinhaService;

@RestController
@RequestMapping("/cozinhas")
public class CozinhaController {
	
	@Autowired
	private CozinhaRepository cozinhaRepository;
	
	@Autowired
	private CozinhaService cadastroCozinhaService;
	
	@GetMapping
	public List<Cozinha> listar() {	
		return cozinhaRepository.findAll();	
	}
	 
	@GetMapping("/{idCozinha}") 
	public Cozinha buscar(@PathVariable Long idCozinha) {
		return cadastroCozinhaService.buscarOuFalhar(idCozinha);	
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Cozinha adicionar(@RequestBody @Valid Cozinha cozinha) {
		return cadastroCozinhaService.salvar(cozinha);
	}
	
	@PutMapping("/{idCozinha}")
	public Cozinha atualizar(@PathVariable Long idCozinha,
			@RequestBody @Valid Cozinha cozinha) {
		Cozinha cozinhaAtual = cadastroCozinhaService.buscarOuFalhar(idCozinha);

		BeanUtils.copyProperties(cozinha, cozinhaAtual, "id");

		return cadastroCozinhaService.salvar(cozinhaAtual);
	}
	
	@DeleteMapping("/{idCozinha}") 
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long idCozinha) {
		cadastroCozinhaService.excluir(idCozinha);
	}
	
}
