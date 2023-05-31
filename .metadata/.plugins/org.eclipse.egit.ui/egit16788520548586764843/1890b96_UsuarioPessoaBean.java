package br.com.managedBean;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;

import br.com.esig.dao.GenericDao;
import br.com.esig.model.UsuarioPessoa;

@ManagedBean(name = "usuarioPessoaBean")
@ViewScoped
public class UsuarioPessoaBean {
	private UsuarioPessoa usuario = new UsuarioPessoa();
	private GenericDao<UsuarioPessoa> dao = new GenericDao<>();
	private List<UsuarioPessoa> list = new ArrayList<UsuarioPessoa>();
	
	@PostConstruct
	public void init() {
		list  = dao.findAll(UsuarioPessoa.class);
		
	}
	public UsuarioPessoa getUsuario() {
		return usuario;
	}
	public void setUsuario(UsuarioPessoa usuario) {
		this.usuario = usuario;
	}
	
	public String salvar() {
		dao.salvar(usuario);
		list.add(usuario);
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Informação:", "Salvo com sucesso!"));
		return "";
	}
	
	
	public String novo() {
		usuario = new UsuarioPessoa();
		usuario.setNome("");
		usuario.setEmail("");
		return "";
	}
	
	public List<UsuarioPessoa> getList() {
		return list;
	}
	
	public String delete() {
		try {
			dao.delete(usuario);
			list.remove(usuario);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Informação:", "Excluído com sucesso!"));
			usuario =  new UsuarioPessoa();
		} catch (Exception e) {
			if (e.getCause() instanceof org.hibernate.exception.ConstraintViolationException) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Informação", "Existe(m) tarefas associadas a esse usuário!"));
			}
		}
		
		return "";
	}
	

}
