package br.com.arego.core.repository2;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.context.junit4.SpringRunner;

import com.github.javafaker.Faker;

import br.com.agrego.core.Application;
import br.com.agrego.core.model.Usuario;
import br.com.agrego.core.repository.AbstractJpaDAO;
import br.com.agrego.core.repository.UsuarioRepo;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=Application.class)
public class UsuarioRepoTest {

	private static final Logger LOGGER = LogManager.getLogger(AbstractJpaDAO.class);
	
	@Autowired
	private UsuarioRepo usuarioRepo;
	private Faker faker = new Faker();
	
	@Test
	public void deveTestarLogger() {
		LOGGER.info("Teste de log");
	}
	
//	@Test
	public void deveTestarPesquisaPaginada() {
		PageRequest of = PageRequest.of(1, 2);
		Page<Usuario> findAll = usuarioRepo.findAll(of);
		
		System.out.println(findAll.getContent());
		
		Sort sort = new Sort(Direction.ASC, "id");
		System.out.println(sort.toString());
		System.out.println(sort.getOrderFor("id"));
		System.out.println(sort.getOrderFor("id").getProperty());
	}

	@Test
	public void deveTestarPesquisaPaginadaEOrdenada() {
		Sort sort = new Sort(Direction.DESC, "id");
		PageRequest of = PageRequest.of(1, 2, Direction.DESC, "nome", "username");
		Page<Usuario> findAll = usuarioRepo.findAll(of);
		
		System.out.println(findAll.getContent());
		
		System.out.println(sort.toString());
		System.out.println(sort.getOrderFor("id"));
		System.out.println(sort.getOrderFor("id").getProperty());
	}
}
