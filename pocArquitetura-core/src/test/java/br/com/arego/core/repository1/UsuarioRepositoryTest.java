package br.com.arego.core.repository1;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.test.context.junit4.SpringRunner;

import com.github.javafaker.Faker;

import br.com.agrego.core.Application;
import br.com.agrego.core.model.Usuario;
import br.com.agrego.core.repository1.UsuarioRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=Application.class)
public class UsuarioRepositoryTest {

	@Autowired
	private UsuarioRepository usuarioRepository;
	private Faker faker = new Faker();
	
	@Test
	public void teste() throws Exception {
		deveInserirNovoUsuario();
		deveInserirNovoUsuario();
		deveInserirNovoUsuario();
		
//		devePesquisarPorExemplo();
	}

	@Test
	public void deveInserirNovoUsuario() throws Exception {
		String nome = faker.gameOfThrones().character() + "@" + faker.gameOfThrones().dragon();
		nome = nome.replace(" ", "_");
		Usuario usuario = new Usuario();
		usuario.setPassword("123");
		usuario.setUsername(nome);
		
		Usuario save = usuarioRepository.save(usuario);
		
		System.out.println(save);
		
	}
	
	@Test
	public void devePesquisarPorExemplo() {

		Usuario usuario = new Usuario();
//		usuario.setId(1l);
		usuario.setUsername("Poetess%");
		Example<Usuario> example = Example.of(usuario, ExampleMatcher.matchingAny());
		
		List<Usuario> findAll = usuarioRepository.findAll(example);
		
		System.out.println(findAll);
		
	}
	
	
}
