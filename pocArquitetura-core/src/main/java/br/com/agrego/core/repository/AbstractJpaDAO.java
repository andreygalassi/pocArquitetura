package br.com.agrego.core.repository;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.Parameter;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.Table;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;

import br.com.agrego.core.model.EnumAcao;
import br.com.agrego.core.model.EnumParametro;
import br.com.agrego.core.model.Historico;
import br.com.agrego.core.util.MyBeanUtil;
import br.com.agrego.core.util.MyLog;
import br.com.agrego.core.util.PaginacaoUtil;
/**
 * Classe abstrata auxiliar a criacao de repository
 * Foi usado o nome dos metodos utilizados na interface JpaRepository
 * Os métodos referente a utilização do histórico tentam converter o ID em Long para gravação na tabela de histórico. 
 * Caso não seja possível, é retornado uma exception
 * Essa ainda é uma limitação da utilização da tabela de historico
 * 
 * @author Andrey
 * @since 2019-01-22
 * @param <T>
 */
public abstract class AbstractJpaDAO<T extends Serializable, ID extends Serializable> {

	private static final Logger LOGGER = LogManager.getLogger(AbstractJpaDAO.class);

	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private PaginacaoUtil paginacaoUtil;

	private Class<T> beanClass;
	private Class<ID> idClass;
	private ID id;

	private String chaveLog = null;

	private Gson gson;
	
	private String nomeCampoId = null;

	private Class<T> getBeanClass() {
		if (this.beanClass == null) {
			this.beanClass = getGenericTypeArgument(this.getClass(), 0);
		}
		return this.beanClass;
	}
	private Class<ID> getIdClass() {
		if (this.idClass == null) {
			this.idClass = getIdGenericTypeArgument(this.getClass(), 0);
		}
		return this.idClass;
	}
	private String getNomeCampoId() {
		if (this.nomeCampoId == null) {
			for (Field field : getBeanClass().getDeclaredFields()) {
				javax.persistence.Id persistenceId = field.getAnnotation(javax.persistence.Id.class);
				org.springframework.data.annotation.Id springId = field
						.getAnnotation(org.springframework.data.annotation.Id.class);
				if (persistenceId != null || springId != null) {
					this.nomeCampoId = StringUtils.capitalize(field.toString());
				}
			}
		}
		return this.nomeCampoId;
	}
	

	@SuppressWarnings("unchecked")
	public static <T> Class<T> getGenericTypeArgument(final Class<?> clazz, final int idx) {
		final Type type = clazz.getGenericSuperclass();

		ParameterizedType paramType;
		try {
			paramType = (ParameterizedType) type;
		} catch (ClassCastException cause) {
			paramType = (ParameterizedType) ((Class<T>) type).getGenericSuperclass();
		}

		return (Class<T>) paramType.getActualTypeArguments()[idx];
	}

	@SuppressWarnings("unchecked")
	public static <ID> Class<ID> getIdGenericTypeArgument(final Class<?> clazz, final int idx) {
		final Type type = clazz.getGenericSuperclass();

		ParameterizedType paramType;
		try {
			paramType = (ParameterizedType) type;
		} catch (ClassCastException cause) {
			paramType = (ParameterizedType) ((Class<ID>) type).getGenericSuperclass();
		}

		return (Class<ID>) paramType.getActualTypeArguments()[idx];
	}

	public AbstractJpaDAO() {
		this.beanClass = getBeanClass();
		
		MyLog myLog = this.beanClass.getAnnotation(MyLog.class);
		if (myLog != null) {
			chaveLog = myLog.chave();
		}
	}

	public T findOne(ID id) {
		return entityManager.find(getBeanClass(), id);
	}
	
	//TODO implementar a pesquisa por lista de IDS. talvez seja necessario utilizar criteria
//	public Page<T> findAllById(Pageable pageable, Collection<ID> ids) {
//		
//		return entityManager.find(getBeanClass(), id);
//	}

	@SuppressWarnings("unchecked")
	public List<T> findAll() {
		List<T> resultList = entityManager.createQuery("from " + getBeanClass().getSimpleName()).getResultList();
		return resultList;
	}

	public Page<T> findAll(Pageable pageable) {
		StringBuilder sb = new StringBuilder();

		sb.append(" from ").append(getBeanClass().getSimpleName()).append(" this ");

		Long resultTotal = entityManager.createQuery("select count(this) " + sb.toString(), Long.class).getSingleResult();

		sb.append(pageableToOrderBy(pageable));
		
		TypedQuery<T> queryList = entityManager.createQuery(sb.toString(), getBeanClass());

		Page<T> resultado = preparaPage(pageable, resultTotal, queryList); 

		return resultado;
	}
	
