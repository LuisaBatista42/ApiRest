package com.aulas.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aulas.entidades.Contato;
import com.aulas.repository.ContatoRepository;
import com.aulas.services.dto.ContatoDTO;
import com.aulas.services.exceptions.OperacaoNaoAutorizadaException;

@Service
public class ContatoService {

	@Autowired
	ContatoRepository repo;
	
	
	public ContatoDTO salvar(Contato contato) {
//		if(contato.getFone().length() > 14) {
//			throw new ValidacaoException("Telefone inválido");
//		}
//		if(!contato.getEmail().contains("@")) {
//			throw new ValidacaoException("Email inválido");
//		}
		
		Contato ct = repo.save(contato);
		ContatoDTO contatoDTO = new ContatoDTO(ct);
		return contatoDTO;
	}
	
	public List<ContatoDTO> consultarContatos(){ 
		List<Contato> contatos = repo.findAll();
		List<ContatoDTO> contatosDTO = new ArrayList();
		for(Contato ct : contatos) {
			contatosDTO.add(new ContatoDTO(ct));
		}
		return contatosDTO;
	}

	public ContatoDTO consultarContatoPorId(Long idcontato) {
		Optional<Contato> opcontato = repo.findById(idcontato);
		Contato ct = opcontato.orElseThrow( () -> new OperacaoNaoAutorizadaException("Contato não encontrado"));
		return new ContatoDTO(ct);
	}
	
	public Contato consultarContato(Long idcontato) {
		Optional<Contato> opcontato = repo.findById(idcontato);
		Contato ct = opcontato.orElseThrow( () -> new OperacaoNaoAutorizadaException("Contato não encontrado"));
		return ct;
	}
	
	public void excluirContato(Long idcontato) {
		//Contato ct = consultarContato(idcontato);
		repo.deleteById(idcontato);
	}
	
	public ContatoDTO alterarContato(Long idcontato, Contato contato){
		Contato ct = consultarContato(idcontato);
		BeanUtils.copyProperties(contato,  ct, "id");
//		ct.setEmail(contato.getEmail());
//		ct.setNome(contato.getNome());
//		ct.setFone(contato.getFone());
		return new ContatoDTO(repo.save(ct));
	}
	
	public List<ContatoDTO> consultarContatoPorEmail(String email){
		return ContatoDTO.converteParaDTO(repo.findByEmail(email));
	}
}
