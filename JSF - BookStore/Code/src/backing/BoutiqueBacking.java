package backing;

import hibernate.HibernateUtil;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import javax.faces.application.FacesMessage;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIInput;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.bean.ManagedBean;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Book;
import model.Category;
import model.Client;
import model.Order;
import model.OrderItem;

import org.hibernate.Session;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

@ManagedBean(name="boutiqueBacking")
@SessionScoped
public class BoutiqueBacking {

	/** Variable d'environnement **/
	private Category category;
	private Book book;
	private Order panier;
	private Order commandeConsulte;
	private List<Order> commandes;

	/** UI Message **/
	private Client client = new Client();
	private UIInput UILogin;
	private UIInput UIPassword;
	private UIInput UIPasswordConfirmation;
	private UIInput UIMail;
	private UIInput UIMailConfirmation;
	private UIInput UIValidate;

	/** Verification formulaire **/
	private String mailVerification;
	private String passwordVerification;
	private boolean rememberme;

	public boolean isRememberme() {
		return rememberme;
	}

	public void setRememberme(boolean rememberme) {
		this.rememberme = rememberme;
	}

	/** Internalisation **/
	/** La locale courante**/
	private Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();

	public BoutiqueBacking(){
		this.panier=new Order();
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public List<Category> getList() {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction(); //Raph
		return session.createQuery("from model.Category").list();
	}

	public String selectCategory(String id) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		category = (Category) session.get(model.Category.class, new Long(id));
		return "category";
	}

