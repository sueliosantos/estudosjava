package dao;

import model.Pessoa;

public class PessoaDao<E> extends GenericDao<Pessoa>{
	public void removerPessoa(Pessoa p) throws Exception {
		getEntityManager().getTransaction().begin();
		String sqlDeleteTelefone = "delete from telefonepessoa where pessoa_id = " + p.getId();
		getEntityManager().createNativeQuery(sqlDeleteTelefone).executeUpdate();
		getEntityManager().getTransaction().commit();
		
		super.delete(p);
	}

}