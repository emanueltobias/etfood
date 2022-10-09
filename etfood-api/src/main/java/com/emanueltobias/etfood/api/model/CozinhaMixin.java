package com.emanueltobias.etfood.api.model;

import java.util.ArrayList;
import java.util.List;

import com.emanueltobias.etfood.domain.model.Restaurante;
import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class CozinhaMixin {
	
	@JsonIgnore
	private List<Restaurante> restaurantes = new ArrayList<>();

}
