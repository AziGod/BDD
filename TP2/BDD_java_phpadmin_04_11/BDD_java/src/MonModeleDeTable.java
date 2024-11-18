import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;

public class MonModeleDeTable extends AbstractTableModel{

	private static String URL = "jdbc:mysql://localhost:10002/jo";
	private static String login = "root";
	private static String password = "licinfo2020";
	private Connection connexion;
	private Statement stmt;

	private ArrayList<String> intitules;
	private ArrayList<String> codes;

	private String[] entetes = { "CODE_SPORT", "INTITULE" };
	private JFrame frame;
	
	public MonModeleDeTable(JFrame frame) {
		this.frame=frame;
		majModele();
	}

	public void majModele() {
		intitules = new ArrayList<>();
		codes = new ArrayList<>();
		// Etablisement de la connexion avec la base
		try {
			connexion = DriverManager.getConnection(URL, login, password);
			stmt = connexion.createStatement();

			// lecture de la liste des sports
			String requete = "SELECT CODE_SPORT,INTITULE FROM SPORT";
			ResultSet resultat = stmt.executeQuery(requete);

			// on parcourt le résultat

			while (resultat.next()) {
				codes.add(resultat.getString(1));
				intitules.add(resultat.getString(2));
			}

		} catch (SQLException c) {
			System.out.println("Connexion echouee ou base de donnees inconnue : " + c);
		}

	}

	public int getColumnCount() {
		return 2;
	}

	public Object getValueAt(int parm1, int parm2) {
		if (parm2 == 1)
			return intitules.get(parm1);
		if (parm2 == 0)
			return codes.get(parm1);
		return null;
	}

	public int getRowCount() {
		return intitules.size();
	}

	public String getColumnName(int col) {
		return entetes[col];
	}

	public boolean isCellEditable(int row, int col) {
		if (col == 1)
			return true;
		else
			return false;
	}

	public void setValueAt(Object value, int row, int col) {
		System.out.println(value + ":" + row + ":" + col);
		// c'est forcément l'intitulé qui est modifié
		// du coup on le modifie dans la base de données

		try {
			
			//Condition pour ne pas ajouter si l'intitule existe déjà
	        PreparedStatement checkStatement = connexion.prepareStatement("SELECT COUNT(*) FROM SPORT WHERE INTITULE = ?");
	        checkStatement.setString(1, (String)value);
	        ResultSet resultSet = checkStatement.executeQuery();
	        if (resultSet.next() && resultSet.getInt(1) > 0) {
	            JOptionPane.showMessageDialog(null, "L'intitulé existe déjà dans la base de données.", "Erreur", JOptionPane.ERROR_MESSAGE);
	            return;
	        }

	        
	        
			PreparedStatement requetePreparee = connexion.prepareStatement("UPDATE SPORT SET INTITULE = ? WHERE CODE_SPORT = ?");
			requetePreparee.setString(1, (String)value);
			requetePreparee.setString(2, codes.get(row));

			// on lance la requête
			int ret=requetePreparee.executeUpdate();
			
			System.out.println("Modification réussie : "+ret);
			intitules.set(row, (String) value);
			fireTableCellUpdated(row, col);

		} catch (SQLException c) {
			System.out.println("Problème lors de la modification de l'intitulé: " + c);
		}

	}
	
	public void addRow(String intitule) {
		try {
			
			//Condition pour ne pas ajouter si l'intitule existe déjà
	        PreparedStatement checkStatement = connexion.prepareStatement("SELECT COUNT(*) FROM SPORT WHERE INTITULE = ?");
	        checkStatement.setString(1, intitule);
	        ResultSet resultSet = checkStatement.executeQuery();
	        if (resultSet.next() && resultSet.getInt(1) > 0) {
	            JOptionPane.showMessageDialog(null, "L'intitulé existe déjà dans la base de données.", "Erreur", JOptionPane.ERROR_MESSAGE);
	            return;
	        }
			
			
	        
			PreparedStatement requetePreparee = connexion.prepareStatement("INSERT INTO SPORT(INTITULE) VALUES (?)");
			requetePreparee.setString(1, intitule);
			

			// on lance la requête
			int ret=requetePreparee.executeUpdate();
			
			System.out.println("Insertion réussie : "+ret);
	        JOptionPane.showMessageDialog(null, "Insertion réussie");
	        fireTableRowsInserted(intitules.size(),intitules.size());
			majModele();
			frame.pack();
		} catch (SQLException c) {
			System.out.println("Problème lors de l'ajout d'un sport : " + c);
		}
		
    }

	

}
