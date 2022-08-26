package com.aulas.service;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.aulas.entidades.Contato;
import com.aulas.repository.ContatoRepository;
import com.aulas.services.ContatoService;
import com.aulas.services.dto.ContatoDTO;

@ExtendWith(SpringExtension.class)
public class ContatoServiceTestes {
	private Long idExistente;
	private Long idNaoExistente;
	private Contato contato;
	private Contato contatoInvalido;
	
	@BeforeEach
	void setup() {
		idExistente = 1l;
		idNaoExistente = 1000l;
		contato = new Contato("maria", "maria@gmail.com", "(51) 994773333");
		contatoInvalido = new Contato("maria", "mariagmail.com", "(51) 994773333");
		
		Mockito.when(repository.save(contato)).thenReturn(contato);
		Mockito.doThrow(IllegalArgumentException.class).when(repository).save(contatoInvalido);
		
		Mockito.doNothing().when(repository).deleteById(idExistente);
		Mockito.doThrow(EntityNotFoundException.class).when(repository).deleteById(idNaoExistente);
		
		Mockito.when(repository.findById(idExistente)).thenReturn(Optional.of(contato));
		Mockito.doThrow(EntityNotFoundException.class).when(repository).findById(idNaoExistente);

	}
	
	@InjectMocks
	ContatoService service;
	
	@Mock
	ContatoRepository repository;
	
	@Test
	public void retornaContatoDTOQuandoSalvarComSucesso() {
		ContatoDTO contatoDTO = service.salvar(contato);
		Assertions.assertNotNull(contatoDTO);
		Mockito.verify(repository).save(contato);
	}
	
	@Test
	public void retornaExcecaoQuandoSalvaContatoSemSucesso() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> service.salvar(contatoInvalido)); 
		Mockito.verify(repository).save(contatoInvalido);
	}
	
	@Test
	public void retornaNadaAoExcluirComIdExistente(){
		Assertions.assertDoesNotThrow(()->{
			service.excluirContato(idExistente);
		});
		Mockito.verify(repository, Mockito.times(1)).deleteById(idExistente);
	}
	
	@Test
	public void lancaEntidadeNaoEntradaAoExcluirIdNaoExistente() {
		Assertions.assertThrows(EntityNotFoundException.class, () -> {
			service.excluirContato(idNaoExistente);
		});
		Mockito.verify(repository, Mockito.times(1)).deleteById(idNaoExistente);
	}
	
	@Test
	public void retornaContatoQuandoPesquisaPorIdExistente() {
		ContatoDTO contato = service.consultarContatoPorId(idExistente);
		Assertions.assertNotNull(contato);
		Mockito.verify(repository).findById(idExistente);
	}
	@Test
	public void lancaEntidadeNaoEntradaAoConsultarIdNaoExistente() {
		Assertions.assertThrows(EntityNotFoundException.class, () -> {
			service.consultarContatoPorId(idNaoExistente);
		});
		Mockito.verify(repository, Mockito.times(1)).findById(idNaoExistente);
	}	
	
}
