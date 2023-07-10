package bean;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;

import org.primefaces.model.charts.bar.BarChartModel;

import com.google.gson.Gson;

import dao.GenericDao;
import dao.PessoaDao;
import model.Pessoa;

@ManagedBean(name = "pessoaBean")
@ViewScoped
public class PessoaBean {
	private Pessoa pessoa = new Pessoa();
	private List<Pessoa> list = new ArrayList<Pessoa>();
	private PessoaDao<Pessoa> pessoaDao = new PessoaDao<Pessoa>();
	
	
	
	@PostConstruct
	public void init() {
		list = pessoaDao.findAll(Pessoa.class);
		
		
	}
	
	public Pessoa getPessoa() {
		return pessoa;
	}
	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}
	public GenericDao<Pessoa> getDao() {
		return pessoaDao;
	}
	public void setDao(GenericDao<Pessoa> dao) {
		this.pessoaDao = pessoaDao;
	}
	
	public void pesquisaCep(AjaxBehaviorEvent event) {
		try {
			URL url = new URL("https://viacep.com.br/ws/"+pessoa.getCep()+"/json/");
			URLConnection connection = url.openConnection();
			InputStream is = connection.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			
			String cep = "";
			StringBuilder jsonCep = new StringBuilder();
			
			while((cep = br.readLine()) != null) {
				jsonCep.append(cep);
			}
			
			Pessoa auxPessoa = (Pessoa) new Gson().fromJson(jsonCep.toString(), Pessoa.class);
			pessoa.setBairro(auxPessoa.getBairro());
			pessoa.setComplemento(auxPessoa.getComplemento());
			pessoa.setLogradouro(auxPessoa.getLogradouro());			
			pessoa.setUf(auxPessoa.getUf());	
			pessoa.setUnidade(auxPessoa.getUnidade());	
			pessoa.setIbge(auxPessoa.getIbge());	
			pessoa.setCep(auxPessoa.getCep());	
			pessoa.setLocalidade(auxPessoa.getLocalidade());	
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	public String salvar() {
		pessoaDao.salvar(pessoa);
		list.add(pessoa);
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Salvo com sucesso!"));
		return "";
	}
	
	public String novo() {
		pessoa = new Pessoa();
		return "";		
	}
	public List<Pessoa> getList() {
	
		return list;
	}
	
	
	public String remover() {
		try {
			pessoaDao.removerPessoa(pessoa);
			list.remove(pessoa);
			pessoa = new Pessoa();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Informação", "Excluido com sucesso!"));
		} catch (Exception e) {
			if (e.getCause() instanceof org.hibernate.exception.ConstraintViolationException) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Informação", "Existem telefones cadastrados para a pessoa!"));
			}else {				
				e.printStackTrace();
			}
		}
		return "";
	}

}
