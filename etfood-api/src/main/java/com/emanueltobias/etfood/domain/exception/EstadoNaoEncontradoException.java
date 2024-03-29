package com.emanueltobias.etfood.domain.exception;

public class EstadoNaoEncontradoException extends EntidadeNaoEncontradaException {

	private static final long serialVersionUID = 1L;
	
	public EstadoNaoEncontradoException(String mensagem) {
		super(mensagem);
	}
	
	public EstadoNaoEncontradoException(Long idEstado) {
		this(String.format("Não existe um cadastro de estado com código %d", idEstado));
	}
	
}
