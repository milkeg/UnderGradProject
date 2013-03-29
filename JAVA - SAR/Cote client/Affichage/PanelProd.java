package Affichage;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import Client.Producteur;

public class PanelProd extends JPanel{

	private JButton btnProduire = new JButton("PRODUIRE");
	private Producteur p;

	public PanelProd(Producteur p) {
		this.p = p;

		btnProduire.setPreferredSize(new Dimension(120,40));
		btnProduire.addActionListener(new BtnProdListener());

		this.add(btnProduire);
	}

	public class BtnProdListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			boolean tmp = p.demandeInserer();
			if(tmp){
				String message = JOptionPane.showInputDialog(((JButton)e.getSource()).getParent(), "<html>Vous avez le droit de produire !<br/> Quel message voulez-vous inserer?</html>", "Insertion", JOptionPane.QUESTION_MESSAGE);
				if(message != null){
					if("".equals(message)){
						JOptionPane.showMessageDialog(((JButton)e.getSource()).getParent(), "<html>Vous n'avez pas rentre de message <br/> Veuillez recommencer</html>", "Pas de message", JOptionPane.ERROR_MESSAGE);
						p.retirerReservation();
					}else if(p.produire(message)){
						JOptionPane.showMessageDialog(((JButton)e.getSource()).getParent(), "Votre message a bien ete insere !", "Succes", JOptionPane.INFORMATION_MESSAGE);
					}else{
						JOptionPane.showMessageDialog(((JButton)e.getSource()).getParent(), "<html>Vous avez ete trop long... <br/> Votre message n'a pas pu etre insere dans le tampon...</html>", "Echec", JOptionPane.ERROR_MESSAGE);
					}
				}
			}else{
				JOptionPane.showMessageDialog(((JButton)e.getSource()).getParent(), "<html>Le tampon est plein !<br/> Vous ne pouvez plus produire. Veuillez essayer plus tard !</html>", "Echec", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}
