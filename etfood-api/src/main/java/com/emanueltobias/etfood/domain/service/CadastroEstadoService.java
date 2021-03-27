package com.emanueltobias.etfood.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.emanueltobias.etfood.domain.exception.EntidadeEmUsoException;
import com.emanueltobias.etfood.domain.exception.EntidadeNaoEncontradaException;
import com.emanueltobias.etfood.domain.model.Estado;
import com.emanueltobias.etfood.domain.repository.EstadoRepository;

@Service
public class CadastroEstadoService {
	
	@Autowired
	EstadoRepository estadoRepository;
	
	public Estado salvar(Estado estado) {
		return estadoRepository.salvar(estado);
	}

	public void excluir(Long estadoId) {
		try {
			estadoRepository.remover(estadoId);

		} catch (EmptyResultDataAccessException e) {
			throw new EntidadeNaoEncontradaException(
					String.format("Não existe um cadastro de estado com o código %d", estadoId));

		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(
					String.format("estado de código %d não pode ser removido, pois está em uso", estadoId));
		}
	}

}