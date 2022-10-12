package com.emanueltobias.etfood.api.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.emanueltobias.etfood.api.model.input.RestauranteInput;
import com.emanueltobias.etfood.domain.model.Cozinha;
import com.emanueltobias.etfood.domain.model.Restaurante;

@Component
public class RestauranteInputDisassembler {
	
	@Autowired
	private ModelMapper modelMapper;

	public Restaurante toDomainObject(RestauranteInput restauranteInput) {
		return modelMapper.map(restauranteInput, Restaurante.class);
	}
	
	public void copyToDomainObject(RestauranteInput restauranteInput, Restaurante restaurante) {
		/*
		 * Para evitar org.hibernate.HibernateException: identifier of an instance of
		 * com.emanueltobias.etfood.domain.model.Cozinha was altered from 3 to 1
		 */
		restaurante.setCozinha(new Cozinha());
		
		modelMapper.map(restauranteInput, restaurante);
	}

}
