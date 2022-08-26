package com.aulas.services;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aulas.entidades.Pessoa;
import com.aulas.repository.PessoaRepository;

@Service
public class PessoaService {
	@Autowired
	PessoaRepository repository;
	
	public Pessoa consultar(Long idpessoa) {
		Optional<Pessoa> obj = repository.findById(idpessoa);
		return obj.orElseThrow(() -> new EntityNotFoundException("Pessoa não encontrada")); 
	}
	
	public Pessoa salvar(Pessoa pessoa) {
		return repository.save(pessoa);
	}
	
	public Pessoa alterar(Long idpessoa, Pessoa pessoa) {
		Optional<Pessoa> obj = repository.findById(idpessoa);
		Pessoa p = obj.orElseThrow(()-> new EntityNotFoundException("Pessoa não encontrada"));
		p.setNome(pessoa.getNome());
		return repository.save(p);		
	}
	
	public List<Pessoa> buscarTodos() {
		return repository.findAll();
	}
}