	public String selectBook(String id) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		setBook((Book) session.get(model.Book.class, new Long(id)));
		return "book";
	}	

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public void paint(OutputStream out, Object data) throws IOException{
		out.write(book.getPhoto());

	}

	public Order getPanier() {
		return panier;
	}

	public void setPanier(Order panier) {
		this.panier = panier;
	}

	public String addBasket()
	{		
		panier.addOne(book);
		return null;
	}

	public String addBasket(Book bk) {		
		panier.addOne(bk);
		return null;
	}

	public String removeBasket() {		
		panier.removeOne(book);
		return null;
	}

	public String removeBasket(Book bk) {		
		panier.removeOne(bk);
		return null;
	}
	/*
	public int getQuantity() {
		return panier.getTotal().intValue();
	} 
	 */


	public Client getClient() {
		return client;
	}

	public String doLogin(){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();

		/** S'il n'y a pas de cookies **/
		Client clientVerif = (Client)session.createQuery("from model.Client client where client.login = :login and client.password = :password").setString("login", client.getLogin()).setString("password", client.getPassword()).uniqueResult();
		if(clientVerif == null){			
			FacesContext context = FacesContext.getCurrentInstance();
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", "N'existe pas");
			context.addMessage(UILogin.getClientId(context), message);
			return "login";
		}
		else if (!clientVerif.getValidate().equals("true")){
			/** Si le client n'est pas validé, 
			 * on renvoie vers disant 
			 * qu'il faut vérifier ses emails **/

			client = clientVerif;
			return "notValidate";
		}
		else {
			client = clientVerif;
			client.setNowLastlogin();
			panier.setClient(client);

			/** Gestion des cookies et du se souvenir de moi **/

			if(isRememberme()){
				/** Passage de la variable remeber de client à true **/
				/** Création (ou re-création) des cookies **/
				client.setRemember(true);
				session.save(client);


				/** On stock le login dans le cookie **/
				Cookie cookieClient = new Cookie("bkclient", client.getLogin());
				/** On stock le lastlogin client et non le password dans le cookie **/
				Cookie cookieToken = new Cookie("bktoken", String.valueOf(client.getLastlogin()));

				/** Temps cookie maximum : 7 jours **/
				/** 7 days = 604 800 seconds **/
				cookieClient.setMaxAge(604800);
				cookieToken.setMaxAge(604800);

				/** Envoie des cookies sur le PC Client **/
				HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
				response.addCookie(cookieClient);
				response.addCookie(cookieToken);

			}
			else {
				/** Passage de la variable remeber de client à false **/
				client.setRemember(false);
				session.save(client);

			}

			return "client";
		}
	}

	public String doLogout(){

		/** Récupérer les cookies **/
		Map<String, Object> cookies = FacesContext.getCurrentInstance().getExternalContext().getRequestCookieMap();
		Cookie cookieClient = (Cookie) cookies.get("bkclient");
		Cookie cookieToken = (Cookie) cookies.get("bktoken");
		/** Détruire les cookies  **/
		cookieClient.setMaxAge(0);
		cookieToken.setMaxAge(0);
		/** Update Cookie **/
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		response.addCookie(cookieClient);
		response.addCookie(cookieToken);

		/** le client ne veut plus qu'on se souvienne de lui côté serveur **/

		client.setRemember(false);
		client = new Client();
		return "logout";
	}

	public String checkCookie(){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();

		Client clientVerif;

		/** Recupérer les cookies **/
		Map<String, Object> cookies = FacesContext.getCurrentInstance().getExternalContext().getRequestCookieMap();

		Cookie cookieClient = (Cookie) cookies.get("bkclient");
		Cookie cookieToken = (Cookie) cookies.get("bktoken");

		/** Si les cookies sont présents **/
		if(cookieClient != null && cookieToken != null){
			clientVerif = (Client)session.createQuery("from model.Client client where client.login = :login and client.lastlogin = :lastlogin").setString("login", cookieClient.getValue()).setString("lastlogin", cookieToken.getValue()).uniqueResult();

			/** Verifie si le client s'est connecté il y a moins de 7 jours coté serveur **/
			/** Eviter l'utilisation frauduleuse des cookies **/
			Date now = new Date();
			/** Temps cookie maximum : 7 jours **/
			/** 7 days = 604 800 000 milliseconds **/
			now.setTime(now.getTime() -  604800000);

			/** Si la personne s'ets connecté il y a plus de 7 jours **/
			/** Eviter l'utilisation frauduleuse du cookie **/
			/** 
			 * Le cookie est ré-généré à chaque connexion 
			 * pour eviter l'utilisation sur plusieurs machines
			 **/

			if(clientVerif != null && clientVerif.isRemember() && clientVerif.getLastlogin() > now.getTime()){			

				client = clientVerif;
				client.setNowLastlogin();
				session.save(client);
				cookieToken.setValue(String.valueOf(client.getLastlogin()));

				/** Temps cookie maximum : 7 jours **/
				/** 7 days = 604 800 seconds **/
				cookieClient.setMaxAge(604800);
				cookieToken.setMaxAge(604800);

				HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
				response.addCookie(cookieClient);
				response.addCookie(cookieToken);

				return "client";

			}
		}

		return "login";
	}

	public UIInput getUILogin() {
		return UILogin;
	}


	public void setUILogin(UIInput uILogin) {
		UILogin = uILogin;
	}


	public void setClient(Client client) {
		this.client = client;
	}


	public UIInput getUIPassword() {
		return UIPassword;
	}


	public void setUIPassword(UIInput uIPassword) {
		UIPassword = uIPassword;
	}

	public String commander() {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();	
		panier.setDate(new Date());
		panier.setClient(client);
		session.save(panier);

		client = (Client) session.get(model.Client.class, client.getId());
		setCommandes(client.getOrders());

		/** Incrémenter le nombre de fois que le livre a ete achete dans la table Book **/
		for (OrderItem oi : panier.getItems()){
			//System.out.println(oi.getBook().toString()+ " passe de " + oi.getBook().getAchete() + " a "+ oi.getQuantity());
			oi.getBook().incAchete(oi.getQuantity());
			session.update(oi.getBook());
		}

		/** Une fois la commande effectuee, la panier est vide **/
		/** La page commande.xhtml affichera la derniere commande venant d'etre passe **/
		/** Celle-ci est recupre en recuprant le dernier element de commandes **/
		this.panier = new Order();
		setCommandeConsulte(this.commandes.get(commandes.size() - 1));

		return "commande";
	}

	public List<Order> getCommandes() {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		client = (Client) session.get(model.Client.class, client.getId());
		setCommandes(client.getOrders());
		return commandes;
	}

	public void setCommandes(List<Order> commandes) {
		this.commandes = commandes;
	}

	public String listeCommandes(){
		/** Charge la liste des commandes **/
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		client = (Client) session.get(model.Client.class, client.getId());
		setCommandes(client.getOrders());
		return "commandes";
	}

	public String selectCommandesConsulte(String id) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		commandeConsulte = (Order) session.get(model.Order.class, new Long(id));
		return "commande";
	}

	public Order getCommandeConsulte() {
		return commandeConsulte;
	}

	public void setCommandeConsulte(Order commandeConsulte) {
		this.commandeConsulte = commandeConsulte;
	}	

	public String doRegister(){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();

		boolean validate = true;

		/** Verifier si le login est unique **/
		Client clientVerif = (Client)session.createQuery("from model.Client client where client.login = :login").setString("login", client.getLogin()).uniqueResult();
		if(clientVerif != null){
			FacesContext context = FacesContext.getCurrentInstance();
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", "Login existe deja, veuillez en choisir un autre.");
			context.addMessage(UILogin.getClientId(context), message);
			validate = false;
		}

		/** Verifier si le mail est unique **/
		clientVerif = (Client)session.createQuery("from model.Client client where client.email = :email").setString("email", client.getEmail()).uniqueResult();
		if(clientVerif != null){
			FacesContext context = FacesContext.getCurrentInstance();
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", "Email deja utilise, veuillez en choisir un autre.");
			context.addMessage(UIMail.getClientId(context), message);
			validate = false;
		}

		/** Verifier si les deux mots de passes sont identiques **/
		if(!client.getPassword().equals(passwordVerification)){
			FacesContext context = FacesContext.getCurrentInstance();
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", "Les mots de passe ne correspondent pas.");
			context.addMessage(UIPasswordConfirmation.getClientId(context), message);
			validate = false;
		}

		/** Verifier si les deux mails sont identiques **/
		if(!client.getEmail().equals(mailVerification)){
			FacesContext context = FacesContext.getCurrentInstance();
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", "Les mails ne correspondent pas.");
			context.addMessage(UIMailConfirmation.getClientId(context), message);
			validate = false;
		}

		if (!validate){
			return "register";
		}
		else {
			String token = UUID.randomUUID().toString();
			client.setValidate(token);

			client.setRemember(false);
			Date time = new Date();
			client.setLastlogin(time.getTime());

			/** Definition des éléments de l'instance courante **/
			panier.setClient(client);
			setCommandes(new ArrayList<Order>());
			/** Sauvegarde en BD **/
			session.save(client);
			/** Il faut fermer la session pour effectuer les modifications (commit) **/
			/** Sinon le mot de passe est enregistré à NULL dans la BD **/
			session.close();

			//UIMessage.add(new Message("Succes", "Compte créé"));

			/** Envoyer un email pour confirmation **/
			this.sendMail();

			return "checkMail.xhtml";
		}
	}

	public String sendMail(){

		String objet = "Bienvenue sur Bookstore";
		String nomDestinataire = client.getLogin();
		String mailDestinataire = client.getEmail();
		String message = "Bienvenue " + client.getLogin() + " !<br>" +
				"Pour activer votre compte, cliquez sur : <br>" +
				"http://localhost:8080/BookStore-Hibernate-JSF20-Start/validateUser.jsf?login=" + client.getLogin() + "&token=" 
				+ client.getValidate() + "<br> Merci, <br> A bientôt sur BookStore.";

		sendMail(objet, mailDestinataire, nomDestinataire, message);

		return "login";
	}

	public String sendMail(String objet, String adresseDestinataire, String nomDestinataire, String message){

		SimpleEmail email = new SimpleEmail();

		email.setSmtpPort(587);
		email.setAuthenticator(new DefaultAuthenticator("projetbookstore",
				"dauphine123"));
		email.setDebug(false);
		email.setHostName("smtp.gmail.com");


		try {
			email.addTo(adresseDestinataire, nomDestinataire);
			email.setFrom("projetbookstore@gmail.com", "Bookstore");
			email.setSubject(objet);
			email.setMsg(message);
			email.setTLS(true);
			email.send();

		} catch (EmailException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//UIMessage.add(new Message("Succes", "Mail de confirmation envoyé"));

		return "login";
	}

	public List<Book> meilleuresVentes(){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		List<Book> livres = (List<Book>) session.createQuery("from model.Book livre order by livre.achete desc").setMaxResults(2).list();		
		return livres;
	}

	public void validateUser(){

		HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();

		/** Necessaire pour la redirection **/
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();

		client.setLogin(request.getParameter("login").toString());
		client.setValidate(request.getParameter("token").toString());

		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Client clientVerif = (Client)session.createQuery("from model.Client client where client.login = :login").setString("login", client.getLogin()).uniqueResult();

		if(clientVerif == null){			
			FacesContext context = FacesContext.getCurrentInstance();
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", "N'existe pas");
			context.addMessage(UILogin.getClientId(context), message);

			//UIMessage.add(new Message("Avertissement", "Le login n'est pas bon, veuillez verifier vos mails"));

			try {
				ec.redirect("login.jsf");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		else if (!clientVerif.getValidate().equals(client.getValidate())){
			/** Si le client n'est pas validé, 
			 * et que le token est faux, on lui dit
			 * qu'il faut vérifier ses emails **/

			FacesContext context = FacesContext.getCurrentInstance();
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", "Déjà validé");
			//context.addMessage(UIValidate.getClientId(context), message);
			client = clientVerif;

			//UIMessage.add(new Message("Avertissement", "Le token n'est pas bon, veuillez verifier vos mails"));

			try {
				ec.redirect("notValidate.jsf");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		else {
			client = clientVerif;
			client.setValidate("true");
			session.save(client);

			//UIMessage.add(new Message("Succes", "Compte Validé"));

			try {
				ec.redirect("hasbeenvalidate.xhtml");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


		}
	}

	public UIInput getUIPasswordConfirmation() {
		return UIPasswordConfirmation;
	}

	public void setUIPasswordConfirmation(UIInput uIPasswordConfirmation) {
		UIPasswordConfirmation = uIPasswordConfirmation;
	}

	public UIInput getUIMail() {
		return UIMail;
	}

	public void setUIMail(UIInput uIMail) {
		UIMail = uIMail;
	}

	public UIInput getUIMailConfirmation() {
		return UIMailConfirmation;
	}

	public void setUIMailConfirmation(UIInput uIMailConfirmation) {
		UIMailConfirmation = uIMailConfirmation;
	}

	public UIInput getUIValidate() {
		return UIValidate;
	}

	public void setUIValidate(UIInput uIValidate) {
		UIValidate = uIValidate;
	}

	public String getMailVerification() {
		return mailVerification;
	}

	public void setMailVerification(String mailVerification) {
		this.mailVerification = mailVerification;
	}

	public String getPasswordVerification() {
		return passwordVerification;
	}

	public void setPasswordVerification(String passwordVerification) {
		this.passwordVerification = passwordVerification;
	}

	/** Permet d'obtenir la nom de la locale **/
	public String getLanguage() {
		return locale.getLanguage();
	}

	/** Permet de modifier le language de la page **/
	public String changeLanguage(String language) {
		if (locale.getLanguage().equals(language)){
			return null;
		}
		else {
			locale = new Locale(language);
			FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
			return null;
		}
	}

	/** Permet d'obtenir la locale courante **/
	public Locale getLocale()
	{
		return locale;
	}

}
