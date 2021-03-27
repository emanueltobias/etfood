package com.emanueltobias.etfood.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.emanueltobias.etfood.domain.exception.EntidadeNaoEncontradaException;
import com.emanueltobias.etfood.domain.model.Cidade;
import com.emanueltobias.etfood.domain.model.Estado;
import com.emanueltobias.etfood.domain.repository.CidadeRepository;
import com.emanueltobias.etfood.domain.repository.EstadoRepository;

@Service
public class CadastroCidadeService {

	@Autowired
	CidadeRepository cidadeRepository;
	
	@Autowired
	EstadoRepository estadoRepository;
	
	public Cidade salvar(Cidade cidade) {
		Long estadoId = cidade.getEstado().getId();

		Estado estado = estadoRepository.buscar(estadoId);

		if (estado == null) {
			throw new EntidadeNaoEncontradaException(
					String.format("Não existe cadastro de estado com o código %d", estadoId));
		}
		
		cidade.setEstado(estado);

		return cidadeRepository.salvar(cidade);
	}
	
	public void excluir(Long cidadeId) {
		try {
			cidadeRepository.remover(cidadeId);

		} catch (EmptyResultDataAccessException e) {
			throw new EntidadeNaoEncontradaException(
					String.format("Não existe um cadastro de cidade com o código %d", cidadeId));

		}
	}

}
