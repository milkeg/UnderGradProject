package wsgros;

import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Hashtable;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Grossiste extends UnicastRemoteObject implements GrossisteInterface {

	private static final HashMap<String, Categorie> categories;
	static {
		categories = new HashMap<String, Categorie>();
		categories.put("Livre", new Categorie("Livre", null));
		categories.put("Vetement", new Categorie("Vetement", null));
		categories.put("Nourriture", new Categorie("Nourriture", null));
		categories.put("Musique", new Categorie("Musique", null));
		categories.put("Papeterie", new Categorie("Papeterie", null));
	}
	
	private long id;
	private Hashtable<String, Produit> stock = new Hashtable<String, Produit>();
	
	public Grossiste(String fichierXML) throws RemoteException {
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
				
				stock.put(nomProd, prod);
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
	
	
	
	public Hashtable<String, Produit> getStock() {
		return stock;
	}

	public void setStock(Hashtable<String, Produit> stock) {
		this.stock = stock;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}


	public boolean verifierPresence(String ref) throws RemoteException {
		System.out.println("Verification de la presence de '"+ref+"'");
		return stock.containsKey(ref);
	}
	
	public boolean commanderProduit(String ref, int quantite) throws RemoteException {
		System.out.println("Commande de "+quantite+" unite(s) de '"+ref+"'");
		Produit prod = stock.get(ref);
		prod.setQuantite(prod.getQuantite()-quantite);
		return true;
	}
	

}
