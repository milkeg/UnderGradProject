package wsvente;


import java.io.Serializable;
import java.util.ArrayList;


public class Panier implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ArrayList<PanierItem> panier = new ArrayList<PanierItem>();
	
	private PanierItem itemPresent(String cat, String ref)
	{
		PanierItem temp = new PanierItem(cat,ref, 0, 0);
		if(panier.contains(temp)){
			return panier.get(panier.indexOf(temp));			
		}
		else return null;
	}
	
	public boolean ajouter(String cat, String ref,int quantite, float prixUnitaire)
	{
		PanierItem temp = itemPresent(cat,ref);
		if(temp!=null)
		{
			temp.setQuantite(quantite + temp.getQuantite());
			return true;
		}
		else 
		{
			return panier.add(new PanierItem(cat,ref,quantite,prixUnitaire));
		}
	}	
	
	public boolean retirer(String cat, String ref, int quantite)
	{
		PanierItem temp = itemPresent(cat,ref);
		if (temp != null && temp.getQuantite() == quantite){
			panier.remove(temp);
			return true;
		}
		else if(temp!=null && temp.getQuantite()>=quantite)
		{
			temp.setQuantite(temp.getQuantite() - quantite);
			return true;
		}
		else return false;
	}
	
	public boolean vider()
	{
		panier.clear();
		return true;
	}
	
	public Panier()
	{
		super();
	}
	
	public float valeur()
	{
		float temp = 0;
        for(PanierItem element: panier){
            temp += element.prixUnitaire*element.quantite;
        }
        return temp;
	}

	public ArrayList<PanierItem> getPanier() {
		return panier;
	}
}