	/**
	 * Metodo auxiliar para retornar o order by como string a partir do Pageable
	 * @param pageable
	 * @return
	 */
	private String pageableToOrderBy(Pageable pageable) {
		if (pageable == null || pageable.getSort()==null || pageable.getSort().toString().length()==0) return "";
		
		return " order by " + pageable.getSort().toString().replaceAll(":", "");
	}

	/**
	 * Ajuda a montar a Page de acordo com os campos
	 * @param pageable
	 * @param resultTotal
	 * @param queryList
	 * @return
	 */
	public Page<T> preparaPage(Pageable pageable, Long resultTotal, TypedQuery<T> queryList) {
		return new PageImpl<T>(queryList.getResultList(),pageable,resultTotal);
	}

	/**
	 * Ex:
	 * this.item1, this,item2, this.item3
	 * @param orderBy 
	 * @return
	 */
	//TODO modificar para receber o Sort
	@SuppressWarnings("unchecked")
	public List<T> findAll(String orderBy) {
		List<T> resultList = entityManager
				.createQuery("from " + getBeanClass().getSimpleName() + " this order by " + orderBy).getResultList();
		return resultList;
	}

	//TODO modificar para receber o Sort
	public Page<T> findAll(Pageable pageable, String orderBy) {
		StringBuilder sb = new StringBuilder();
		sb.append("from " + getBeanClass().getSimpleName() + " this ");

		Long resultTotal = entityManager.createQuery("select count(this) " + sb.toString(), Long.class).getSingleResult();
		TypedQuery<T> queryList = entityManager.createQuery(sb.toString() + " this order by " + orderBy, getBeanClass());

//		paginacaoUtil.preparar(queryList, pageable);

//		Resultado<T> resultado = new Resultado<>(resultTotal, queryList.getResultList());
		Page<T> resultado = preparaPage(pageable, resultTotal, queryList); 

		return resultado;
	}

	//TODO tentar melhorar esse metodo, pois n pesquisa com like em algumas situações
	public List<T> findByExample(Pageable pageable, T example) throws IllegalArgumentException, IllegalAccessException,
			InvocationTargetException, SecurityException, NoSuchMethodException {
		TypedQuery<T> typedQuery = createCriteriaByExample(example);
		
		paginacaoUtil.preparar(typedQuery, pageable);

		return typedQuery.getResultList();
	}
	public List<T> findByExample(T example) throws IllegalArgumentException, IllegalAccessException,
			InvocationTargetException, SecurityException, NoSuchMethodException {
		TypedQuery<T> typedQuery = createCriteriaByExample(example);
		
		return typedQuery.getResultList();
	}
	/**
	 * Método para facilitar a busca por objeto com base nos campos preenchidos do próprio objeto passado.
	 * São ignorados os campos null na busca.
	 * Caso o campo seja string, a pesquisa NÃO considera como like (igualdade parcial do campo), e sim como igualdade todal.
	 * @param example
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	private TypedQuery<T> createCriteriaByExample(T example) throws IllegalAccessException, InvocationTargetException {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<T> cq = cb.createQuery(getBeanClass());
		Root<T> r = cq.from(getBeanClass());
		Predicate p = cb.conjunction();
		Metamodel mm = entityManager.getMetamodel();
		EntityType<T> et = mm.entity(getBeanClass());
		Set<Attribute<? super T, ?>> attrs = et.getAttributes();
		for (Attribute<? super T, ?> a : attrs) {
			String name = a.getName();
			String javaName = a.getJavaMember().getName();
			String getter = "get" + javaName.substring(0, 1).toUpperCase() + javaName.substring(1);
			//FIXME incluido o try para evitar erro ao procurar o get inexistente de um field (ex: codMeioDisponibilizacao)
			//TODO implementar o like caso seja uma string
			try {
				Method m = getBeanClass().getMethod(getter, (Class<?>[]) null);
				if (m.invoke(example, (Object[]) null) != null 
						&& StringUtils.isNotBlank(m.invoke(example, (Object[]) null).toString())) {
					p = cb.and(p, cb.equal(r.get(name), m.invoke(example, (Object[]) null)));
				}
			} catch (NoSuchMethodException e) { }
		}
		cq.select(r).where(p);
		TypedQuery<T> typedQuery = entityManager.createQuery(cq);
		return typedQuery;
	}
	
	/**
	 * Esse método não salva o usuário logado no histórico de alteração e sim o usuário padrão
	 */
	@Transactional
	public T saveOrUpdate(T entity) {
		return saveOrUpdate(entity, EnumParametro.USUARIO_DEFAULT.toString());
	}
	/**
	 * @param usuario usuário que será gravado na tabela de histórico
	 */
	@Transactional
	public T saveOrUpdate(T entity, String usuario) {
		T retorno = null;
		ID id = null;
		try {
			id = getId(entity);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			LOGGER.error("Erro ao tentar capturar o id do objeto", e);
			throw new RuntimeException("Erro ao tentar capturar o id do objeto", e);
		}
		if (id == null) {
			retorno = save(entity, usuario);
		} else {
			retorno = update(entity, usuario);
		}
		return retorno;
	}

