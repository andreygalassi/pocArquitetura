package br.com.agrego.core.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.com.agrego.core.model.Livro;

@Repository
public class LivroRepo extends AbstractJpaDAO<Livro, Long>{
	
}
