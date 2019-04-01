package br.com.agrego.core.repository;

import org.springframework.stereotype.Repository;

import br.com.agrego.core.model.Usuario;

@Repository
public class UsuarioRepo extends AbstractJpaDAO<Usuario, Long>{

}
