package com.emanueltobias.etfood.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.emanueltobias.etfood.domain.exception.EntidadeEmUsoException;
import com.emanueltobias.etfood.domain.exception.EstadoNaoEncontradoException;
import com.emanueltobias.etfood.domain.model.Estado;
import com.emanueltobias.etfood.domain.repository.EstadoRepository;

@Service
public class CadastroEstadoService {
	
	private static final String MSG_ESTADO_EM_USO  = "Estado de código %d não pode ser removido, pois está em uso";

	@Autowired
	EstadoRepository estadoRepository;
	
	public Estado salvar(Estado estado) {
		return estadoRepository.save(estado);
	}

	public void excluir(Long idEstado) {
		try {
			estadoRepository.deleteById(idEstado);

		} catch (EmptyResultDataAccessException e) {
			throw new EstadoNaoEncontradoException(idEstado);

		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(
					String.format(MSG_ESTADO_EM_USO, idEstado));
		}
	}
	
	public Estado buscarOuFalhar(Long idEstado) {
	    return estadoRepository.findById(idEstado)
	        .orElseThrow(() -> new EstadoNaoEncontradoException(idEstado));
	}

}