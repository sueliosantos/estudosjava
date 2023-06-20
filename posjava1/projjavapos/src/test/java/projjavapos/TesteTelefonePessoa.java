package projjavapos;

import org.junit.Test;

import dao.GenericDao;
import model.Pessoa;
import model.TelefonePessoa;

public class TesteTelefonePessoa {
	@Test
	public void testeSalvarPessoa() {
		GenericDao<Pessoa> pdao = new GenericDao<>();
		GenericDao<TelefonePessoa> dao = new GenericDao<>();
		Pessoa p = pdao.pesquisar(2L, Pessoa.class);
		TelefonePessoa t = new TelefonePessoa();
		t.setPessoa(p);
		t.setNumero("849961741505");
		t.setTipo("celular");
		
		dao.salvar(t);
		
	}
}
