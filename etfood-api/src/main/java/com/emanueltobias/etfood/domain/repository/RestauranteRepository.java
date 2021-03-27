package com.emanueltobias.etfood.domain.repository;

import java.util.List;

import com.emanueltobias.etfood.domain.model.Restaurante;

public interface RestauranteRepository {
	
	List<Restaurante> listar();
	Restaurante buscar(Long id);
	Restaurante salvar(Restaurante restaurante);
	void remover(Long id);

}
