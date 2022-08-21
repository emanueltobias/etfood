package com.emanueltobias.etfood.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.emanueltobias.etfood.domain.exception.CidadeNaoEncontradaException;
import com.emanueltobias.etfood.domain.exception.EntidadeEmUsoException;
import com.emanueltobias.etfood.domain.model.Cidade;
import com.emanueltobias.etfood.domain.model.Estado;
import com.emanueltobias.etfood.domain.repository.CidadeRepository;

@Service
public class CadastroCidadeService {
	
	private static final String MSG_CIDADE_EM_USO = "Cidade de código %d não pode ser removida, pois está em uso";

	@Autowired
	CidadeRepository cidadeRepository;
	
	@Autowired
	CadastroEstadoService cadastroEstadoService;
	
	public Cidade salvar(Cidade cidade) {
	    Long idEstado = cidade.getEstado().getId();

	    Estado estado = cadastroEstadoService.buscarOuFalhar(idEstado);

	    cidade.setEstado(estado);
	    
	    return cidadeRepository.save(cidade);
	}
	
	public void excluir(Long idCidade) {
		try {
			cidadeRepository.deleteById(idCidade);
			
		} catch (EmptyResultDataAccessException e) {
			throw new CidadeNaoEncontradaException(idCidade);

		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(
				String.format(MSG_CIDADE_EM_USO, idCidade));
		}
	}
	
	public Cidade buscarOuFalhar(Long idCidade) {
	    return cidadeRepository.findById(idCidade)
	        .orElseThrow(() -> new CidadeNaoEncontradaException(idCidade));
	}       

}
