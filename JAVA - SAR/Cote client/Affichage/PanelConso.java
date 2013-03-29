package Affichage;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import Client.Consommateur;

public class PanelConso extends JPanel{

	private JButton btnConsommer = new JButton("CONSOMMER");
	private Consommateur c;
	
	public PanelConso(Consommateur c){
		this.c = c;
		
		btnConsommer.setPreferredSize(new Dimension(120,40));
		btnConsommer.addActionListener(new BtnConsoListener());
		
		this.add(btnConsommer);
	}
	
	public class BtnConsoListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			String message = c.consommer();
			if(message != null){
				JOptionPane.showMessageDialog(((JButton)e.getSource()).getParent(), message, "Votre message", JOptionPane.INFORMATION_MESSAGE);
			}else{
				JOptionPane.showMessageDialog(((JButton)e.getSource()).getParent(), "Il n'y a aucun message dans le tampon", "Pas de message", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}
