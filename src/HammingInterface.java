import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.ArrayList;
import Hamming.*;

public class HammingInterface extends JFrame {
	
	JButton code, decipher, quit;
	JTextField txtMot;
	
	public HammingInterface() {
		super("Hamming");
		
		// Partie écriture
		JPanel panEdit = new JPanel(new FlowLayout());
		JLabel lblEdit = new JLabel("Mot : ");	panEdit.add(lblEdit);
		txtMot = new JTextField(10);	panEdit.add(txtMot);
		
		// Partie Boutons
		JPanel panBtn = new JPanel(new FlowLayout());
		code = new JButton("Coder");		panBtn.add(code);
		decipher = new JButton("Décoder");	panBtn.add(decipher);
		quit = new JButton("Quitter");		panBtn.add(quit);
		
		quit.addActionListener(e -> System.exit(0));
		code.addActionListener(new Encode());
		decipher.addActionListener(new Decipher());
		
		// Réglages
		add(panBtn, BorderLayout.SOUTH);
		add(panEdit);
		
		pack();
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	public void initDialEncode(String message) {
		JDialog dial = new JDialog(this, "Résultat");
		dial.setContentPane(new JPanel(new GridLayout(2, 1)));
		
		JLabel rewind = new JLabel("Le message d'origine est : " + message);
		JLabel result = new JLabel("Le message codé (bit de controle:message codé) : " + Hamming.encodeHamming(message));
		
		dial.add(rewind);
		dial.add(result);
		
		dial.setLocationRelativeTo(this);
		dial.pack();
		dial.setVisible(true);
	}
	
	public void initDialDecipher(String message) {
		JDialog dial = new JDialog(this, "Résultat");
		JPanel pan = new JPanel(new GridLayout(2, 1));
		
		ArrayList<Integer> posError = Hamming.getErrors(message);
		
		StringBuilder supp;
		if(posError.isEmpty()) {
			supp = new StringBuilder("Aucune erreur dans ce mot de Hamming !");
		}
		else {
			supp = new StringBuilder("Erreur(s) possible(s) détecté(s) au(x) bit(s) : ");
			for (Integer integer : posError) {
				supp.append(integer).append(" ");
			}
		}
		
		JLabel rewind = new JLabel("Le mot de Hamming est : " + message);
		JLabel result = new JLabel(supp.toString());
		
		pan.add(rewind);
		pan.add(result);
	
		dial.setContentPane(pan);
		dial.setLocationRelativeTo(this);
		dial.pack();
		dial.setVisible(true);
	}
	
	class Encode implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if(txtMot.getText().matches("[01]+")) {
				initDialEncode(txtMot.getText());
			}
			else {
				JOptionPane.showMessageDialog(null, "Invalid bits input", "Error", JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}
	
	class Decipher implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if(txtMot.getText().matches("[01]+")) {
				initDialDecipher(txtMot.getText());
			}
			else {
				JOptionPane.showMessageDialog(null, "Invalid bits input", "Error", JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(HammingInterface::new);
	}
}