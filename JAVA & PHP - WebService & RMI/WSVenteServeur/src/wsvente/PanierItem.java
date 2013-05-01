package wsvente;

import java.io.Serializable;

public class PanierItem implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	String categorie = null;
	String reference = null;
	int quantite = 0;
	float prixUnitaire = 0;
	
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (obj == this)
            return true;
        if (obj.getClass() != getClass())
            return false;

        PanierItem temp = (PanierItem) obj;
        if(temp.reference.equals(this.reference)) 
        	return true;
        else return false;
    }
    
    
    public PanierItem(){}
    
    public PanierItem(String categorie, String reference)
    {
    	this.categorie = categorie;
    	this.reference = reference;
    	quantite = 0;
    }

    
    public PanierItem(String categorie, String reference, int quantite,float prixUnitaire)
    {
    	this(categorie,reference);
    	this.quantite = quantite;
    	this.prixUnitaire = prixUnitaire;
    }
    
    public void setQuantite(int i)
    {
    	if(i>=0)
    		quantite = i;
    }
    
    public int getQuantite()
    {
    	return quantite;
    }

    
    public String getReference()
    {
    	return reference;
    }

}
