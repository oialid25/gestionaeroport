package com.example.javafx2.data;
import com.example.javafx2.logic.Avion;
import com.example.javafx2.logic.Passager;
import com.example.javafx2.logic.Vol;
import java.sql.*;
public class DataManager {
    private static final String DB_URL_H2 = "jdbc:h2:mem:aeroport_db;DB_CLOSE_DELAY=-1;INIT=RUNSCRIPT FROM 'classpath:database/schema.sql'";
    private static final String DB_USER_H2 = "root";
    private static final String DB_PASSWORD_H2 = "Dybala123@";
    private static final boolean USE_H2 = false;
    private static final String DB_URL_MYSQL = "jdbc:mysql://localhost:3306/aeroport_db?useSSL=false&serverTimezone=UTC";
    private static final String DB_USER_MYSQL = "root";
    private static final String DB_PASSWORD_MYSQL = "Dybala123@";
    private Connection connection;
    public DataManager() {
        try {
            if (USE_H2) {
                Class.forName("org.h2.Driver");
                connection = DriverManager.getConnection(DB_URL_H2, DB_USER_H2, DB_PASSWORD_H2);
                System.out.println("Connexion à H2 Database établie.");
            } else {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(DB_URL_MYSQL, DB_USER_MYSQL, DB_PASSWORD_MYSQL);
                System.out.println("Connexion à MySQL établie.");
            }
        } catch (ClassNotFoundException e) {
            System.err.println("Driver JDBC non trouvé: " + e.getMessage());
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Erreur de connexion à la base de données: " + e.getMessage());
            System.err.println("Code SQL: " + e.getSQLState());
            System.err.println("Code fournisseur: " + e.getErrorCode());
            e.printStackTrace();
        }
    }
    public void initialiserTables() throws SQLException {
        if (!USE_H2) {
            System.out.println("Vérification des tables MySQL...");
        } else {
            System.out.println("Tables H2 initialisées automatiquement via schema.sql");
        }
    }
    public void insererAvion(Avion avion) throws SQLException {
        String sql = "INSERT INTO avion (matricule, dimension_w, dimension_h, masse_supportee, seat_capacite, etat) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            double[] dimensions = avion.getDimensions();
            pstmt.setString(1, avion.getMatricule());
            pstmt.setDouble(2, dimensions[0]);
            pstmt.setDouble(3, dimensions[1]);
            pstmt.setDouble(4, avion.getMasse_supportee());
            pstmt.setInt(5, avion.getSeat_capacite());
            pstmt.setString(6, avion.getEtat());
            int lignesMiseAJour = pstmt.executeUpdate();
            System.out.println("Avion inséré. Nombre de lignes mises à jour: " + lignesMiseAJour);
        } catch (SQLException e) {
            if (e.getSQLState().equals("23505") || e.getSQLState().equals("23000")) {
                System.out.println("L'avion " + avion.getMatricule() + " existe déjà dans la base de données.");
                return;
            }
            throw e;
        }
    }
    public void insererVol(Vol vol) throws SQLException {
        String sql = "INSERT INTO vol (id_vol, destination, depart, date_vol, duree_vol, seat_occupee, id_avion) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            insererAvion(vol.getAvion());
            pstmt.setInt(1, vol.getId_vol());
            pstmt.setString(2, vol.getDestination());
            pstmt.setString(3, vol.getDepart());
            pstmt.setDate(4, new java.sql.Date(vol.getDate_vol().getTime()));
            pstmt.setInt(5, vol.getDuree_vol());
            pstmt.setInt(6, vol.getSeat_occupee());
            pstmt.setString(7, vol.getAvion().getMatricule());
            int lignesMiseAJour = pstmt.executeUpdate();
            System.out.println("Vol inséré. Nombre de lignes mises à jour: " + lignesMiseAJour);
            if (!connection.getAutoCommit()) {
                connection.commit();
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'insertion du vol ID " + vol.getId_vol() + ": " + e.getMessage());
            System.err.println("Code SQL: " + e.getSQLState());
            System.err.println("Code d'erreur: " + e.getErrorCode());
            e.printStackTrace();
            if (e.getSQLState() != null && (e.getSQLState().equals("23505") || e.getSQLState().equals("23000"))) {
                System.out.println("Le vol " + vol.getId_vol() + " existe déjà dans la base de données.");
                return;
            }
            throw e;
        }
    }
    public void ajouterPassager(Passager passager) throws SQLException {
        String sql = "INSERT INTO passager (num_passeport, nom, prenom, genre, date_naissance, masse_bag, id_vol) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, passager.getNum_passeport());
            pstmt.setString(2, passager.getNom());
            pstmt.setString(3, passager.getPrenom());
            pstmt.setString(4, passager.getGenre());
            pstmt.setDate(5, new java.sql.Date(passager.getDate_naissance().getTime()));
            pstmt.setDouble(6, passager.masse_bag());
            pstmt.setInt(7, passager.getVol().getId_vol());
            int lignesMiseAJour = pstmt.executeUpdate();
            System.out.println("Passager ajouté. Nombre de lignes mises à jour: " + lignesMiseAJour);
        }
    }
    public boolean passagerExiste(String numPasseport) throws SQLException {
        String sql = "SELECT COUNT(*) FROM passager WHERE num_passeport = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, numPasseport);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count > 0;
                }
            }
        }
        return false;
    }
    public void fermerConnexion() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Connexion à la base de données fermée.");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la fermeture de la connexion: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
