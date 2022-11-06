package com.emanueltobias.etfood.core.modelmapper;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.emanueltobias.etfood.api.model.EnderecoModel;
import com.emanueltobias.etfood.api.model.RestauranteModel;
import com.emanueltobias.etfood.domain.model.Endereco;
import com.emanueltobias.etfood.domain.model.Restaurante;

@Configuration
public class ModelMapperConfig {
	
	@Bean
	public ModelMapper modelMapper() {
		var modelMapper = new ModelMapper();
		
		modelMapper.createTypeMap(Restaurante.class, RestauranteModel.class)
		.addMapping(Restaurante::getTaxaFrete, RestauranteModel::setPrecoFrete)
		.<String>addMapping(
				src -> src.getEndereco().getCidade().getEstado().getNome(),
				(dest, value) -> dest.getEndereco().getCidade().setEstado(value));
		
		return modelMapper;
	}

}