	@SuppressWarnings("unchecked")
	private ID getId(T entity) throws NoSuchMethodException, SecurityException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		if (this.id == null) {
			Method method = getBeanClass().getMethod(getNomeCampoId(), new Class[] {});
			return (ID) method.invoke(entity, new Object[] {});
		}
		return this.id;
	}

	/**
	 * Esse método não salva o usuário logado no histórico de alteração e sim o usuário padrão
	 */
	@Transactional
	public T save(T entity) {
		return save(entity, EnumParametro.USUARIO_DEFAULT.toString());
	}
	/**
	 * @param usuario usuário que será gravado na tabela de histórico
	 */
	@Transactional
	public T save(T entity, String usuario) {
		entityManager.persist(entity);
		gravarLog(entity, EnumAcao.CREATE, usuario);
		return entity;
	}

	/**
	 * Esse método não salva o usuário logado no histórico de alteração e sim o usuário padrão
	 */
	public T saveAndFlush(T entity) {
		return saveAndFlush(entity, EnumParametro.USUARIO_DEFAULT.toString());
	}
	/**
	 * @param usuario usuário que será gravado na tabela de histórico
	 */
	public T saveAndFlush(T entity, String usuario) {
		entityManager.persist(entity);
		entityManager.flush();
		gravarLog(entity, EnumAcao.CREATE, usuario);
		return entity;
	}

	/**
	 * Esse método não salva o usuário logado no histórico de alteração e sim o usuário padrão
	 */
	@Transactional
	public T update(T entity) {
		return update(entity, EnumParametro.USUARIO_DEFAULT.toString());
	}
	/**
	 * @param usuario usuário que será gravado na tabela de histórico
	 */
	@Transactional
	public T update(T entity, String usuario) {
		T merge = entityManager.merge(entity);
		gravarLog(merge, EnumAcao.UPDATE, usuario);
		return merge;
	}

	/**
	 * Esse método não salva o usuário logado no histórico de alteração e sim o usuário padrão
	 */
	@Transactional
	public T updatePartial(ID id, T update) {
		return updatePartial(id, update, EnumParametro.USUARIO_DEFAULT.toString());
	}
	/**
	 * Atualiza o registro com ID selecionado com os valores passado pela entidade UPDATE ignorando os valores null
	 * @param ID da entidade
	 * @param UPDATE entidade
	 * @param usuario usuário que será gravado na tabela de histórico
	 */
	@Transactional
	public T updatePartial(ID id, T update, String usuario) {
		T entity = findOne(id);

		MyBeanUtil.myCopyProperties(update, entity);
		return update(entity, usuario);
	}

	/**
	 * Esse método não salva o usuário logado no histórico de alteração e sim o usuário padrão
	 */
	@Transactional
	public void deleteByEntity(T entity) {
		deleteByEntity(entity, EnumParametro.USUARIO_DEFAULT.toString());
	}
	/**
	 * @param usuario usuário que será gravado na tabela de histórico
	 */
	@Transactional
	public void deleteByEntity(T entity, String usuario) {
		gravarLog(entity, EnumAcao.DELETE, usuario);
		entityManager.remove(entity);
	}

	/**
	 * Esse método não salva o usuário logado no histórico de alteração e sim o usuário padrão
	 */
	@Transactional
	public void deleteById(ID id) {
		deleteById(id, EnumParametro.USUARIO_DEFAULT.toString());
	}
	/**
	 * @param usuario usuário que será gravado na tabela de histórico
	 */
	@Transactional
	public void deleteById(ID id, String usuario) {
		T entity = findOne(id);
		deleteByEntity(entity, usuario);
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	protected Query createQuery(final String ql) {
		return getEntityManager().createQuery(ql);
	}

	protected TypedQuery<T> createTypedQuery(final String ql) {
		TypedQuery<T> createQuery = getEntityManager().createQuery(ql, beanClass);
		return createQuery;
	}

	protected PaginacaoUtil getPaginacaoUtil() {
		return paginacaoUtil;
	}

	/**
	 * Método utilizado para o fechamento das conexões quando é necessário utilizar uma conexão manual para uso do recordSet.
	 * Ideal para utilização de conexões que utilizando pkg
	 * @param connection
	 * @param preparedStatement
	 * @param resultSet
	 */
	protected void fecharConexoes(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet) {
		if (resultSet != null) {
			try {
				resultSet.close(); 
			} catch (SQLException e) {
				LOGGER.error("Ocorreu o seguinte erro ao fechar o ResultSet : ", e);
			}
		}

		if (preparedStatement != null) {
			try {
				preparedStatement.close();
			} catch (SQLException e) {
				LOGGER.error("Ocorreu o seguinte erro ao fechar o CallableStatement : ", e);
			}
		}

		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				LOGGER.error("Ocorreu o seguinte erro ao fechar o Connection : ", e);
			}
		}
	}

	/**
	 * Método safe para executar a gravação do histórico, porém, se houver erro, 
	 * não deve deixar de executar a ação
	 */
	private void gravarLog(T entity, EnumAcao acao, String usuario) {
		try {

			if (chaveLog!=null) {
				Long valorId = getIdEntidade(entity);
				if (valorId != null) {
					Historico<T> log = new Historico<T>();
					log.setAcao(acao);
					log.setIdReferencia(valorId);
					log.setTabela(chaveLog);
					if (usuario!=null) {
						log.setAgente(usuario);
					}else {
						log.setAgente(EnumParametro.USUARIO_DEFAULT.toString());
					}
					log.setData(new Date());
					String jsonInString = gson.toJson(entity);
					log.setDocumento(jsonInString);
					entityManager.persist(log);
				}
			}else {
				LOGGER.warn("Log não gravado por falta de definição da chave");
			}
		} catch (Exception e) {
			LOGGER.error("Não foi possível gravar o histórico", e);
		}
	}

	private Long getIdEntidade(T entity) {
		for (Field f : this.beanClass.getDeclaredFields()) {
			Id annotation = f.getAnnotation(Id.class);
			if (annotation != null) {
				try {
					Object invoke = new PropertyDescriptor(f.getName(), this.beanClass).getReadMethod().invoke(entity);
					return (Long) invoke;
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
						| IntrospectionException e) {
					LOGGER.warn("Problema ao tentar capturar o valor do ID para gravar o log", e);
				}
			}
		}
		return null;
	}
	/**
	 * Retorna a lista de historicos do item logado
	 * @param idItem id do item logado
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Historico<T>> getHistoricosItem(Long idItem) {
		if (idItem==null) return null;
		
		StringBuilder sb = new StringBuilder();
		sb.append(" from ");
		sb.append(Historico.class.getSimpleName() + " b ");
		sb.append(" where b.tabela = :tabela and idReferencia = :idReferencia ");
		sb.append(" order by id desc ");

		Query typedQuery = entityManager.createQuery(sb.toString());

		for (Parameter<?> p : typedQuery.getParameters()) {
			if (p.getName().equals("tabela"))
				typedQuery.setParameter(p.getName(), this.chaveLog);
			if (p.getName().equals("idReferencia"))
				typedQuery.setParameter(p.getName(), idItem);
		}

		return typedQuery.getResultList();
	}
	
	/**
	 * Retorna o log especificado
	 * @param idHistorico id do histórico de log
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Historico<T> getHistorico(Long idHistorico) {
		if (idHistorico==null) return null;
		
		StringBuilder sb = new StringBuilder();
		sb.append(" from ");
		sb.append(Historico.class.getSimpleName() + " b ");
		sb.append(" where id = :idHistorico and b.tabela = :tabela ");
		sb.append(" order by id desc ");

		Query typedQuery = entityManager.createQuery(sb.toString());

		for (Parameter<?> p : typedQuery.getParameters()) {
			if (p.getName().equals("tabela"))
				typedQuery.setParameter(p.getName(), this.chaveLog);
			if (p.getName().equals("idHistorico"))
				typedQuery.setParameter(p.getName(), idHistorico);
		}
		
		if (typedQuery.getResultList().iterator().hasNext()) {
			return (Historico<T>) typedQuery.getResultList().iterator().next();
		}

		return null;
	}
}

