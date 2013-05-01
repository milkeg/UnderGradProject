package wsgros;

import java.io.Serializable;


public class Produit implements Serializable, Comparable<Produit> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String nom;
	private int quantite;
	private float prix;
	private String description;
	private String type;
	private Categorie categorie;
	
	public Produit(){}
	
	public Produit(String nom, int quantite, float prix, String description, String type, Categorie categorie){
		this.nom = nom;
		this.quantite = quantite;
		this.prix = prix;
		this.description = description;
		this.setType(type);
		this.categorie = categorie;
	}
	
	public Produit(String nom, int quantite, float prix, String description){
		this.nom = nom;
		this.quantite = quantite;
		this.prix = prix;
		this.description = description;
	}
	
	public String getNom() {
		return nom;
	}
	
	public void setNom(String nom) {
		this.nom = nom;
	}
	
	public int getQuantite() {
		return quantite;
	}
	
	public void setQuantite(int quantite) {
		this.quantite = quantite;
	}
	
	public float getPrix() {
		return prix;
	}
	
	public void setPrix(float prix) {
		this.prix = prix;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public Categorie getCategorie() {
		return categorie;
	}
	
	public void setCategorie(Categorie categorie) {
		this.categorie = categorie;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public int compareTo(Produit o) {
		return this.getNom().compareTo(o.getNom());
	}
	
}
