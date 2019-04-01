package br.com.agrego.core.service;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.agrego.core.repository.AbstractJpaDAO;

public abstract class AbstractService<E extends Serializable, RR extends AbstractJpaDAO<E, ?>>  {

	@Autowired
	private RR repo;
	
	public RR getRepo() {
		return repo;
	}
}
