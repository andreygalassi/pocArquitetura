package br.com.agrego.api.v1;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.agrego.core.model.Livro;

@RestController
@RequestMapping("/livros")
public class LivroApi {

	@GetMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public Livro findById(@PathVariable("id") Long id) {
		Livro l = new Livro();
		l.setTitulo("teste");
		return l;
	}

	@GetMapping()
	@ResponseStatus(HttpStatus.OK)
	public String findAll() {
		return "Teste";
	}
}
