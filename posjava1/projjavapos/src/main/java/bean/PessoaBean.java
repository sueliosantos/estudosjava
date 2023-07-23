package bean;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;

import org.apache.tomcat.util.codec.binary.Base64;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;

import com.google.gson.Gson;

import dao.EmailDao;
import dao.GenericDao;
import dao.PessoaDao;
import model.EmailPessoa;
import model.Pessoa;

@ManagedBean(name = "pessoaBean")
@ViewScoped
public class PessoaBean {
	private Pessoa pessoa = new Pessoa();
	private List<Pessoa> list = new ArrayList<Pessoa>();
	private PessoaDao<Pessoa> pessoaDao = new PessoaDao<Pessoa>();
	private EmailPessoa emailPessoa = new EmailPessoa();
	private EmailDao<EmailPessoa> emailDao = new EmailDao<EmailPessoa>();
	private String nomeFiltroPesquisa;
	
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
	
	public void addEmail() {
		emailPessoa.setPessoa(pessoa);
		emailPessoa = emailDao.updateMarge(emailPessoa);
		pessoa.getEmails().add(emailPessoa);
		emailPessoa = new EmailPessoa();
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Mensagem", "Cadastrado com sucesso.");
		PrimeFaces.current().dialog().showMessageDynamic(message);
		
	}
	
	public void removerEmail() throws Exception {
		String idEmail = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("idemail");
		EmailPessoa email = new EmailPessoa();
		email.setId(Long.parseLong(idEmail));
		emailDao.delete(email);
		
		pessoa.getEmails().remove(email);
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Mensagem", "Removido com sucesso.");
		PrimeFaces.current().dialog().showMessageDynamic(message);
	}
	
	public void pesquisar() {
		list = pessoaDao.pesquisarPorNome(nomeFiltroPesquisa);
	}

	public EmailPessoa getEmailPessoa() {
		return emailPessoa;
	}

	public void setEmailPessoa(EmailPessoa emailPessoa) {
		this.emailPessoa = emailPessoa;
	}

	public String getNomeFiltroPesquisa() {
		return nomeFiltroPesquisa;
	}

	public void setNomeFiltroPesquisa(String nomeFiltroPesquisa) {
		this.nomeFiltroPesquisa = nomeFiltroPesquisa;
	}

	public void uploadImg(FileUploadEvent img) {
		String imagem = "data:image/png;base64," + DatatypeConverter.printBase64Binary(img.getFile().getContent());
		pessoa.setFoto(imagem);
	}
	
	public void download() throws IOException {
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();	
		String fileId = params.get("fileId");
		
		Pessoa pessoa = getDao().pesquisar(Long.parseLong(fileId), Pessoa.class);
		
		if (pessoa.getFoto() != null) {			
			byte[] img = new Base64().decodeBase64(pessoa.getFoto().split("\\,")[1]);
			HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
			
			response.addHeader("Content-Disposition", "attachment; filename=download.png");
			response.setContentType("application/octet-stream");
			response.setContentLength(img.length);
			response.getOutputStream().write(img);
			response.getOutputStream().flush();
			FacesContext.getCurrentInstance().responseComplete();
		}
	}
	
}
