package com.emanueltobias.etfood.domain.exception;

public class CozinhaNaoEncontradaException extends EntidadeNaoEncontradaException {

	private static final long serialVersionUID = 1L;
	
	public CozinhaNaoEncontradaException(String mensagem) {
		super(mensagem);
	}
	
	public CozinhaNaoEncontradaException(Long idCozinha) {
		this(String.format("Não existe um cadastro de cozinha com o código %d", idCozinha));
	}
	
}
