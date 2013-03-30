package Client;

import java.io.*;
import java.net.*;

import Client.Connexion.UDPReceive;

public class Client extends Thread {

	Connexion co;

	public Client (){
		try {
			this.co = new Connexion(new Socket("localhost", 8080));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String readClavier() {
		String ligne = null;
		InputStreamReader buffer = new InputStreamReader(System.in);
		BufferedReader clavier = new BufferedReader(buffer);
		try {
			if(clavier.ready()) ligne = clavier.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ligne;
	}

	public void run() {
		int etat = 0;
		boolean exit = false;
		Integer aleatoire;
		String login, aleaStr;

		boolean scenario = false;

		co.openConnection();
		//String lectureClavier = null;
		String lectureMessages = null;
		String lectureClavier = null;
		UDPReceive udp = null;
		while(!exit) {
			lectureClavier = null;
			lectureMessages = null;
			lectureMessages = co.read();
			if(lectureMessages != null && !lectureMessages.isEmpty()) {
				if(lectureMessages.startsWith("<PORT>")) {
					String[] tabSplit = lectureMessages.split(">");
					int portSplit = Integer.parseInt(tabSplit[1]);
					System.out.println("Le port est : " + portSplit);
					udp = co.new UDPReceive(portSplit);
					udp.start();
				} else if (lectureMessages.equalsIgnoreCase("Exit")){
					co.write("Exit");
					//udp.close();
					co.closeConnection();
					udp.interrupt();
					//System.exit(0);
					exit = true;
				} else {
					if (!lectureMessages.equalsIgnoreCase("<STOP>")){
						System.out.println(lectureMessages);		
						continue;
					}

					if (scenario){

						/**** SCENARIO ****/
						/**
					Liste des états :
					0 : Login
					1 : Menu Principal [1-4]
					2 : Saisir Ville [1-2]
					3 : Saisir Activité [1-4]
					4 : Menu Action [1-5]
					5 : Saisir Commentaire
					6 : Saisir Note 
					7 : Saisir Note pour Note & Commentaire
					8 : Saisir Commentaire pour Note & Commentaire
					9 : Menu Alerte [1-2]
					10 : Saisir Note Minimum
					11 : Saisir Note Minimum pour Note Mininim et Note Maximum
					12 : Saisir Note Maximum pour Note Mininim et Note Maximum
					13 : Affichage des Appréciations
					14 : Affichage des villes
					15 : Saisir Ville [1-2] pour Affichage des activités
					16 : Quitter


						 **/

						switch(etat){
						case 0 : 
							// Login
							login = "Login : " + System.currentTimeMillis();
							System.out.println(login);
							co.write(login);
							etat = 1;
							break;

						case 1 :
							// Menu Principal
							// Compris entre 1 et 4
							aleatoire = (int) ((int) 1 + Math.random() * 4);
							System.out.println(aleatoire);
							co.write(aleatoire.toString());
							if(aleatoire == 1){
								etat = 2;
							} else if (aleatoire == 2){
								// Retour sur le menu initiale
							} else if (aleatoire == 3){
								etat = 15;
							} else if (aleatoire == 4){
								// Quitter
								etat = 16;
							}
							break;

						case 2 :
							// Menu Choix Ville
							// Compris entre 1 et 2
							aleatoire = (int) (1 + Math.random() * 2);
							System.out.println(aleatoire);
							co.write(aleatoire.toString());
							etat = 3;
							break;

						case 3 : 
							// Menu Choix Activité
							// Compris entre 1 et 4
							aleatoire = (int) (1 + Math.random() * 4);
							System.out.println(aleatoire);
							co.write(aleatoire.toString());
							etat = 4;
							break;

						case 4 :
							// Menu Choix Noter/Commenter/NoterEtCommenter/Alerte/AfficherAppreciation
							// Compris entre 1 et 5
							aleatoire = (int) (1 + Math.random() * 5);
							System.out.println(aleatoire);
							co.write(aleatoire.toString());
							if(aleatoire == 1){
								etat = 6;
							} else if (aleatoire == 2){
								etat = 5;
							} else if (aleatoire == 3){
								etat = 7;
							} else if (aleatoire == 4){
								etat = 9;
							} else if (aleatoire == 5){
								etat = 13;
							}
							break;

						case 5 :
							aleaStr = "Commentaire : " + System.currentTimeMillis();
							System.out.println(aleaStr);
							co.write(aleaStr);
							etat = 1;
							break;

						case 6 :
							aleatoire = (int) (Math.random() * 20);
							System.out.println(aleatoire);
							co.write(aleatoire.toString());
							etat = 1;
							break;

						case 7 :
							aleatoire = (int) (Math.random() * 20);
							System.out.println(aleatoire);
							co.write(aleatoire.toString());
							etat = 8;
							break;

						case 8 :
							aleaStr = "Commentaire : " + System.currentTimeMillis();
							System.out.println(aleaStr);
							co.write(aleaStr);
							etat = 1;
							break;

						case 9 :
							aleatoire = (int) ( 1 + Math.random() * 2);
							System.out.println(aleatoire);
							co.write(aleatoire.toString());
							if (aleatoire == 1){
								etat = 10;
							} else if (aleatoire == 2){
								etat = 11;
							}
							break;

						case 10 :
							// Insérer note min
							// Compris entre 0 et 20
							aleatoire = (int) (Math.random() * 20);
							System.out.println(aleatoire);
							co.write(aleatoire.toString());
							etat = 1;
							break;

						case 11 :
							// Insérer note min
							// Compris entre 0 et 20
							aleatoire = (int) (Math.random() * 20);
							System.out.println(aleatoire);
							co.write(aleatoire.toString());
							etat = 12;
							break;

						case 12 :
							// Insérer note max
							// Compris entre 0 et 20
							aleatoire = (int) (Math.random() * 20);
							System.out.println(aleatoire);
							co.write(aleatoire.toString());
							etat = 1;
							break;

						case 13 :
							// Affichage uniquement
							etat = 1;
							break;

						case 14 :
							// Affichage uniquement
							etat = 1;
							break;

						case 15 :
							// Menu Choix Ville
							// Compris entre 1 et 2
							aleatoire = (int) (1 + Math.random() * 2);
							System.out.println(aleatoire);
							co.write(aleatoire.toString());
							etat = 14;
							break;

						case 16 :
							// Affichage uniquement
							co.write("Exit");
							//udp.close();
							//co.closeConnection();
							exit = true;
							break;
						}

						/**** SCENARIO ****/

					}
				}
			}

			if (!scenario){


				lectureClavier = readClavier();


				if(lectureClavier != null && !lectureClavier.isEmpty() && !lectureClavier.equals("Quit")) {
					co.write(lectureClavier);
				}
				/*
				if(lectureClavier != null && lectureClavier.equals("Exit")) {
					co.write("Exit");
					//udp.close();
					co.closeConnection();
					udp.interrupt();
					//System.exit(0);
					exit = true;
				}
				 */
			}
		}
		co.closeConnection();
		udp.interrupt();
		System.exit(0);
	}

	public static void main (String args[]){
		Client c = new Client();
		c.start();
	}

}
