package Affichage;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Font;
import java.rmi.RMISecurityManager;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;


public class AffichageSAR extends JFrame {
	CardLayout cl = new CardLayout();
	JPanel containerGlobal = new JPanel();
	
	public AffichageSAR(String adresse, String objet, String port){
		JFrame frame = new JFrame();
		frame.setTitle("Producteur/Consommateur SAR");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setSize(400, 250);
		
		containerGlobal.setLayout(cl);
		PanelAccueil pa = new PanelAccueil(cl, containerGlobal, adresse, objet, port);
		containerGlobal.add(pa, "Accueil");
		
		frame.setContentPane(containerGlobal);
		frame.setVisible(true);
	}
	

	public static void main(String[] args) {
		if(System.getSecurityManager() == null){
			System.setSecurityManager(new RMISecurityManager());
		}
		String adresse = JOptionPane.showInputDialog(null, "Veuillez donner l'adresse du serveur", "Bienvenue !", JOptionPane.QUESTION_MESSAGE);
		if(adresse == null){
			System.exit(0);
		}else if("".equals(adresse)){
			adresse = "localhost";
		}
		String port = JOptionPane.showInputDialog(null, "Veuillez donner le port sur lequel se connecter", "Bienvenue !", JOptionPane.QUESTION_MESSAGE);
		if(port == null){
			System.exit(0);
		}else if("".equals(port)){
			port = "1099";
		}
		String objet = JOptionPane.showInputDialog(null, "Quel objet distant voulez-vous atteindre ?", "Bienvenue !", JOptionPane.QUESTION_MESSAGE);
		if(objet == null){
			System.exit(0);
		}
		else if("".equals(objet)){
			objet = "Tii";
		}
		
		AffichageSAR as = new AffichageSAR(adresse, objet, port);
	}
	
	
}
