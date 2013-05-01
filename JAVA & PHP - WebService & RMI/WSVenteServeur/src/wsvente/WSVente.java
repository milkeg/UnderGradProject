package wsvente;



import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.rpc.ServiceException;


import wsbanque.WSBanque;
import wsbanque.WSBanqueServiceLocator;
import wsgros.WSGros;
import wsgros.WSGrosServiceLocator;
import de.daenet.www.webservices.CurrencyServer.CurrencyServerWebServiceLocator;
import de.daenet.www.webservices.CurrencyServer.CurrencyServerWebServiceSoap;


public class WSVente {

	private String deviseVoulue = "EUR";
	private Map<String,CatalogueInterface> catalogues;
	private Panier panier;
	private ArrayList<Commande> commandes;
	private CurrencyServerWebServiceSoap currencyService;
	private WSBanque banque;
	private String messageDelai = "";
	private String messageBanque = "";

	public WSVente() throws RemoteException, MalformedURLException, ServiceException {
		super();
		System.out.println("constructeur");
		System.out.println("cata : "+catalogues);
		catalogues = new HashMap<String,CatalogueInterface>();
		panier = new Panier();
		commandes = new ArrayList<Commande>();
		currencyService = new CurrencyServerWebServiceLocator().getCurrencyServerWebServiceSoap();
		banque = new WSBanqueServiceLocator().getWSBanque();
		String[]args = {};
		chercherCatalogues(args);
	}

	public float consulterPrix(String ref) throws RemoteException
	{
		Iterator<String> it = catalogues.keySet().iterator();
		boolean pres = false;
		while(it.hasNext()){
			String nom = it.next();
			CatalogueInterface cata = catalogues.get(nom);
			pres = cata.verifierPresence(ref);
			if(pres){
				return (float) (cata.consulterPrix(ref)*currencyService.getCurrencyValue("AVERAGE", "EUR", deviseVoulue));
			}else{
				System.out.println("Le produit n'est pas present dans le catalogue "+nom);
			}
		}
		if(!pres){
			System.out.println("le produit ne se trouve dans aucun catalogue");
		}
		return -1;
	}

	public void initCommunication(){
	}

	public int verifierDisponibilite(String ref) throws RemoteException {
		Iterator<String> it = catalogues.keySet().iterator();
		boolean pres = false;
		while(it.hasNext()){
			String nom = it.next();
			CatalogueInterface cata = catalogues.get(nom);
			pres = cata.verifierPresence(ref);
			if(pres){
				System.out.println("Le produit est present dans le catalogue "+nom);
				return cata.verifierDisponibilite(ref);
			}else{
				System.out.println("Le produit n'est pas present dans le catalogue "+nom);
			}
		}
		if(!pres){
			System.out.println("le produit ne se trouve dans aucun catalogue");
		}
		return -1;
	} 

	public boolean ajouterPanier(String cat, String ref, int quantite) throws RemoteException {
		float prixUnitaire = consulterPrix(ref);
		return panier.ajouter(cat,ref,quantite,prixUnitaire);
	}	

