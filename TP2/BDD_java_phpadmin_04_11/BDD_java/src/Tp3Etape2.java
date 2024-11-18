
import java.sql.*;
import java.util.Scanner;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ngarric
 */
public class Tp3Etape2 {

	private static String URL = "jdbc:mysql://localhost:10002/jo";
	private static String login = "root";
	private static String password = "licinfo2020";
	private Connection connexion;
	private Statement stmt;

	public Tp3Etape2() {
		try {
			// Etablisement de la connexion avec la base
			connexion = DriverManager.getConnection(URL, login, password);
			stmt = connexion.createStatement();

		} catch (SQLException c) {
			System.out.println("Connexion echouee ou base de donnees inconnue : " + c);
		} catch (Exception d) {
			System.out.println("Problème sur connexion");
		}
	}

	public void menu() {

		int res = -1;
		while (res != 0) {
			// on propose à l'utilisateur de choisir entre plusieurs options
			Scanner scan = new Scanner(System.in);
			do {
				System.out.println("Menu\n0=Fin" +
								   "\n1=Lister tous les sports" +
								   "\n2=Ajout d'un sport" +
								   "\n3=Liste des sportifs d'un sport" +
								   "\n4=Liste des sportifs Individuel Or" +
								   "\n5=Liste des équipes et sportifs" +
								   "\n6=Supprimer un sportif");
				res = scan.nextInt();
				if (res<0 || res > 6) {
					System.out.println("mauvais choix! Recommencez.");
				}
			} while (res<0 || res > 6);
			switch (res) {

			// affichage de tous les noms de sports
			case 1:
				try {
					// on lance la requête
					String requete = "SELECT INTITULE FROM SPORT";
					ResultSet resultat = stmt.executeQuery(requete);

					System.out.println("Voici tous les noms de sports :");

					// on parcourt le résultat
					while (resultat.next()) {

						System.out.println(resultat.getString(1));
					}
					System.out.println();
				} catch (SQLException c) {
					System.out.println("Connexion echouee ou base de donnees inconnue : " + c);
				}
				break;
				
			// ajout d'un sport
			case 2:
				try {
					// saisie du nom du sport
					System.out.print("Entrer le nom du sport à ajouter : ");
					String nom = scan.next();
					
					PreparedStatement requetePreparee = connexion.prepareStatement("INSERT INTO SPORT(INTITULE) VALUES (?)");
					requetePreparee.setString(1,nom); 
					

					// on lance la requête
					requetePreparee.executeUpdate();
					System.out.println("Ajout réussi" );

				} catch (SQLException c) {
					System.out.println("Problème lors de l'ajout d'un sport: " + c);
				}
				break;
		
				
		// liste des sportifs d'un sport donnée
		case 3:
			try {
				
				// saisie du nom du sport
				System.out.print("Entrer le nom du sport concerné : ");
				String nom = scan.next();
								
		        // on prépare la requête SQL en utilisant un paramètre pour éviter les injections SQL
		        String requete = "SELECT NOM, PRENOM FROM SPORTIF " +
		                         "INNER JOIN SPORT ON SPORTIF.CODE_SPORT = SPORT.CODE_SPORT " +
		                         "WHERE SPORT.INTITULE = (?)";
		        PreparedStatement pstmt = connexion.prepareStatement(requete);
		        pstmt.setString(1, nom); // on lie le nom du sport au paramètre de la requête

		        // on exécute la requête
		        ResultSet resultat = pstmt.executeQuery();
				
				System.out.println("Voici tous les sportif du sport :");

				// on parcourt le résultat
				while (resultat.next()) {

					System.out.println(resultat.getString(1)+" "+resultat.getString(2));
					
				}
				System.out.println();
			} catch (SQLException c) {
				System.out.println("Connexion echouee ou base de donnees inconnue : " + c);
			}
			break;
			

		// liste des sportifs qui ont une médaille d'or en individuel
		case 4:
			try {
				// on lance la requête
				String requete = "SELECT NOM, PRENOM, SPORT.INTITULE " +
		                 "FROM SPORTIF " +
		                 "INNER JOIN SPORT ON SPORTIF.CODE_SPORT = SPORT.CODE_SPORT " +
		                 "INNER JOIN DISCIPLINE ON SPORTIF.CODE_SPORT = DISCIPLINE.CODE_SPORT " +
		                 "INNER JOIN GAGNER_INDIVIDUEL ON GAGNER_INDIVIDUEL.CODE_DISCIPLINE = DISCIPLINE.CODE_DISCIPLINE " +
		                 "WHERE GAGNER_INDIVIDUEL.MEDAILLE = 'Or'";
				ResultSet resultat = stmt.executeQuery(requete);

				System.out.println("Voici tous les sportifs individuel Or :");

				// on parcourt le résultat
				while (resultat.next()) {

					System.out.println(resultat.getString(1)+" "+resultat.getString(2)+" "+resultat.getString(3));
				}
				System.out.println();
			} catch (SQLException c) {
				System.out.println("Connexion echouee ou base de donnees inconnue : " + c);
			}
			break;
			
			
		// liste des équipes et sportifs
		case 5:
			try {
				// on lance la requête
				String requete = "SELECT EQUIPE.DENOMINATION, NOM, PRENOM FROM SPORTIF " +
								 "INNER JOIN APPARTENIR_EQUIPE ON SPORTIF.NUM_LICENCE = APPARTENIR_EQUIPE.NUM_LICENCE " +
								 "INNER JOIN EQUIPE ON EQUIPE.NUM_EQUIPE = APPARTENIR_EQUIPE.NUM_EQUIPE ";
				ResultSet resultat = stmt.executeQuery(requete);

				System.out.println("Voici tous les sportifs individuel Or :");

				// on parcourt le résultat
				while (resultat.next()) {

					System.out.println("Equipe: "+resultat.getString(1)+" | Sportif: "+resultat.getString(2)+" "+resultat.getString(3));
				}
				System.out.println();
			} catch (SQLException c) {
				System.out.println("Connexion echouee ou base de donnees inconnue : " + c);
			}
			break;
			
			
		// supprimer un sportif
		case 6:
			try {
				// saisie du sportif à supprimer
				System.out.print("Entrer le nom du sportif à supprimer : ");
				String nom = scan.next();
				
				PreparedStatement requetePreparee = connexion.prepareStatement("DELETE FROM SPORTIF WHERE NOM = (?)");
				requetePreparee.setString(1,nom); 
				

				// on lance la requête
				requetePreparee.executeUpdate();
				System.out.println("Suppresion réussie");

			} catch (SQLException c) {
				System.out.println("Problème lors de la suppresion du sportif: " + c);
			}
			break;
			
			}
		}

		// fermeture de la connexion
		try {
			connexion.close();
			System.out.println("Programme terminé");
		} catch (SQLException c) {
			System.out.println("Problème de fermeture de connexion: " + c);
		}

	}

}
