package dao;

import java.util.List;

import javax.persistence.Query;

import model.Pessoa;

public class PessoaDao<E> extends GenericDao<Pessoa>{
	public void removerPessoa(Pessoa p) throws Exception {
		getEntityManager().getTransaction().begin();
		String sqlDeleteTelefone = "delete from telefonepessoa where pessoa_id = " + p.getId();
		getEntityManager().createNativeQuery(sqlDeleteTelefone).executeUpdate();
		
		String sqlDeleteEmails = "delete from emailpessoa where pessoa_id = " + p.getId();
		getEntityManager().createNativeQuery(sqlDeleteEmails).executeUpdate();
		
		getEntityManager().getTransaction().commit();
		
		super.delete(p);
	}

	public List<Pessoa> pesquisarPorNome(String nomeFiltroPesquisa) {
		Query q = super.getEntityManager().createQuery("from Pessoa where nome like '%"+nomeFiltroPesquisa+"%'" );
		
		return q.getResultList();

	}

}
