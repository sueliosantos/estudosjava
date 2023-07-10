package projjavapos;

import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import dao.GenericDao;
import model.Pessoa;

public class TestePessoa {
	@Test
	public void testeSalvarPessoa() {
		GenericDao<Pessoa> dao = new GenericDao<>();

		Pessoa p = new Pessoa();
		p.setNome("Suelio");
		p.setLogin("suelio");
		p.setSobrenome("santos");

		dao.salvar(p);
	}
	
	@Test
	public void testeSalvarPessoa2() {
		GenericDao<Pessoa> dao = new GenericDao<>();

		Pessoa p = new Pessoa();
		p.setNome("Claudiana");
		p.setLogin("clau");
		p.setSobrenome("clau");

		dao.salvar(p);
	}

	@Test
	public void testeGetById() {
		GenericDao<Pessoa> dao = new GenericDao<>();

		Pessoa p = dao.pesquisar(10L, Pessoa.class);
		
		System.out.println(p);

	}
	
	
	@Test
	public void testeBuscar() {
		GenericDao<Pessoa> dao = new GenericDao<>();
		Pessoa p = new Pessoa();
		p.setId(10L);

		p = dao.pesquisar2(p);
		
		System.out.println(p);

	}
	
	@Test
	public void testeUpdate() {
		GenericDao<Pessoa> dao = new GenericDao<>();

		Pessoa p = dao.pesquisar(10L, Pessoa.class);
		
		p.setNome("Giovanna");
		p.setLogin("gigi");
		p.setSobrenome("massa");		
		p = dao.updateMarge(p);
		
		System.out.println(p);

	}
	
	@Test
	public void testeDelete() {
		GenericDao<Pessoa> dao = new GenericDao<>();

		Pessoa p = dao.pesquisar(16L, Pessoa.class);
		
		try {
			dao.delete(p);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	@Test
	public void testeGetAll() {
		GenericDao<Pessoa> dao = new GenericDao<>();

		List<Pessoa> list = dao.findAll(Pessoa.class);
		
		for (Pessoa pessoa : list) {
			System.out.println(pessoa.getNome() + " - " + pessoa.getLogin());
			System.out.println("----");
		}
	}
	
	@Test
	public void testeQueryList() {
		GenericDao<Pessoa> dao = new GenericDao<>();
		List<Pessoa> list = dao.getEntityManager().createQuery(" from Pessoa").getResultList();
		
		for (Pessoa pessoa : list) {
			System.out.println(pessoa.getNome());
			System.out.println("----");
			
		}
	}


}
