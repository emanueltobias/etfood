package com.emanueltobias.etfood.api.assembler;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.emanueltobias.etfood.api.model.input.FormaPagamentoModel;
import com.emanueltobias.etfood.domain.model.FormaPagamento;

@Component
public class FormaPagamentoModelAssembler {

    @Autowired
    private ModelMapper modelMapper;
    
    public FormaPagamentoModel toModel(FormaPagamento formaPagamento) {
        return modelMapper.map(formaPagamento, FormaPagamentoModel.class);
    }
    
    public List<FormaPagamentoModel> toCollectionModel(List<FormaPagamento> formasPagamentos) {
        return formasPagamentos.stream()
                .map(formaPagamento -> toModel(formaPagamento))
                .collect(Collectors.toList());
    }
    
}
