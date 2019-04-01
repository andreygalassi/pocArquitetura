package br.com.arego.core.service;

import java.util.List;
import java.util.Locale;
import java.util.Random;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.github.javafaker.Faker;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;

import br.com.agrego.core.Application;
import br.com.agrego.core.model.Livro;
import br.com.agrego.core.service.LivroService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=Application.class)
public class LivroServiceTest {

	@Autowired
	private LivroService livroService;
	private Faker faker = new Faker(new Random(2));

//	@Test
	public void teste() throws Exception {
		
		Livro livro = new Livro();
		livro.setTitulo(faker.gameOfThrones().character());
		livroService.getRepo().save(livro);
		List<Livro> findAll = livroService.getRepo().findAll();
		System.out.println(findAll);
	}
	
	@Test
	public void testeFake() {
		Faker faker1 = new Faker(new Random(2));
		Faker faker2 = new Faker(new Random(2));
		Faker faker3 = new Faker(new Random(2));
		
		System.out.println(faker1.gameOfThrones().character());
		System.out.println(faker1.gameOfThrones().character());
		System.out.println(faker3.gameOfThrones().character());
	}
}