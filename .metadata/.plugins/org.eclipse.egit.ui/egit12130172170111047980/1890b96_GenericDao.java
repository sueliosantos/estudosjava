package br.com.esig.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.hibernate.Hibernate;

import antlr.collections.List;
import br.com.esig.util.HibernateUtil;
import jakarta.transaction.Transaction;

public class GenericDao<E> {
	private EntityManager entityManager = HibernateUtil.getEntityManager();

	public void salvar(E entidade) {
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		entityManager.persist(entidade);
		transaction.commit();
	}

	public E pesquisar(E entidade) {
		Object id = HibernateUtil.getPrimaryKey(entidade);
		E e = (E) entityManager.find(entidade.getClass(), id);

		return e;
	}

	public E updateMarge(E entidade) {
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		E endidadeModificada = entityManager.merge(entidade);
		transaction.commit();

		return endidadeModificada;
	}

	public void delete(E endidade) throws Exception{
		Object id = HibernateUtil.getPrimaryKey(endidade);

		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();

		entityManager.createNativeQuery("delete from " + 
				endidade.getClass().getSimpleName().toLowerCase() + " where id =" + id).executeUpdate();
		
		transaction.commit();
	}
	
	public java.util.List<E> findAll(Class<E> entidade){
		EntityTransaction transction = entityManager.getTransaction();
		transction.begin();
		
		java.util.List<E> lista = entityManager.createQuery("from " + entidade.getName() + " order by id desc").getResultList();
		
		transction.commit();
		
		return lista;
	}
	
	public EntityManager getEntityManager() {
		return entityManager;
	}

}
