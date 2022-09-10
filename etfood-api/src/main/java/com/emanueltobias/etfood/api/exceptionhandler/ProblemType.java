package com.emanueltobias.etfood.api.exceptionhandler;

import lombok.Getter;

@Getter
public enum ProblemType {
	
	ERRO_DE_SISTEMA("Erro de sistema", "/erro-de-sistema"),
	PARAMETRO_INVALIDO("Parâmetro inválido", "/parametro-invalido"),
	MENSAGEM_INCOMPREENSIVEL("Mensagem incompreensível", "/mensagem-incompreensivel"),
	ERRO_NEGOCIO("Violação de regra de negócio", "/erro-negocio"),
	RECURSO_NAO_ENCONTRADO("Recurso não encontrado", "/recurso-nao-encontrado"),
	ENTIDADE_EM_USO("Entidade em uso", "/entidade-em-uso"),
	DADOS_INVALIDOS("Dados inválidos", "/dados-invalidos");
	
	private String title;
	private String uri;
	
	private ProblemType(String title, String path) {
		this.title = title;
		this.uri = "https://etfood.com.br" + path;
	}
	
	

}
