package bean;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import dao.GenericDao;
import model.Pessoa;

@ManagedBean(name = "pessoaBean")
@ViewScoped
public class PessoaBean {
	private Pessoa pessoa = new Pessoa();
	private GenericDao<Pessoa> dao = new GenericDao<>();
	private List<Pessoa> list = new ArrayList<Pessoa>();
	
	public Pessoa getPessoa() {
		return pessoa;
	}
	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}
	public GenericDao<Pessoa> getDao() {
		return dao;
	}
	public void setDao(GenericDao<Pessoa> dao) {
		this.dao = dao;
	}
	
	public String salvar() {
		dao.salvar(pessoa);
		return "";
	}
	
	public String novo() {
		pessoa = new Pessoa();
		return "";		
	}
	public List<Pessoa> getList() {
		list = dao.findAll(Pessoa.class);
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Informação", "Salvo com sucesso!"));
		return list;
	}
	
	
	public String remover() {
		dao.delete(pessoa);
		pessoa = new Pessoa();
		return "";
	}
	

}
