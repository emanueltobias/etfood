package com.emanueltobias.etfood.api.controller;

import java.util.List;

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

import com.emanueltobias.etfood.domain.exception.EntidadeNaoEncontradaException;
import com.emanueltobias.etfood.domain.exception.NegocioException;
import com.emanueltobias.etfood.domain.model.Cidade;
import com.emanueltobias.etfood.domain.repository.CidadeRepository;
import com.emanueltobias.etfood.domain.service.CadastroCidadeService;

@RestController
@RequestMapping("/cidades")
public class CidadeController {

	@Autowired
	CidadeRepository cidadeRepository;

	@Autowired
	CadastroCidadeService cadastroCidadeService;

	@GetMapping
	public List<Cidade> listar() {
		return cidadeRepository.findAll();
	}

	@GetMapping("/{cidadeId}")
	public Cidade buscar(@PathVariable Long cidadeId) {
	    return cadastroCidadeService.buscarOuFalhar(cidadeId);
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Cidade adicionar(@RequestBody Cidade cidade) {
		try {
			return cadastroCidadeService.salvar(cidade);
		 } catch (EntidadeNaoEncontradaException e) {
		    	throw new NegocioException(e.getMessage()); 
		    }
	}

	@PutMapping("/{cidadeId}")
	public Cidade atualizar(@PathVariable Long cidadeId,
	        @RequestBody Cidade cidade) {
	    Cidade cidadeAtual = cadastroCidadeService.buscarOuFalhar(cidadeId);
	    
	    BeanUtils.copyProperties(cidade, cidadeAtual, "id");
	    
	    try {
	    	return cadastroCidadeService.salvar(cidadeAtual);
	    } catch (EntidadeNaoEncontradaException e) {
	    	throw new NegocioException(e.getMessage()); 
	    }
	    
	}

	
	@DeleteMapping("/{cidadeId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long cidadeId) {
		cadastroCidadeService.excluir(cidadeId);	
	}

}