	public boolean acheterPanier(int numeroCompte, String password, String nomClient, String prenomClient) throws RemoteException {
		float prixEuro = panier.valeur();
		float prixDeviseVoulue = (float) (prixEuro*currencyService.getCurrencyValue("AVERAGE", "EUR", deviseVoulue));

		setMessageDelai(null);

		int argentDispo = banque.verifierFondDispo(numeroCompte, password, prixEuro);
		System.out.println(argentDispo);
		switch (argentDispo) {
		case 1:
			banque.effectuerPaiment(numeroCompte, prixEuro);
			setMessageBanque("Paiement OK");
			break;
		case 0:
			setMessageBanque("Vous n'avez pas assez d'argent sur votre compte");
			System.out.println(messageBanque);
			return false;
		case -1:
			setMessageBanque("Le mot de passe que vous avez fournit n'est pas correct");
			return false;
		case -2:
			setMessageBanque("Votre compte n'existe pas dans la banque");
			return false;
		default:
			break;
		}

		int delai = 3;
		setMessageDelai("Votre délai de livraison est de "+ delai + " jours.");
		CatalogueInterface cata = null;

		for (PanierItem item : panier.getPanier())
		{
			Iterator<String> it = catalogues.keySet().iterator();
			boolean present = false;
			while(it.hasNext()){
				String nomCatalogue = it.next();
				cata = catalogues.get(nomCatalogue);
				present = cata.verifierPresence(item.getReference());
				if(present){
					break;
				}					
			}

			if(verifierDisponibilite(item.getReference()) <= item.getQuantite())
			{						
				WSGros service;
				try {
					service = new WSGrosServiceLocator().getWSGros();
					int differentiel = item.getQuantite() - verifierDisponibilite(item.getReference());
					boolean stockSuffisant = service.commanderProduit(item.getReference(), 10+differentiel);
					if(stockSuffisant){
						cata.approvisionner(item.getReference(), 10+differentiel);
					}else {
						return false;
					}
				} catch (ServiceException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				delai +=7;
				setMessageDelai("Votre délai de livraison est de "+ delai + " jours.");
			}
			
			cata.acheterProduit(item.getReference(), item.getQuantite());

		}
		
		

		commandes.add(new Commande(panier, nomClient, prenomClient, delai, deviseVoulue, prixDeviseVoulue));
		panier.vider();
		return true;
	}

	public boolean retirerPanier(String cat, String ref, int quantite) {
		return panier.retirer(cat,ref,quantite);
	}

	public boolean viderPanier() {
		return panier.vider();
	}

	public boolean changerDevise(String nouvDev) {
		deviseVoulue = nouvDev;
		return true;
	}


	private void chercherCatalogues(String[] args) throws RemoteException, MalformedURLException
	{
		if (System.getSecurityManager() == null){
			System.setProperty("java.security.policy", "/Users/michaelgoletto/Documents/workspace/WSVenteServeur/src/security.policy");
			System.setSecurityManager(new RMISecurityManager());
		}
		String host;
		if (args.length == 0) host = "localhost:1099";
		else host = args[0];

		String[] names = Naming.list("rmi://" + host + "/");
		try{
			for (int i = 0; i < names.length; i++)
			{
				System.out.println(names[i]);
				CatalogueInterface cat = (CatalogueInterface) Naming.lookup(names[i]);
				catalogues.put(names[i], cat);
			}
		}
		catch (Exception e) { 
			e.printStackTrace();
		}
	}


	public boolean validerAjout(String descr, String cat , String ref ) throws RemoteException {
		if(descr.length() <= 100){
			Iterator<String> it = catalogues.keySet().iterator();
			while(it.hasNext()){
				String nom = it.next();	
				CatalogueInterface cata = catalogues.get(nom);
				return cata.ValiderAjout(descr, ref);
			}
			return false;
		}else{
			return false;
		}
	}

	public Produit[] getListeProduitsParCategorie(String categorie) throws RemoteException{
		List<Produit> results = new ArrayList<Produit>();
		Iterator<String> it = catalogues.keySet().iterator();
		while(it.hasNext()){
			String nom = it.next();
			CatalogueInterface cata = catalogues.get(nom);
			List<Produit> list = cata.getListeProduitsParCategorie(categorie);
			results.addAll(list);
		}
		Produit[] res = new Produit[results.size()];
		double change = currencyService.getCurrencyValue("AVERAGE", "EUR", deviseVoulue);
		for(int i=0;i<results.size();i++){
			res[i]=results.get(i);
			res[i].setPrix((float)(res[i].getPrix()*change));
		}
		return res;
	}
	
	public Produit[] getListeProduitsParType(String type) throws RemoteException{
		List<Produit> results = new ArrayList<Produit>();
		Iterator<String> it = catalogues.keySet().iterator();
		while(it.hasNext()){
			String nom = it.next();
			CatalogueInterface cata = catalogues.get(nom);
			List<Produit> list = cata.getListeProduitsParType(type);
			results.addAll(list);
		}
		Produit[] res = new Produit[results.size()];
		double change = currencyService.getCurrencyValue("AVERAGE", "EUR", deviseVoulue);
		for(int i=0;i<results.size();i++){
			res[i]=results.get(i);
			res[i].setPrix((float)(res[i].getPrix()*change));
		}
		return res;
	}
	
	public Produit[] getAllProduits() throws RemoteException{
		List<Produit> results = new ArrayList<Produit>();
		Iterator<String> it = catalogues.keySet().iterator();
		while(it.hasNext()){
			String nom = it.next();
			CatalogueInterface cata = catalogues.get(nom);
			List<Produit> list = cata.getAllProduits();
			results.addAll(list);
		}
		Collections.sort(results);
		Produit[] res = new Produit[results.size()];
		double test = System.currentTimeMillis();
		double change = currencyService.getCurrencyValue("AVERAGE", "EUR", deviseVoulue);
		System.out.println("temps pour calculer le change : "+(System.currentTimeMillis()-test));

		double test2 = System.currentTimeMillis();
		for(int i=0;i<results.size();i++){
			test = System.currentTimeMillis();
			res[i]=results.get(i);
			res[i].setPrix((float)(res[i].getPrix()*change));
			System.out.println("temp iteration "+i+" : "+(System.currentTimeMillis()-test));
		}
		System.out.println("temps methode getAllproducts : "+(System.currentTimeMillis()-test2));
		return res;
	}
	
	public Categorie[] getListeCategorie() throws RemoteException {
		List<Categorie> results = new ArrayList<Categorie>();
		Iterator<String> it = catalogues.keySet().iterator();
		while(it.hasNext()){
			String nom = it.next();
			CatalogueInterface cata = catalogues.get(nom);
			List<Categorie> list = cata.getListeCategorie();
			results.addAll(list);
		}
		Categorie[] res = new Categorie[results.size()];
		for(int i=0;i<results.size();i++){
			res[i]=results.get(i);
		}
		return res;
	}
	
	public PanierItem[] getPanier(){
		for(int i=0; i< panier.getPanier().size(); i++){
			System.out.println(panier.getPanier().get(i).getReference());
		}
		return new PanierSerial(panier).getPanier();
		
	}

	//	public String testDispoGrossiste(String ref) throws RemoteException {
	//		Iterator<String> it = grossistes.keySet().iterator();
	//		boolean pres = false;
	//		while(it.hasNext()){
	//			String nom = it.next();
	//			GrossisteInterface gro = grossistes.get(nom);
	//			pres = gro.verifierPresence(ref);
	//			if(pres){
	//				return "produit present chez "+nom;
	//			}else{
	//				System.out.println("Le produit n'est pas present chez le grossiste "+nom);
	//			}
	//		}
	//		if(!pres){
	//			return "le produit ne se trouve chez aucun grossiste";
	//		}
	//		return null;
	//	}
	
//	public Map getCatalogues(){
//		return catalogues;
//	}

	public static void main(String[] args) throws RemoteException, MalformedURLException, ServiceException {
//		WSVente ve = new WSVente();
		//Les catalogues
//		Map<String,CatalogueInterface> cats = ve.getCatalogues();

		//Test produits de la categorie Livre

//		List<Produit> list = ve.getListeProduitsParCategorie("Musique");
//		for(int i=0;i<list.size();i++){
//			System.out.println(list.get(i).getNom());
//		}


		//Test disponibilite + prix
//		System.out.println("il reste "+ve.verifierDisponibilite("Les Robots")+" exemplaires de ce produit");
//		System.out.println("le produit coute "+ve.consulterPrix("Les Robots"));

		//Test grossiste
		//		System.out.println(ve.testDispoGrossiste("Ben l'Oncle Soul"));
		CurrencyServerWebServiceSoap currencyService = new CurrencyServerWebServiceLocator().getCurrencyServerWebServiceSoap();
		System.out.println(currencyService.getCurrencyValue("AVERAGE", "EUR", "USD"));
	}

	public String getMessageBanque() {
		System.out.println(messageBanque);
		return messageBanque;
	}

	public void setMessageBanque(String messageBanque) {
		this.messageBanque = messageBanque;
	}
	
	public float getTotalPanier(){
		try {
			return panier.valeur() * (float)(currencyService.getCurrencyValue("AVERAGE", "EUR", deviseVoulue));
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return (Float) null;
	}

	public String getMessageDelai() {
		return messageDelai;
	}

	public void setMessageDelai(String messageDelai) {
		this.messageDelai = messageDelai;
	}

}
