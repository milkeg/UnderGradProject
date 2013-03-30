package Serveur;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Iterator;


public class Serveur extends Thread {
	protected static ArrayList<Ville> villes;
	private static ArrayList<ServeurClient> clients;
	private static final int port = 8080;
	private ServerSocket srv;
	private static boolean edited = false;

	public Serveur() {
		try {
			srv = new ServerSocket(port);
			villes = new ArrayList<Ville>();
			clients = new ArrayList<ServeurClient>();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Serveur(int portNumber) {
		try {
			srv = new ServerSocket(portNumber);
			villes = new ArrayList<Ville>();
			clients = new ArrayList<ServeurClient>();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean add(Ville e) {
		return villes.add(e);
	}

	static public String villesToString() {
		String res = "";
		for(Ville villeCourante : villes) {
			res += villeCourante.toString() + "\n";
		}
		return res;
	}

	public static boolean add(ServeurClient e) {
		return clients.add(e);
	}

	public static ServeurClient isClient(String userName) {

		ServeurClient clientCourant;
		Iterator<ServeurClient> it = clients.iterator();
		while(it.hasNext()){
			clientCourant = it.next();
			if (clientCourant.getNomClient().equals(userName)){
				return clientCourant;
			}
		}
		return null;
	}

	public class ServeurAlerte extends Thread {
		private ArrayList<Connexion> clientsConnectes;

		public ServeurAlerte() {
			clientsConnectes = new ArrayList<Connexion>();
		}

		public void add(Connexion c){
			synchronized(clientsConnectes) {
				clientsConnectes.add(c);
			}
		}

		public boolean remove(Connexion o) {
			return clientsConnectes.remove(o);				
		}

		public void run() {
			ArrayList<Connexion> clientsDeconnectes = new ArrayList<Connexion>();
			while(true) {
				synchronized(clientsConnectes) {
					for(Connexion e : clientsConnectes){
						if(e.isClosed()) {
							clientsDeconnectes.add(e);
						} else {
							ServeurClient cl = e.clientCourant;
							if(cl != null && !cl.alertesActivees().isEmpty()) {
								e.writeUDP(cl.alertesActiveesToString());
							}
						}
					}
					for(Connexion e : clientsDeconnectes){
						remove(e);
					}
					clientsDeconnectes.clear();
				}
			}
		}
	}

	public void run() {
		try {
			Connexion c;
			ServeurAlerte srvAlerte = new ServeurAlerte();
			srvAlerte.start();
			while(true) {
				c = new Connexion(srv.accept());
				c.start();
				srvAlerte.add(c);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Ville paris = new Ville("Paris");
		paris.addActivite("Natation", 2012, 0, 5, 20, 00, 00, 2012, 1, 5, 22, 00, 00, "Sport", "Info.");		
		paris.addActivite("Coiffure", 2012, 1, 5, 10, 00, 00, 2012, 1, 5, 12, 00, 00, "Bien-etre", "Info.");
		paris.addActivite("Restaurant", 2012, 2, 5, 20, 00, 00, 2012, 1, 5, 22, 00, 00, "Restauration", "Info.");
		paris.addActivite("Soin visage", 2012, 3, 5, 18, 00, 00, 2012, 1, 5, 20, 00, 00, "Bien-etre", "Info.");
		Ville nantes = new Ville("Nantes");
		nantes.addActivite("Modelage", 2012, 1, 5, 18, 00, 00, 2012, 1, 5, 20, 00, 00, "Sport", "Info.");		
		nantes.addActivite("Soin visage", 2012, 4, 5, 16, 00, 00, 2012, 1, 5, 18, 00, 00, "Bien-etre", "Info.");
		nantes.addActivite("Restaurant", 2012, 3, 5, 20, 00, 00, 2012, 1, 5, 22, 00, 00, "Restauration", "Info.");
		nantes.addActivite("Soin visage", 2012, 2, 5, 18, 00, 00, 2012, 1, 5, 20, 00, 00, "Bien-etre", "Info.");
		Serveur srv = new Serveur();
		srv.add(paris);
		srv.add(nantes);
		srv.run();
	}
}
