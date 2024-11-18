import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;

public class Tp3Etape4 extends JFrame implements ActionListener {
	private JTable tableau;
	private MonModeleDeTable modele;
	public static String selectedIntitule = "";

	public Tp3Etape4() {
		super();

		setTitle("JTable avec tous les sports");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		modele = new MonModeleDeTable(this);
		tableau = new JTable(modele);
		
		tableau.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				System.out.println(tableau.getValueAt(tableau.getSelectedRow(), 1));
				Tp3Etape4.selectedIntitule = (String) tableau.getValueAt(tableau.getSelectedRow(), 1);
				
			}
		});

	    // Boutons Ajouter et Supprimer
	    JButton btn = new JButton("Ajouter");
	    JButton btnSup = new JButton("Supprimer");

	    btn.addActionListener(this);
	    btnSup.addActionListener(this);

	    // Création d'un panel pour les boutons
	    JPanel panelBoutons = new JPanel();
	    panelBoutons.setLayout(new FlowLayout()); // Disposition horizontale
	    panelBoutons.add(btn);
	    panelBoutons.add(btnSup);

	    // Ajout des composants à la fenêtre
	    getContentPane().add(panelBoutons, BorderLayout.SOUTH);
	    getContentPane().add(tableau.getTableHeader(), BorderLayout.NORTH);
	    getContentPane().add(tableau, BorderLayout.CENTER);

	    pack();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getActionCommand().equals("Ajouter")) {
			System.out.println("Ajouter ligne ");
			String sport = JOptionPane.showInputDialog("Entrer le nom du sport");
			if (sport != null) {
				// ajouter un sport du modèle de table et de la bdd
				modele.addRow(sport);
			}
		}
		else if (arg0.getActionCommand().equals("Supprimer")) {
			System.out.println("Supprimer ligne ");
			
			if (Tp3Etape4.selectedIntitule != null) {
				// supprimer un sport du modèle de table et de la bdd
				modele.deleteRow(Tp3Etape4.selectedIntitule);
				tableau.update(getGraphics());
			}

		}
		
	}
				
	}
	
