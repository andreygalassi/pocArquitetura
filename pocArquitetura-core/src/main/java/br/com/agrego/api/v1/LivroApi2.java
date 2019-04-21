package br.com.agrego.api.v1;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.agrego.core.model.Livro;
import br.com.agrego.core.repository.LivroRepo;
import br.com.agrego.core.service.AbstractService;

@RestController
@RequestMapping("/livros2")
public class LivroApi2 extends AbstractApi<Livro, AbstractService<Livro,LivroRepo>>{

	
}
