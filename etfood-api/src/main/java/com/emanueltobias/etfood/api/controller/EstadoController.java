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

import com.emanueltobias.etfood.api.assembler.EstadoInputDisassembler;
import com.emanueltobias.etfood.api.assembler.EstadoModelAssembler;
import com.emanueltobias.etfood.api.model.EstadoModel;
import com.emanueltobias.etfood.api.model.input.EstadoInput;
import com.emanueltobias.etfood.domain.model.Estado;
import com.emanueltobias.etfood.domain.repository.EstadoRepository;
import com.emanueltobias.etfood.domain.service.EstadoService;

@RestController
@RequestMapping("/estados")
public class EstadoController {
	
	@Autowired
	private EstadoRepository estadoRepository;
	
	@Autowired
	private EstadoService estadoService;
	
	@Autowired
	private EstadoModelAssembler estadoModelAssembler;

	@Autowired
	private EstadoInputDisassembler estadoInputDisassembler;  
	
	@GetMapping
	public List<EstadoModel> listar() {
		return estadoModelAssembler.toCollectionModel(estadoRepository.findAll());
	}
	 
	@GetMapping("/{idEstado}")
	public EstadoModel buscar(@PathVariable Long idEstado) {
		Estado estado = estadoService.buscarOuFalhar(idEstado);
		
	    return estadoModelAssembler.toModel(estado);
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public EstadoModel adicionar(@RequestBody @Valid EstadoInput estadoInput) {
	    Estado estado = estadoInputDisassembler.toDomainObject(estadoInput);

	    estado = estadoService.salvar(estado);
	    
	    return estadoModelAssembler.toModel(estado);
	}
	
	@PutMapping("/{idEstado}")
	public EstadoModel atualizar(@PathVariable Long idEstado,
	        @RequestBody @Valid EstadoInput estadoInput) {
	    Estado estadoAtual = estadoService.buscarOuFalhar(idEstado);
	    
	    estadoInputDisassembler.copyToDomainObject(estadoInput, estadoAtual);
	    
	    estadoAtual = estadoService.salvar(estadoAtual);
	    
	    return estadoModelAssembler.toModel(estadoAtual);
	}

	@DeleteMapping("/{idEstado}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long idEstado) {
		estadoService.excluir(idEstado);	
	}
	
}
