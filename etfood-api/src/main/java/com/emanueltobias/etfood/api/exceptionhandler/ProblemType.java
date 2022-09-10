package com.emanueltobias.etfood.api.exceptionhandler;

import lombok.Getter;

@Getter
public enum ProblemType {
	
	ENTIDADE_NAO_ENCONTRADA("Entidade não encontrada", "/entidade-nao-encontrada");
	
	private String title;
	private String uri;
	
	private ProblemType(String title, String path) {
		this.title = title;
		this.uri = "https://etfood.com.br" + path;
	}
	
	

}
