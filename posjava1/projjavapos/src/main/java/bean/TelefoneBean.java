package bean;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import dao.PessoaDao;
import dao.TelefoneDao;
import model.Pessoa;
import model.TelefonePessoa;

@ManagedBean(name = "telefoneBean")
@ViewScoped
public class TelefoneBean {
	private Pessoa pessoa = new Pessoa();
	private PessoaDao<Pessoa> pessoaDao = new PessoaDao<Pessoa>();
	private TelefoneDao<TelefonePessoa> telefoneDao = new TelefoneDao<TelefonePessoa>();
	private TelefonePessoa telefone = new TelefonePessoa();
	
	@PostConstruct
	public void init() {
		String idPessoa = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("idPessoa");
		pessoa = pessoaDao.pesquisar(Long.parseLong(idPessoa), Pessoa.class);
		
	}
	
	public String salvar() {
		telefone.setPessoa(pessoa);
		telefoneDao.salvar(telefone);
		telefone = new TelefonePessoa();
		pessoa = pessoaDao.pesquisar(pessoa.getId(), Pessoa.class);
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Informação", "Salvo com sucesso!"));
		return "";
	}
	
	public String novo() {
		telefone = new TelefonePessoa();
		return "";		
	}
	
	public String removerTelefone() throws Exception {
		telefoneDao.delete(telefone);
		pessoa = pessoaDao.pesquisar(pessoa.getId(), Pessoa.class);
		telefone = new TelefonePessoa();
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Informação", "Salvo com sucesso!"));


		return "";
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public TelefoneDao<TelefonePessoa> getTelefoneDao() {
		return telefoneDao;
	}

	public void setTelefoneDao(TelefoneDao<TelefonePessoa> telefoneDao) {
		this.telefoneDao = telefoneDao;
	}

	public TelefonePessoa getTelefone() {
		return telefone;
	}

	public void setTelefone(TelefonePessoa telefone) {
		this.telefone = telefone;
	}
}
