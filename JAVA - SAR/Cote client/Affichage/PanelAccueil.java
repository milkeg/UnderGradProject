package Affichage;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Client.Consommateur;
import Client.Producteur;


public class PanelAccueil extends JPanel{
	private JButton btnProd = new JButton("Producteur");
	private JButton btnConso = new JButton("Consommateur");
	private JPanel containerGlobal;
	private JPanel container = new JPanel();
	private CardLayout cartes;
	private String adresse;
	private String objet;
	private String port;
	
	public PanelAccueil(CardLayout cartes, JPanel containerGlobal, String adresse, String objet, String port){
		
		this.containerGlobal = containerGlobal;
		this.cartes = cartes;
		this.adresse = adresse;
		this.objet = objet;
		this.port = port;
		
		this.setLayout(new BorderLayout());
		
		btnProd.setPreferredSize(new Dimension(120,40));
		btnProd.addActionListener(new BtnProdListener());
		btnConso.setPreferredSize(new Dimension(120,40));
		btnConso.addActionListener(new BtnConsoListener());
		container.add(btnProd);
		container.add(btnConso);
		JLabel message = new JLabel("<html><u>Bienvenue sur l'application</u></html>");
		JLabel message2 = new JLabel("Qui etes-vous ?");
		message.setFont(new Font("Arial", Font.PLAIN, 30));
		message.setHorizontalAlignment(JLabel.CENTER);
		message2.setFont(new Font("Arial", Font.PLAIN, 20));
		message2.setHorizontalAlignment(JLabel.CENTER);
		this.add(container, BorderLayout.SOUTH);
		this.add(message2, BorderLayout.CENTER);
		this.add(message, BorderLayout.NORTH);
	}
	
	class BtnProdListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			Producteur p = new Producteur("rmi://"+adresse+":"+port+"/"+objet);
			containerGlobal.add(new PanelProd(p), "Fenetre Prod");
			cartes.show(containerGlobal, "Fenetre Prod");
		}
	}
	
	class BtnConsoListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			Consommateur c = new Consommateur("rmi://"+adresse+":"+port+"/"+objet);
			containerGlobal.add(new PanelConso(c), "Fenetre Conso");
			cartes.show(containerGlobal, "Fenetre Conso");
		}
	}
	
	
}
