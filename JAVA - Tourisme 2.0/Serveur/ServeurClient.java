package Serveur;

import java.net.InetAddress;
import java.util.ArrayList;

public class ServeurClient extends Connexion {
	private String nomClient;
	private InetAddress adresseIP;
	private int portClient;
	private ArrayList<Alerte> alertes = new ArrayList<Alerte>();
	
	public ServeurClient(String nomClient, InetAddress adresseIP, int port) {
		super();
		this.nomClient = nomClient;
		this.adresseIP = adresseIP;
		this.portClient = port++;
	}

	public String getNomClient() {
		return nomClient;
	}

	public void setNomClient(String nomClient) {
		this.nomClient = nomClient;
	}

	public InetAddress getAdresseIP() {
		return adresseIP;
	}

	public void setAdresseIP(InetAddress adresseIP) {
		this.adresseIP = adresseIP;
	}
	
	protected int getPortClient() {
		return portClient;
	}

	protected void setPortClient(int portClient) {
		this.portClient = portClient;
	}

	public void abonnerAlerte(Alerte e) {
		synchronized (alertes) {
			alertes.add(e);
		}
	}
	
	public ArrayList<Alerte> alertesActivees() {
		// retourne la liste des alertes activées
		ArrayList<Alerte> list = new ArrayList<Alerte>();
		synchronized (alertes) {
			for(Alerte e : alertes) {
				if(e.alerteActivee() && !e.isNotifiee()) list.add(e);
			}	
		}
		return list;
	}
	
	public String alertesActiveesToString() {
		ArrayList<Alerte> list = alertesActivees();
		String res = "";
		for(Alerte e : list) {
			res += e.toString();
			e.setNotifiee(true);
		}
		return res;
	}

	public class Alerte {

		private Ville.Activite activite;
		private float noteMin = 0;
		private float noteMax = 20;
		private boolean notifiee = false;
		
		public Alerte(Ville.Activite e, float noteMin){
			this.activite = e;
			this.noteMin = noteMin;
			
		}
		
		public Alerte(Ville.Activite e, float noteMin, float noteMax){
			this.activite = e;
			this.noteMin = noteMin;
			this.noteMax = noteMax;
		}

		public float getNoteMin() {
			return noteMin;
		}

		public void setNoteMin(float noteMin) {
			this.noteMin = noteMin;
		}

		public float getNoteMax() {
			return noteMax;
		}

		public void setNoteMax(float noteMax) {
			this.noteMax = noteMax;
		}

		protected boolean isNotifiee() {
			return notifiee;
		}

		protected void setNotifiee(boolean notifiee) {
			this.notifiee = notifiee;
		}

		public synchronized boolean alerteActivee() {
			float moy = activite.getMoyenne();
			if (moy < noteMin && moy < noteMax ){
				return true;
			}
			return false;
		}
		
		public synchronized String toString() {
			String res = ">>> ALERTE : noteMin : " + this.noteMin + "    noteMax : " + this.noteMax + "    " + this.activite.toString() ;
			return res;
		}
	}
	
}
