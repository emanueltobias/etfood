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

import com.emanueltobias.etfood.api.assembler.CidadeInputDisassembler;
import com.emanueltobias.etfood.api.assembler.CidadeModelAssembler;
import com.emanueltobias.etfood.api.model.CidadeModel;
import com.emanueltobias.etfood.api.model.input.CidadeInput;
import com.emanueltobias.etfood.domain.exception.EstadoNaoEncontradoException;
import com.emanueltobias.etfood.domain.exception.NegocioException;
import com.emanueltobias.etfood.domain.model.Cidade;
import com.emanueltobias.etfood.domain.repository.CidadeRepository;
import com.emanueltobias.etfood.domain.service.CidadeService;

@RestController
@RequestMapping("/cidades")
public class CidadeController {

	@Autowired
	CidadeRepository cidadeRepository;

	@Autowired
	CidadeService cidadeService;
	
	@Autowired
	private CidadeModelAssembler cidadeModelAssembler;

	@Autowired
	private CidadeInputDisassembler cidadeInputDisassembler; 

	@GetMapping
	public List<CidadeModel> listar() {
	    return cidadeModelAssembler.toCollectionModel(cidadeRepository.findAll());
	}

	@GetMapping("/{idCidade}")
	public CidadeModel buscar(@PathVariable Long idCidade) {
		Cidade cidade = cidadeService.buscarOuFalhar(idCidade);
		
	    return cidadeModelAssembler.toModel(cidade);
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public CidadeModel adicionar(@RequestBody @Valid CidadeInput cidadeInput) {
		 try {
		        Cidade cidade = cidadeInputDisassembler.toDomainObject(cidadeInput);
		        
		        cidade = cidadeService.salvar(cidade);
		        
		        return cidadeModelAssembler.toModel(cidade);
		    } catch (EstadoNaoEncontradoException e) {
		        throw new NegocioException(e.getMessage(), e);
		    }
		}


	@PutMapping("/{idCidade}")
	public CidadeModel atualizar(@PathVariable Long idCidade,
	        @RequestBody @Valid CidadeInput cidadeInput) {	    
	    try {
	    	Cidade cidadeAtual = cidadeService.buscarOuFalhar(idCidade);
	    	
	    	cidadeInputDisassembler.copyToDomainObject(cidadeInput, cidadeAtual);
	        
	        cidadeAtual = cidadeService.salvar(cidadeAtual);
	        
	        return cidadeModelAssembler.toModel(cidadeAtual);
	    } catch (EstadoNaoEncontradoException e) {
	    	throw new NegocioException(e.getMessage(), e); 
	    }
	}

	
	@DeleteMapping("/{cidadeId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long cidadeId) {
		cidadeService.excluir(cidadeId);	
	}

}
