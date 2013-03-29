package Serveur;

import java.rmi.*;
import java.rmi.server.*;
import java.util.*;


public class TamponInterfaceImpl extends UnicastRemoteObject implements TamponInterface{
	Message[] tampon;
	LinkedList<Message> fileAttente; // FIFO
	int taille, in, out, nbmess, nbmessReserves;


	public TamponInterfaceImpl(int taille) throws RemoteException{
		this.taille = taille;
		tampon = new Message[taille];
		fileAttente = new LinkedList<Message>();
		in = 0;
		out = 0;
		nbmess = 0;
		nbmessReserves = 0;
	}


	public String extraire() throws RemoteException{
		synchronized(tampon){
			if(nbmess > 0){
				Message m = tampon[out]; 
				out = (out + 1) % taille;
				nbmess--;
				System.out.println ("*** Consommateur extrait le message de " + m.getIdClient() +  "--> nouvelle taille = " + nbmess);
				return m.getMessage();
			}
			return null;
		}
	}


	public boolean demandeInsertion(long c) throws RemoteException{
		synchronized(tampon){
			synchronized (fileAttente){
				if((nbmess + nbmessReserves) == taille && !fileAttente.isEmpty()){
					Iterator<Message> it = fileAttente.iterator();
					while(it.hasNext()){
						if(it.next().getFin().before(new Date(System.currentTimeMillis()))){
							fileAttente.removeFirst();
							nbmessReserves--;
							System.out.println ("*** Serveur libere une place pour " + c);
							break;
						}
					}
				}

				if((nbmess + nbmessReserves) < taille){
					Message m = new Message(c);
					fileAttente.add(m);
					nbmessReserves++;
					System.out.println ("*** Serveur reserve une place pour " + c);
					return true;
				}
				return false;
			}
		}
	}	

	public boolean inserer(long idClient, String message) throws RemoteException{
		synchronized(tampon){
			synchronized (fileAttente){
				if(!fileAttente.isEmpty()){
					Iterator<Message> it = fileAttente.iterator();
					Message m;
					while(it.hasNext()){
						m = it.next();
						if(m.getIdClient() == idClient){
							tampon[in] = m;
							it.remove();
							m.setMessage(message);
							in = (in + 1) % taille;
							nbmess++;
							nbmessReserves--;
							System.out.println ("*** Serveur insere un message pour " + idClient);
							return true;
						}
					}
				}
				return false;
			}
		}
	}


	public boolean retirerReservation(long id) throws RemoteException{
		synchronized (fileAttente){
			if(!fileAttente.isEmpty()){
				Iterator<Message> it = fileAttente.iterator();
				Message m;
				while(it.hasNext()){
					m = it.next();
					if(m.getIdClient() == id){
						it.remove();
						nbmessReserves--;
						System.out.println ("*** Serveur annule la reservation pour " + id);
						return true;
					}
				}
			}
			return false;
		}
	}
}
