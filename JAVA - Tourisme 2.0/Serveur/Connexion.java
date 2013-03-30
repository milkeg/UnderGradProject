package Serveur;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.Date;
import java.util.Iterator;

import Serveur.Ville.Activite;

public class Connexion extends Thread {
	private Socket s;
	private DatagramSocket socketUDP;
	private PrintWriter out;
	private BufferedReader in;
	private DatagramPacket bufferUDP;
	private static int port = 8181; 
	private String userName;
	protected ServeurClient clientCourant;

	// 2 semaines = 1 209 600 000 millisecondes
	// On peut commenter jusqu'à deux semaines apres la date de fi nd'une activité
	static long DELAI = 1209600000;

	public Connexion() {
		super();
	}

	public Connexion(Socket s) {
		this.s = s;
	}

	public void openConnection() {
		try {
			in = new BufferedReader(new InputStreamReader(s.getInputStream()));
			out = new PrintWriter(s.getOutputStream(), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void closeConnection() {
		try {
			closeSocketUDP();
			in.close();
			out.close();
			s.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void closeSocketUDP() {
		this.socketUDP.disconnect();
		this.socketUDP.close();
	}

	public void write(String s) {
		out.println(s);
		out.println("<STOP>");
		out.flush();
	}

	public void writeNoSTOP(String s) {
		out.println(s);
		out.flush();
	}

	public void writeUDP(String s) {
		InetAddress ip = getIP();
		int portClientCourant  = clientCourant.getPortClient();
		bufferUDP = new DatagramPacket(s.getBytes(), s.length(), ip, portClientCourant);
		try {
			socketUDP.send(bufferUDP);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean isClosed(){
		return s.isClosed();
	}

	public String read() {
		try {
			String reponse = "";
			while(reponse.isEmpty()) {
				while(!in.ready()) {
					yield();
				}
				reponse = in.readLine();
			}
			return reponse;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void sendPort(int port) {
		writeNoSTOP("<PORT>" + port);
	}

	public String nomUtilisateur() {
		write("Saisissez votre nom d'utilisateur :\n");
		return read();
	}

	public InetAddress getIP() {
		return s.getInetAddress();
	}

	public Ville selectionnerVille() {
		String res = "";
		int i = 1;
		Iterator<Ville> it = Serveur.villes.iterator();

		res += "Selectionnez la ville parmi :\n";
		while (it.hasNext() ){
			res += i+". " + it.next().toString() + "\n";
			i++;
		}

		write(res);

		String choix = read();
		it = Serveur.villes.iterator();
		Ville villeCourante;

		int j = 1;
		while(it.hasNext()){
			villeCourante = it.next();
			if(villeCourante.getNom().equalsIgnoreCase(choix)){
				return villeCourante; 
			}
			else {
				// Si la personne n'a pas rentré le nom de la ville
				// on verifie s'il a saisi le nombre correspodant
				// Dans le cas ou il n'a pas rentré un nombre,
				// on leve une exception qui nous permet de sortir de la condition
				try{
					int choix_int = (Integer.parseInt(choix));
					if(choix_int == j){
						return villeCourante; 
					}
				} catch (NumberFormatException e) {
					return null;		
				}
				j++;
			}
		}
		return null;
	}

	public Activite selectionnerActivite(Ville villeCourante) {

		Iterator<Activite> it = villeCourante.activitesToIterator();
		int i = 1;
		String res = "";

		res += "Selectionnez votre activite parmi :\n";
		while (it.hasNext() ){
			res += i+". " + it.next().toString() + "\n";
			i++;
		}

		write(res);

		String choix = read();
		it = villeCourante.activitesToIterator();
		Activite activiteCourante;

		int j = 1;
		while(it.hasNext()){
			activiteCourante = it.next();
			if(activiteCourante.getNom().equalsIgnoreCase(choix) ){
				return activiteCourante; 
			}
			else {
				// Si la personne n'a pas rentré le nom de l'activité
				// On verifie s'il a saisi le nombre correspodant
				// Dans le cas ou il n'a pas rentré un nombre,
				// on leve une exception qui nous permet de sortir de la condition
				try{
					int choix_int = (Integer.parseInt(choix));
					if(choix_int == j){
						return activiteCourante; 
					}
				} catch (NumberFormatException e) {
					return null;		
				}
				j++;
			}
		}
		return null;		

	}

	public void listeVille(){
		String res = "";
		int i = 1;
		Iterator<Ville> it = Serveur.villes.iterator();

		res += "La liste de villes est :\n";
		while (it.hasNext() ){
			res += i+". " + it.next().toString() + "\n";
			i++;
		}

		writeNoSTOP(res);

	}

	public void listeActivite(Ville villeCourante){
		Iterator<Activite> it = villeCourante.activitesToIterator();
		int i = 0;
		String res = "";

		res += "Voici la liste des activites :\n";
		while (it.hasNext() ){
			res += i+". " + it.next().toString() + "\n";
			i++;
		}

		writeNoSTOP(res);
	}

	public boolean verifierNote(float note){
		if(note <= 20 && note >= 0){
			return true;
		}
		else {
			return false;
		}
	}

	public boolean ajouterNote(Ville ville, Activite activite, Float note){
		return ville.addActiviteNote(activite, userName, note);
	}

	public boolean ajouterCommentaire(Ville ville, Activite activite, String commentaire){
		return ville.addActiviteCommentaire(activite, userName, commentaire);
	}

	public boolean dejaCommente(Ville ville, Activite activite){
		String commenaire;
		if((commenaire = ville.activiteDejaCommente(activite, userName)) != null){
			write("Vous avez deja commenté cette activité : " + commenaire);

			// Modification et desactivation pour les scenarios
			write("Cette note sera remplacée par celle qui vous allez saisir !");
			/**
			write("Voulez-vous remplacer votre commententaire ? (Oui/Non) ou (0/1)");
			String choix = read();
			if(choix.equalsIgnoreCase("non") || choix.equalsIgnoreCase("2")){
				return true;
			}
			else {
				return false;
			}
			 **/
		}
		return false;
	}

	public void dejaNote(Ville ville, Activite activite){
		Float note;
		// Desactivé pour les scenarios
		//String choix;
		if(((note = ville.activiteDejaNote(activite, userName))) != null){			
			write("Vous avez deja donné à cette activité une note de : " + note);

			// Modification et desactivation pour les scenarios
			write("Cette note sera remplacée par celle qui vous allez saisir !");
			/**
			write("Si vous ne voulez pas remplacer votre commentaire taper : exit");
			choix = read();
			if(choix.equalsIgnoreCase("non") || choix.equalsIgnoreCase("2")){
				return true;
			}
			else if(choix.equalsIgnoreCase("oui") || choix.equalsIgnoreCase("1")){
				return false;
			}
			 **/
		}
	}

	public void noterCommenter(Ville villeSelectionnee, Activite activiteSelectionnee){
		String commentaire;

		// Si la note est present mais que l'utilisateur veut l ecraser ou si la note n est pas presente
		dejaNote(villeSelectionnee, activiteSelectionnee);

		ajouterNote(villeSelectionnee, activiteSelectionnee, saisieNote("saisie"));

		// Si le commentaire est present mais que l'utilisateur veut l ecraser ou si le commentaire n est pas presente
		if(!dejaCommente(villeSelectionnee, activiteSelectionnee)){
			write("Saississez votre commentaire :\n");
			commentaire = read();
			ajouterCommentaire(villeSelectionnee, activiteSelectionnee, commentaire);
		}
	}

	public void commenter(Ville villeSelectionnee, Activite activiteSelectionnee){
		String commentaire;
		// Si le commentaire est present mais que l'utilisateur veut l ecraser ou si le commentaire n est pas presente
		dejaCommente(villeSelectionnee, activiteSelectionnee);
		write("Saississez votre commentaire :\n");
		commentaire = read();
		ajouterCommentaire(villeSelectionnee, activiteSelectionnee, commentaire);	
	}

	public void noter(Ville villeSelectionnee, Activite activiteSelectionnee){
		// Si la note est present mais que l'utilisateur veut l ecraser ou si la note n est pas presente
		dejaNote(villeSelectionnee, activiteSelectionnee);

		ajouterNote(villeSelectionnee, activiteSelectionnee, saisieNote("saisie"));
	}

	public float saisieNote(String mot){
		boolean correctNote = false;
		String noteStr;
		Float note;

		while(!correctNote){
			write("Saississez la note "+mot+" :\n");
			noteStr = read();
			try {
				note = Float.parseFloat(noteStr);
				if (!verifierNote(note)){
					write("La note "+mot+" n'est pas comprise entre 0 et 20 !\n");
					continue;
				}
				else {
					correctNote = true;
					return note;
				}
			} catch (NumberFormatException e) {
				write("Vous n'avez pas saisi un nombre !\n");
			}	
		}
		// Return pour ne pas avoir un avertissement
		// jamais utilisé car on ne sort pas de la boucle sans renvoyer une float
		return -1;
	}

	public void ajouterAlerte(Activite activiteSelectionnee){
		String choix;
		boolean correct = false;

		while (!correct){
			String res = "";
			res += "Souhaitez vous rentrer :\n";
			res += "1. Note minimum.\n";
			res += "2. Note minimum et maximum.\n";
			write (res);
			choix = read();
			if(choix.equalsIgnoreCase("1") || choix.equalsIgnoreCase("NoteMin")){
				clientCourant.abonnerAlerte(clientCourant. new Alerte(activiteSelectionnee, saisieNote("minimum")));
				correct = true;
			}
			else if(choix.equalsIgnoreCase("2") || choix.equalsIgnoreCase("NoteMinMax")) {
				// WHILE > TRY > IF
				clientCourant.abonnerAlerte(clientCourant. new Alerte(activiteSelectionnee, saisieNote("minimum"), saisieNote("maximum")));
				correct = true;
			}
		}
	}

	public void voirAppreciation(Activite activiteSelectionnee){
		writeNoSTOP(activiteSelectionnee.getAppreciation());
	}

	/** Méthode de communication globale **/
	public void communication() {
		userName = nomUtilisateur();
		ServeurClient client = Serveur.isClient(userName);

		// IMPORTANT ******
		this.clientCourant = client;
		// *******


		if(client == null) {
			this.clientCourant = new ServeurClient(userName, getIP(), port);
			Serveur.add(this.clientCourant);
			try {
				socketUDP = new DatagramSocket();
			} catch (SocketException e) {
				e.printStackTrace();
			}
			sendPort(port++);
		} else {
			client.setAdresseIP(getIP());
			client.setPortClient(port);
			try {
				socketUDP = new DatagramSocket();
			} catch (SocketException e) {
				e.printStackTrace();
			}
			sendPort(port++);
		}


		/** MENU **/
		String menu = "", message = "";
		boolean exit = false;
		do {

			menu = "";
			menu += "Que souhaitez vous faire :\n";
			menu += "1. Ajouter une note et/ou une activité et/ou une alerte.\n";
			menu += "2. Affiche la liste des villes.\n";
			menu += "3. Affiche les activités d'une ville.\n";
			menu += "4. Quitter.\n";
			write(menu);

			String choix = read();

			if (choix.equalsIgnoreCase("1") || choix.equalsIgnoreCase("NoteEtCommentaire")){

				Ville villeSelectionnee = null;
				while(villeSelectionnee == null){
					villeSelectionnee = selectionnerVille();
				}

				Activite activiteSelectionnee = null;
				while(activiteSelectionnee == null){
					activiteSelectionnee = selectionnerActivite(villeSelectionnee);
				}

				menu = "";
				menu += "Que souhaitez vous faire.\n";
				menu += "1. Ajouter ou modifier une note.\n";
				menu += "2. Ajouter ou modifier un commentaire.\n";
				menu += "3. Ajouter ou modifier une note et un commentaire.\n";
				menu += "4. Ajouter une alerte.\n";
				menu += "5. Voir toutes les appréciations.\n";
				write(menu);

				choix = read();

				// 2 semaines = 1 209 600 000 millisecondes
				// On peut commenter jusqu'à deux semaines apres la date de fi nd'une activité
				if (activiteSelectionnee.getFin().before(new Date(System.currentTimeMillis() + DELAI))){
					message = ""; 
					message += "L'activité est terminée depuis plus de deux semaines !\n";
					message += "Il est donc impossible de la noter, commenter ou mettre une alerte...!\n";
					write(message);
				}
				else {
					if (choix.equalsIgnoreCase("1") || choix.equalsIgnoreCase("Note")){
						noter(villeSelectionnee, activiteSelectionnee);
					} else if (choix.equalsIgnoreCase("2") || choix.equalsIgnoreCase("Commentaire")){
						commenter(villeSelectionnee, activiteSelectionnee);
					} else if (choix.equalsIgnoreCase("3") || choix.equalsIgnoreCase("NoteEtCommentaire")){	
						noterCommenter(villeSelectionnee, activiteSelectionnee);
					} else if (choix.equalsIgnoreCase("4") || choix.equalsIgnoreCase("Ajouter")){	
						ajouterAlerte(activiteSelectionnee);
					} else if (choix.equalsIgnoreCase("5") || choix.equalsIgnoreCase("VoirTous")){	
						voirAppreciation(activiteSelectionnee);
					}
				}
			} else if (choix.equalsIgnoreCase("2") || choix.equalsIgnoreCase("Ville")){
				listeVille();
			} else if (choix.equalsIgnoreCase("3") || choix.equalsIgnoreCase("Activite")){
				Ville villeSelectionnee = null;
				while(villeSelectionnee == null){
					villeSelectionnee = selectionnerVille();
				}
				listeActivite(villeSelectionnee);

			} else if (choix.equalsIgnoreCase("4") || choix.equalsIgnoreCase("exit")){
				exit = true;
				write("Exit");
			}
		} while(!exit);
	}

	public void run() {
		openConnection();
		communication();
		closeConnection();
	}


}