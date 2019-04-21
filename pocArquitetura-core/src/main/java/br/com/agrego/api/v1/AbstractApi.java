package br.com.agrego.api.v1;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import br.com.agrego.core.service.AbstractService;


public abstract class AbstractApi<E extends Serializable, S extends AbstractService<E, ?>> {


	@Autowired
	private S service;
	
	public S getService() {
		return service;
	}
	
	@GetMapping()
	@ResponseStatus(HttpStatus.OK)
	public List<E> findAll() {
		return service.getRepo().findAll();
	}
}
