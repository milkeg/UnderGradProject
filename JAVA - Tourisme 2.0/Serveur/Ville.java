package Serveur;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;


public class Ville {
	private String nom;
	private ArrayList<Activite> activites;

	public Ville (String nom){
		this.setNom(nom);
		activites = new ArrayList<Activite>();
	}

	public void addActivite(String nom, int yearDebut, int monthDebut, int dateDebut, int hourOfDayDebut,
			int minuteDebut, int secondDebut, int yearFin, int monthFin, int dateFin, int hourOfDayFin,
			int minuteFin, int secondFin, String type, String informations){
		activites.add(new Activite(nom, yearDebut, monthDebut, dateDebut, hourOfDayDebut, minuteDebut, secondDebut, 
				yearFin, monthFin, dateFin, hourOfDayFin, minuteFin, secondFin, type, informations));
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public Activite getActiviteByName(String s) {
		for(Activite a : activites){
			s.equals(a.getNom());
			return a;
		}
		return null;
	}

	public String toString() {
		return nom;
	}

	public String activitesToString() {
		String res = "\nActivités :\n";
		for(Activite activiteCourante : activites) {
			res += activiteCourante.toString();
		}
		return res;
	}

	public Iterator<Activite> activitesToIterator() {
		return activites.iterator();
	}

	public boolean addActiviteNote(Activite activite, String nomClient, Float note){
		Iterator<Activite> it = activites.iterator();
		while(it.hasNext()){
			Activite activiteCourante = it.next(); 
			if (activiteCourante.equals(activite)){
				activiteCourante.ajouterNote(nomClient, note);
				return true;
			}
		}
		return false;
	}

	public boolean addActiviteCommentaire(Activite activite, String nomClient, String commentaire){
		Iterator<Activite> it = activites.iterator();
		while(it.hasNext()){
			Activite activiteCourante = it.next(); 
			if (activiteCourante.equals(activite)){
				activiteCourante.ajouterCommentaire(nomClient, commentaire);
				return true;
			}
		}
		return false;
	}

	public String activiteDejaCommente(Activite activite, String nomClient){
		Iterator<Activite> it = activites.iterator();
		while(it.hasNext()){
			Activite activiteCourante = it.next(); 
			if (activiteCourante.equals(activite)){
				if (activiteCourante.commentaires.containsKey(nomClient)){
					return activiteCourante.commentaires.get(nomClient);
				}
				else {
					return null;
				}
			}
		}
		return null;
	}

	public Float activiteDejaNote(Activite activite, String nomClient){
		Iterator<Activite> it = activites.iterator();
		while(it.hasNext()){
			Activite activiteCourante = it.next(); 
			if (activiteCourante.equals(activite)){
				if (activiteCourante.notes.containsKey(nomClient)){
					return activiteCourante.notes.get(nomClient);
				}
				else {
					return null;
				}
			}
		}
		return null;
	}

	public boolean verifierActivite (String nomActivite, float noteMin, float noteMax){

		Iterator<Activite> it = activites.iterator();
		while(it.hasNext()){
			Activite activiteCourante = it.next(); 
			if (activiteCourante.nom == nomActivite && activiteCourante.getMoyenne() > noteMin && activiteCourante.getMoyenne() < noteMax){
				return true;
			}
		}
		return false;

	}

	/*
	 * Classe Interne Activié : Celle-ci présente les avantages de ... 
	 * Une Activité est forcement lié a une ville
	 * Bémol : On ne peut faire une recherche des villes qui contiennent une activité 
	 * 		sans perdre enormément de temps
	 */
	public class Activite {

		private String nom;
		private Calendar debut;
		private Calendar fin;
		private String type;
		private String informations;
		private HashMap <String, String> commentaires;
		private HashMap <String, Float> notes;		

		/*
		 * On stockera <Nom_CLient, Commentaires> ou <Nom_Client, Notes>
		 * Afin de permettre la modification
		 * Logiquement, un même utilisateur ne peut mettre deux commentaires ou notes
		 */


		public Activite(String nom, int yearDebut, int monthDebut, int dateDebut, int hourOfDayDebut,
				int minuteDebut, int secondDebut, int yearFin, int monthFin, int dateFin, int hourOfDayFin,
				int minuteFin, int secondFin, String type, String informations) {
			this.setNom(nom);
			debut = Calendar.getInstance(new Locale("French"));
			fin = Calendar.getInstance(new Locale("French"));
			setDebut(yearDebut, monthDebut, dateDebut, hourOfDayDebut, minuteDebut, secondDebut);
			setFin(yearFin, monthFin, dateFin, hourOfDayFin, minuteFin, secondFin);
			this.setType(type);
			this.setInformations(informations);

			commentaires = new HashMap<String, String>();
			notes = new HashMap<String, Float>();
		}

		public String getNom() {
			return nom;
		}

		public void setNom(String nom) {
			this.nom = nom;
		}

		public final void setDebut(int year, int month, int date, int hourOfDay,
				int minute, int second) {
			debut.set(year, month, date, hourOfDay, minute, second);
		}

		public Calendar getDebut(){
			return debut;
		}

		public final void setFin(int year, int month, int date, int hourOfDay,
				int minute, int second) {
			fin.set(year, month, date, hourOfDay, minute, second);
		}

		public Calendar getFin(){
			return fin;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getInformations() {
			return informations;
		}

		public void setInformations(String informations) {
			this.informations = informations;
		}

		public float getMoyenne(){
			float moyenne = 0;
			int i = 0;
			if(!notes.isEmpty()) {
				for(Float noteCourante : notes.values()){
					i++;
					moyenne += noteCourante;
				}
				return moyenne/i;
			}
			return -1;
		}

		public String toString() {
			return "Nom : " + nom + "    Debut  : " + debut.getTime().toString() 
					+ "    Fin : " + fin.getTime().toString() 
					+ "	Type : " + type + "    Note : " + getMoyenne() + "\n";
		}

		public synchronized Float ajouterNote(String nomClient, Float value) {
			return notes.put(nomClient, value);
		}

		public synchronized String ajouterCommentaire(String nomClient, String value) {
			return commentaires.put(nomClient, value);
		}

		public String getAppreciation(){
			String res = toString() + "\n";
			res += "Note\t\t\tCommentaire";
			Set<String> noteCle = notes.keySet();
			Set<String> commentairesCle = commentaires.keySet();
			String cle;
			Iterator<String> it = noteCle.iterator();
			while(it.hasNext()){
				cle = it.next();
				if(notes.containsKey(cle)){
					res += notes.get(cle) + "\t\t\t";
				} else {
					res += "\t\t\t\t";
				}
				if(commentaires.containsKey(cle)){
					res += commentaires.get(cle) + "\n";
					commentairesCle.remove(cle);
				} else {
					res += "\n";
				}				
			}
			it = commentairesCle.iterator();
			while(it.hasNext()){
				cle = (String) it.next();
				res += "\t\t\t\t";
				res += commentaires.get(cle) + "\n";
			}
			return res;
		}


	}


}
