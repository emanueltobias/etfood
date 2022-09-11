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

import com.emanueltobias.etfood.domain.exception.EstadoNaoEncontradoException;
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

	@GetMapping("/{idCidade}")
	public Cidade buscar(@PathVariable Long idCidade) {
	    return cadastroCidadeService.buscarOuFalhar(idCidade);
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Cidade adicionar(@RequestBody @Valid Cidade cidade) {
		try {
			return cadastroCidadeService.salvar(cidade);
		 } catch (EstadoNaoEncontradoException e) {
		    	throw new NegocioException(e.getMessage(), e); 
		    }
	}

	@PutMapping("/{idCidade}")
	public Cidade atualizar(@PathVariable Long idCidade,
	        @RequestBody @Valid Cidade cidade) {	    
	    try {
	    	Cidade cidadeAtual = cadastroCidadeService.buscarOuFalhar(idCidade);
	    	
	    	BeanUtils.copyProperties(cidade, cidadeAtual, "id");
	    	
	    	return cadastroCidadeService.salvar(cidadeAtual);
	    } catch (EstadoNaoEncontradoException e) {
	    	throw new NegocioException(e.getMessage(), e); 
	    }
	    
	}

	
	@DeleteMapping("/{cidadeId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long cidadeId) {
		cadastroCidadeService.excluir(cidadeId);	
	}

}
