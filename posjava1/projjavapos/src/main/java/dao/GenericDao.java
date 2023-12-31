package dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import projjavapos.HibernateUtil;



public class GenericDao<E> {
	private EntityManager entityManager = HibernateUtil.geEntityManager();

	public void salvar(E entidade) {
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		entityManager.persist(entidade);
		transaction.commit();
	}

	public E pesquisar(Long id, Class<E> entidade) {
		entityManager.clear();
		@SuppressWarnings("unchecked")
		E singleResult = (E) entityManager.createQuery("from " + entidade.getSimpleName() + " where id = " + id ).getSingleResult();
		E e = singleResult;
		return e;
	}
	
	public E pesquisar2(E entidade) {
		Object id = HibernateUtil.getPrimaryKey(entidade);
		E e = (E) entityManager.find(entidade.getClass(), id);
		return e;
	}

	public E updateMarge(E entidade) {
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		E entidadeSalva = entityManager.merge(entidade);
		transaction.commit();

		return entidadeSalva;
	}

	public void delete(E entidade) throws Exception{
		Object id = HibernateUtil.getPrimaryKey(entidade);

		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();

		entityManager.createNativeQuery(
				"delete from " + entidade.getClass().getSimpleName().toLowerCase() + " where id = " + id).executeUpdate();
		transaction.commit();

	}

	public List<E> findAll(Class<E> entidade){
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		
		List<E> lista = entityManager.createQuery("from " + entidade.getName() + " order by id").getResultList();
		
		transaction.commit();
		
		return lista;
		
	}
	
	public EntityManager getEntityManager() {
		return entityManager;
	}
	
}
