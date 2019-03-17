package br.com.arego.core.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.github.javafaker.Faker;

import br.com.agrego.core.Application;
import br.com.agrego.core.model.Usuario;
import br.com.agrego.core.repository.UsuarioRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=Application.class)
public class UsuarioRepositoryTest {

	@Autowired
	private UsuarioRepository usuarioRepository;
	private Faker faker = new Faker();
	
	@Test
	public void deveInserirNovoUsuario() throws Exception {
		String nome = faker.gameOfThrones().character() + "@" + faker.gameOfThrones().dragon();
		nome = nome.replace(" ", "_");
		Usuario usuario = new Usuario();
		usuario.setNome(nome);
		usuario.setPassword("123");
		usuario.setUsername(nome);
		
		Usuario save = usuarioRepository.save(usuario);
		
		System.out.println(save);
	}
	
	
}
