package wsvente;

import java.io.Serializable;

public class PanierSerial implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private PanierItem[] panier;
	
	public PanierSerial(){
		System.out.println("Contructeur PanierSerial");
	}
	
	public PanierSerial(Panier pan){
		panier = new PanierItem[pan.getPanier().size()];
		for(int i=0;i<panier.length;i++){
			panier[i] = pan.getPanier().get(i);
		}
	}

	public PanierItem[] getPanier() {
		return panier;
	}

	public void setPanier(PanierItem[] panier) {
		this.panier = panier;
	}
	
	
	
	
	
	

}
