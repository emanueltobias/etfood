package com.emanueltobias.etfood.api.model;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import com.emanueltobias.etfood.domain.model.Cozinha;
import com.emanueltobias.etfood.domain.model.Endereco;
import com.emanueltobias.etfood.domain.model.FormaPagamento;
import com.emanueltobias.etfood.domain.model.Produto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

public abstract class RestauranteMixin {
	
	@JsonIgnoreProperties(value = "nome", allowGetters = true)
	private Cozinha cozinha;
	
	@JsonIgnore
	private Endereco endereco;
	
	//@JsonIgnore
	private OffsetDateTime dataCadastro;
	
	//@JsonIgnore
	private OffsetDateTime dataAtualizacao;
	
	@JsonIgnore
	private List<FormaPagamento> formasPagamento = new ArrayList<>();
	
	@JsonIgnore
	private List<Produto> produtos = new ArrayList<>(); 

}
