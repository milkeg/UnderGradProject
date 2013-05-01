package wsbanque;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class WSBanque {
	
	private Map<Integer, Compte> comptes;
	
	public WSBanque(){
		System.out.println("Constructeur WSBanque");
		comptes = new HashMap<Integer,Compte>();
		File fichier = new File("/Users/michaelgoletto/Documents/workspace/WSBanque/ComptesXML/comptes.xml");
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fichier);
			
			NodeList comptesNode = doc.getElementsByTagName("compte");
			
			for(int i=0; i<comptesNode.getLength(); i++){
				Element compteNode = (Element) comptesNode.item(i);
				int numeroCompte = Integer.parseInt(compteNode.getAttribute("numeroCompte"));
				String passwordCompte = compteNode.getAttribute("password");
				double solde = Double.parseDouble(compteNode.getElementsByTagName("solde").item(0).getTextContent());
				Compte compte = new Compte(numeroCompte, passwordCompte, solde);
				
				comptes.put(compte.getNumeroCompte(), compte);
			}
			
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public boolean addCompte(int numeroCompte, String password, double solde){
		if(comptes.containsKey(numeroCompte)){
			return false;
		}else{
			Compte compte = new Compte(numeroCompte, password, solde);
			comptes.put(numeroCompte, compte);
			return true;
		}
	}
	
	/**
	 * Methode qui verifie la disponibilite des fonds sur un compte
	 * @return renvoie -2 si le compte n'existe pas, 
	*				   -1 si le client n'a pas donne le bon mot de passe, 
	*					0 si le client n'a pas les fonds necessaires sur son compte, 
	*					1 si le client a assez de fond
	*/
	public int verifierFondDispo(int numeroCompte, String password, double montant){
		Iterator<Entry<Integer, Compte>> it = comptes.entrySet().iterator();
		while(it.hasNext()){
			Entry<Integer, Compte> e = it.next();
			System.out.println(e.getKey());
		}
		Compte compte = comptes.get(numeroCompte);
		if(compte != null){
			if(password.equals(compte.getPassword())){
				return compte.retraitAutorise(montant) ? 1 : 0;
			}else {
				return -1;
			}
		}else{
			return -2;
		}
	}
	
	public boolean effectuerPaiment(int numeroCompte, double montant){
		return comptes.get(numeroCompte).retraitDe(montant);
	}
}