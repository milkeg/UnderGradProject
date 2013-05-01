package wsvente;

import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Catalogue extends UnicastRemoteObject implements CatalogueInterface {

	private static final HashMap<String, Categorie> categories;
	static {
		categories = new HashMap<String, Categorie>();
		categories.put("Livre", new Categorie("Livre", "Livres sur divers sujet, tels que la cuisine, le fantastique, les thrillers..."));
		categories.put("Vetement", new Categorie("Vetement", "Vetements pour homme et pour femmes, allant de 5 ans à 40 ans"));
		categories.put("Nourriture", new Categorie("Nourriture", "Tout ce dont vous avez besoin pour vous nourrir au quotidient !"));
		categories.put("Musique", new Categorie("Musique", "Ecoutez les derniers sons du moment, decouvrez les avant premieres des artistes les plus en vogue !"));
		categories.put("Papeterie", new Categorie("Papeterie", "Besoin d'un stylo ? De feuille ? D'un compas ? La papeterie est ouverte 24h/24 et 7j/7 !"));
	}
	
	private long id;
	private Hashtable<String, Produit> catalogue = new Hashtable<String, Produit>();
	
	public Catalogue(String fichierXML) throws RemoteException {
		super();
		File fichier = new File(fichierXML);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fichier);
			
			NodeList produits = doc.getElementsByTagName("produit");
			
			for(int i=0; i<produits.getLength(); i++){
				Element prodNode = (Element) produits.item(i);
				String nomProd = prodNode.getElementsByTagName("nomProduit").item(0).getTextContent();
				int quantiteProd = Integer.parseInt(prodNode.getElementsByTagName("quantite").item(0).getTextContent());
				float prixProd = Float.parseFloat(prodNode.getElementsByTagName("prix").item(0).getTextContent());
				String descProd = prodNode.getElementsByTagName("description").item(0).getTextContent();
				Categorie catProd = categories.get(prodNode.getAttribute("categorie"));
				Produit prod = new Produit(nomProd, quantiteProd, prixProd, descProd, "IF", catProd);
				
				catalogue.put(nomProd, prod);
			}
			
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		id = this.hashCode();
	}
	
	
	
	public Hashtable<String, Produit> getCatalogue() {
		return catalogue;
	}

	public void setCatalogue(Hashtable<String, Produit> catalogue) {
		this.catalogue = catalogue;
	}
	
	public float consulterPrix(String ref) throws RemoteException {
		System.out.println("Consultation du prix de '"+ref+"'");
		if(catalogue.containsKey(ref)) {
			Produit prod = catalogue.get(ref);
			return prod.getPrix();
		}else{
			return -1;
		}
	}

	public long getId() {
		return id;
	}



	public void setId(long id) {
		this.id = id;
	}



	public int verifierDisponibilite(String ref) throws RemoteException {
		System.out.println("Verification de disponibilite de '"+ref+"'");
		if(catalogue.containsKey(ref)) {
			Produit prod = catalogue.get(ref);
			return prod.getQuantite();
		}else{
			return -1;
		}
	}

	
	public boolean ValiderAjout(String descr, String ref) throws RemoteException {
		return false;
	}

	public boolean verifierPresence(String ref) throws RemoteException {
		System.out.println("Verification de la presence de '"+ref+"'");
		return catalogue.containsKey(ref);
	}
	
	public boolean acheterProduit(String ref, int quantite) throws RemoteException {
		System.out.println("Achat de "+quantite+" unite(s) de '"+ref+"'");
		Produit prod = catalogue.get(ref);
		prod.setQuantite(prod.getQuantite()-quantite);
		return true;
	}
	
	public List<Produit> getListeProduitsParCategorie(String categorie) throws RemoteException {
		System.out.println("Consultation de la liste des produits de la categorie '"+categorie+"'");
		List<Produit> results = new ArrayList<Produit>();
		Categorie realCat = categories.get(categorie);
		Iterator<String> it = catalogue.keySet().iterator();
		while(it.hasNext()){
			String nom = it.next();
			Produit prod = catalogue.get(nom);
			if(prod.getCategorie().equals(realCat)){
				results.add(prod);
			}
		}
		return results;
	}
	
	public boolean approvisionner(String ref, int quantite) throws RemoteException {
		System.out.println("Approvisionnement de "+quantite+" exemplaires de '"+ref+"'");
		Produit prod = catalogue.get(ref);
		prod.setQuantite(prod.getQuantite()+quantite);
		return true;
	}
	
		public List<Produit> getAllProduits() throws RemoteException {
		System.out.println("Consultation de la liste de tous les produits");
		List<Produit> results = new ArrayList<Produit>();
		Iterator<Produit> it = catalogue.values().iterator();
		while(it.hasNext()){
			Produit prod = it.next();
			results.add(prod);
		}

		return results;
	}
	
	public List<Produit> getListeProduitsParType(String type) throws RemoteException {
		System.out.println("Consultation de la liste des produits du type '"+type+"'");
		List<Produit> results = new ArrayList<Produit>();
		Iterator<Produit> it = catalogue.values().iterator();
		while(it.hasNext()){
			Produit prod = it.next();
			if(prod.getType().equals(type)){
				results.add(prod);
			}
		}
		return results;
	}
	
	public List<Categorie> getListeCategorie() throws RemoteException {
		System.out.println("Consultation de la liste des categories");
		return (List<Categorie>)categories.values();
	}
}
