package br.com.agrego.core.util;

import javax.persistence.Query;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class PaginacaoUtil {
	public void preparar(Query query, Pageable pageable) {
		if (!Pageable.unpaged().equals(pageable)) {
			int paginaAtual = pageable.getPageNumber();
			int totalRegistrosPorPagina = pageable.getPageSize();
			int primeiroRegistro = paginaAtual * totalRegistrosPorPagina;
			
			query.setFirstResult(primeiroRegistro);
			query.setMaxResults(totalRegistrosPorPagina);
		}
	}

	public void preparar(Criteria criteria, Pageable pageable) {
		if (!Pageable.unpaged().equals(pageable)) {
			int paginaAtual = pageable.getPageNumber();
			int totalRegistrosPorPagina = pageable.getPageSize();
			int primeiroRegistro = paginaAtual * totalRegistrosPorPagina;
			
			criteria.setFirstResult(primeiroRegistro);
			criteria.setMaxResults(totalRegistrosPorPagina);
	
			Sort sort = pageable.getSort();
			if (sort != null) {
				Sort.Order order = sort.iterator().next();
				String property = order.getProperty();
				criteria.addOrder(order.isAscending() ? Order.asc(property) : Order.desc(property));
			}
		}
	}
}