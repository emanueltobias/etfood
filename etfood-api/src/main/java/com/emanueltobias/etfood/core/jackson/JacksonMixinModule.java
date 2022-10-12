package com.emanueltobias.etfood.core.jackson;

import org.springframework.stereotype.Component;

import com.emanueltobias.etfood.api.model.mixin.CidadeMixin;
import com.emanueltobias.etfood.api.model.mixin.CozinhaMixin;
import com.emanueltobias.etfood.api.model.mixin.RestauranteMixin;
import com.emanueltobias.etfood.domain.model.Cidade;
import com.emanueltobias.etfood.domain.model.Cozinha;
import com.emanueltobias.etfood.domain.model.Restaurante;
import com.fasterxml.jackson.databind.module.SimpleModule;

@Component
public class JacksonMixinModule extends SimpleModule {

	private static final long serialVersionUID = 1L;
	
	public JacksonMixinModule() {
		setMixInAnnotation(Restaurante.class, RestauranteMixin.class);
		setMixInAnnotation(Cidade.class, CidadeMixin.class);
	    setMixInAnnotation(Cozinha.class, CozinhaMixin.class);
	}

}
