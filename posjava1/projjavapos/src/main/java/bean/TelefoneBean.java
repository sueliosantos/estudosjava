package bean;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import dao.PessoaDao;
import model.Pessoa;

@ManagedBean(name = "telefoneBean")
@ViewScoped
public class TelefoneBean {
	private Pessoa pessoa = new Pessoa();
	private PessoaDao<Pessoa> pessoaDao = new PessoaDao<Pessoa>();
	
	@PostConstruct
	public void init() {
		String idPessoa = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("idPessoa");
		pessoa = pessoaDao.pesquisar(Long.parseLong(idPessoa), Pessoa.class);
		
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}
}
