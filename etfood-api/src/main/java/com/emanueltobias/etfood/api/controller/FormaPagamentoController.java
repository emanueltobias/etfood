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

import com.emanueltobias.etfood.api.assembler.FormaPagamentoInputDisassembler;
import com.emanueltobias.etfood.api.assembler.FormaPagamentoModelAssembler;
import com.emanueltobias.etfood.api.model.input.FormaPagamentoInput;
import com.emanueltobias.etfood.api.model.input.FormaPagamentoModel;
import com.emanueltobias.etfood.domain.model.FormaPagamento;
import com.emanueltobias.etfood.domain.repository.FormaPagamentoRepository;
import com.emanueltobias.etfood.domain.service.FormaPagamentoService;

@RestController
@RequestMapping("/formas-pagamento")
public class FormaPagamentoController {

    @Autowired
    private FormaPagamentoRepository formaPagamentoRepository;
    
    @Autowired
    private FormaPagamentoService formaPagamentoService;
    
    @Autowired
    private FormaPagamentoModelAssembler formaPagamentoModelAssembler;
    
    @Autowired
    private FormaPagamentoInputDisassembler formaPagamentoInputDisassembler;
    
    @GetMapping
    public List<FormaPagamentoModel> listar() {
        List<FormaPagamento> todasFormasPagamentos = formaPagamentoRepository.findAll();
        
        return formaPagamentoModelAssembler.toCollectionModel(todasFormasPagamentos);
    }
    
    @GetMapping("/{idFormaPagamento}")
    public FormaPagamentoModel buscar(@PathVariable Long idFormaPagamento) {
        FormaPagamento formaPagamento = formaPagamentoService.buscarOuFalhar(idFormaPagamento);
        
        return formaPagamentoModelAssembler.toModel(formaPagamento);
    }
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FormaPagamentoModel adicionar(@RequestBody @Valid FormaPagamentoInput formaPagamentoInput) {
        FormaPagamento formaPagamento = formaPagamentoInputDisassembler.toDomainObject(formaPagamentoInput);
        
        formaPagamento = formaPagamentoService.salvar(formaPagamento);
        
        return formaPagamentoModelAssembler.toModel(formaPagamento);
    }
    
    @PutMapping("/{idFormaPagamento}")
    public FormaPagamentoModel atualizar(@PathVariable Long idFormaPagamento,
            @RequestBody @Valid FormaPagamentoInput formaPagamentoInput) {
        FormaPagamento formaPagamentoAtual = formaPagamentoService.buscarOuFalhar(idFormaPagamento);
        
        formaPagamentoInputDisassembler.copyToDomainObject(formaPagamentoInput, formaPagamentoAtual);
        
        formaPagamentoAtual = formaPagamentoService.salvar(formaPagamentoAtual);
        
        return formaPagamentoModelAssembler.toModel(formaPagamentoAtual);
    }
    
    @DeleteMapping("/{idFormaPagamento}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long idFormaPagamento) {
        formaPagamentoService.excluir(idFormaPagamento);	
    } 
    
}
