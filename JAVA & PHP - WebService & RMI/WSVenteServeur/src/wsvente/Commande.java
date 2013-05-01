package wsvente;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class Commande implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static int id = 0;
	private Panier panier;
	private String nom;
	private String prenom;
	private Date dateCommande;
	private Date datePrevisionLivraison;
	private String devise;
	private float facture;
	
	public Commande(Panier panier, String nom, String prenom, int delai, String devise, float facture)
	{
		id++;
		dateCommande = new Date();
		GregorianCalendar calendar = new java.util.GregorianCalendar();
		calendar.setTime(dateCommande); 
		calendar.add(Calendar.DATE, delai);
		datePrevisionLivraison = calendar.getTime();
		this.devise = devise;
		this.panier = panier;
		this.nom = nom;
		this.prenom = prenom;
		this.facture = facture;
	}
	
	public static int getId() {
		return id;
	}

	public static void setId(int id) {
		Commande.id = id;
	}

	public Panier getPanier() {
		return panier;
	}

	public void setPanier(Panier panier) {
		this.panier = panier;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public Date getDateCommande() {
		return dateCommande;
	}

	public void setDateCommande(Date dateCommande) {
		this.dateCommande = dateCommande;
	}

	public Date getDatePrevisionLivraison() {
		return datePrevisionLivraison;
	}

	public void setDatePrevisionLivraison(Date datePrevisionLivraison) {
		this.datePrevisionLivraison = datePrevisionLivraison;
	}

	public String getDevise() {
		return devise;
	}

	public void setDevise(String devise) {
		this.devise = devise;
	}

	public float getFacture() {
		return facture;
	}

	public void setFacture(float facture) {
		this.facture = facture;
	}


}
