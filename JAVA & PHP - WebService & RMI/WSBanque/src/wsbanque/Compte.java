package wsbanque;

public class Compte {

	private int numeroCompte;
	private double solde;
	private String password;
	
	public Compte(int numeroCompte, String password) {
		this.setNumeroCompte(numeroCompte);
		this.password = password;
		solde = 0;
	}
	
	public Compte(int numeroCompte, String password, double solde) {
		this.setNumeroCompte(numeroCompte);
		this.password = password;
		this.solde = solde;
	}
	
	public void depotDe(double montant){
		setSolde(solde+montant);
	}
	
	public boolean retraitDe(double montant){
		if(retraitAutorise(montant)){
			solde-=montant;
		}else{
			return false;
		}
		return true;
	}
	
	public boolean retraitAutorise(double montant){
		if(solde >= montant){
			return true;
		}else{
			return false;
		}
	}
	
	public double valeurDuSolde(){
		return solde;
	}

	public double getSolde() {
		return solde;
	}

	public void setSolde(double solde) {
		this.solde = solde;
	}

	public int getNumeroCompte() {
		return numeroCompte;
	}

	public void setNumeroCompte(int numeroCompte) {
		this.numeroCompte = numeroCompte;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
	
}
