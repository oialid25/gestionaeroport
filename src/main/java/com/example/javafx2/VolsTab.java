package com.example.javafx2;
import com.example.javafx2.data.DataManager;
import com.example.javafx2.logic.*;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
public class VolsTab extends VBox {
    private gestion_aeroport aeroport;
    private DataManager dataManager;
    private ComboBox<Avion> avionComboBox;
    private DatePicker datePicker;
    private TextField destinationField;
    private TextField departField;
    private TextField dureeField;
    public VolsTab(gestion_aeroport aeroport) {
        this.aeroport = aeroport;
        this.dataManager = new DataManager();
        try {
            dataManager.initialiserTables();
            System.out.println("Base de données initialisée avec succès dans VolsTab.");
        } catch (Exception e) {
            System.err.println("Erreur lors de l'initialisation de la base de données dans VolsTab: " + e.getMessage());
            e.printStackTrace();
        }
        setupUI();
    }
    private void setupUI() {
        setPadding(new Insets(20));
        setSpacing(15);
        Label titleLabel = new Label("Création d'un Nouveau Vol");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20));
        Label dateLabel = new Label("Date:");
        datePicker = new DatePicker();
        datePicker.setValue(LocalDate.now());
        gridPane.add(dateLabel, 0, 0);
        gridPane.add(datePicker, 1, 0);
        Label destLabel = new Label("Destination:");
        destinationField = new TextField();
        gridPane.add(destLabel, 0, 1);
        gridPane.add(destinationField, 1, 1);
        Label departLabel = new Label("Départ:");
        departField = new TextField();
        gridPane.add(departLabel, 0, 2);
        gridPane.add(departField, 1, 2);
        Label dureeLabel = new Label("Durée (secondes):");
        dureeField = new TextField();
        gridPane.add(dureeLabel, 0, 3);
        gridPane.add(dureeField, 1, 3);
        Label avionLabel = new Label("Avion:");
        avionComboBox = new ComboBox<>();
        updateAvionComboBox();
        gridPane.add(avionLabel, 0, 4);
        gridPane.add(avionComboBox, 1, 4);
        Button newPlaneButton = new Button("Nouvel Avion");
        newPlaneButton.setOnAction(e -> openNewPlaneDialog());
        gridPane.add(newPlaneButton, 2, 4);
        Button planifierButton = new Button("Planifier");
        planifierButton.setStyle("-fx-font-size: 14px; -fx-background-color: #4CAF50; -fx-text-fill: white;");
        planifierButton.setOnAction(e -> planifierVol());
        gridPane.add(planifierButton, 1, 5);
        GridPane.setColumnSpan(planifierButton, 2);
        getChildren().addAll(titleLabel, gridPane);
    }
    private void updateAvionComboBox() {
        List<Avion> disponibles = aeroport.getAvions().stream()
                .filter(avion -> "disponible".equals(avion.getEtat()))
                .collect(Collectors.toList());
        avionComboBox.getItems().clear();
        avionComboBox.getItems().addAll(disponibles);
        if (!disponibles.isEmpty()) {
            avionComboBox.setCellFactory(param -> new ListCell<Avion>() {
                @Override
                protected void updateItem(Avion avion, boolean empty) {
                    super.updateItem(avion, empty);
                    if (empty || avion == null) {
                        setText(null);
                    } else {
                        setText(avion.getMatricule() + " (Capacité: " + avion.getSeat_capacite() + ")");
                    }
                }
            });
            avionComboBox.setButtonCell(new ListCell<Avion>() {
                @Override
                protected void updateItem(Avion avion, boolean empty) {
                    super.updateItem(avion, empty);
                    if (empty || avion == null) {
                        setText(null);
                    } else {
                        setText(avion.getMatricule() + " (Capacité: " + avion.getSeat_capacite() + ")");
                    }
                }
            });
        }
    }
    private void openNewPlaneDialog() {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setTitle("Nouvel Avion");
        GridPane dialogGrid = new GridPane();
        dialogGrid.setHgap(10);
        dialogGrid.setVgap(10);
        dialogGrid.setPadding(new Insets(20));
        Label matriculeLabel = new Label("Matricule:");
        TextField matriculeField = new TextField();
        dialogGrid.add(matriculeLabel, 0, 0);
        dialogGrid.add(matriculeField, 1, 0);
        Label dimWLabel = new Label("Largeur:");
        TextField dimWField = new TextField();
        Label dimHLabel = new Label("Hauteur:");
        TextField dimHField = new TextField();
        dialogGrid.add(dimWLabel, 0, 1);
        dialogGrid.add(dimWField, 1, 1);
        dialogGrid.add(dimHLabel, 0, 2);
        dialogGrid.add(dimHField, 1, 2);
        Label longArretLabel = new Label("Longueur Arrêt:");
        TextField longArretField = new TextField();
        Label tempsArretLabel = new Label("Temps Arrêt:");
        TextField tempsArretField = new TextField();
        dialogGrid.add(longArretLabel, 0, 3);
        dialogGrid.add(longArretField, 1, 3);
        dialogGrid.add(tempsArretLabel, 0, 4);
        dialogGrid.add(tempsArretField, 1, 4);
        Label longDepLabel = new Label("Longueur Départ:");
        TextField longDepField = new TextField();
        Label tempsDepLabel = new Label("Temps Départ:");
        TextField tempsDepField = new TextField();
        dialogGrid.add(longDepLabel, 0, 5);
        dialogGrid.add(longDepField, 1, 5);
        dialogGrid.add(tempsDepLabel, 0, 6);
        dialogGrid.add(tempsDepField, 1, 6);
        Label masseLabel = new Label("Masse Supportée (kg):");
        TextField masseField = new TextField();
        dialogGrid.add(masseLabel, 0, 7);
        dialogGrid.add(masseField, 1, 7);
        Label capaciteLabel = new Label("Capacité Sièges:");
        TextField capaciteField = new TextField();
        dialogGrid.add(capaciteLabel, 0, 8);
        dialogGrid.add(capaciteField, 1, 8);
        Button createButton = new Button("Créer");
        Button cancelButton = new Button("Annuler");
        createButton.setOnAction(e -> {
            try {
                String matricule = matriculeField.getText();
                double dimW = Double.parseDouble(dimWField.getText());
                double dimH = Double.parseDouble(dimHField.getText());
                double longArret = Double.parseDouble(longArretField.getText());
                double tempsArret = Double.parseDouble(tempsArretField.getText());
                double longDep = Double.parseDouble(longDepField.getText());
                double tempsDep = Double.parseDouble(tempsDepField.getText());
                double masse = Double.parseDouble(masseField.getText());
                int capacite = Integer.parseInt(capaciteField.getText());
                Avion newAvion = new Avion(matricule, dimW, dimH, longArret, tempsArret,
                        longDep, tempsDep, masse, capacite, "disponible", 0.0, 0.0);
                aeroport.ajouter_avion(newAvion);
                try {
                    if (dataManager != null) {
                        dataManager.insererAvion(newAvion);
                        System.out.println("Avion inséré avec succès dans la base de données. Matricule: " + newAvion.getMatricule());
                    }
                } catch (SQLException sqlEx) {
                    System.err.println("Note: L'avion peut déjà exister dans la base de données: " + sqlEx.getMessage());
                    sqlEx.printStackTrace();
                }
                updateAvionComboBox();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Succès");
                alert.setHeaderText(null);
                alert.setContentText("Avion créé avec succès!");
                alert.showAndWait();
                dialogStage.close();
            } catch (NumberFormatException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText(null);
                alert.setContentText("Veuillez entrer des valeurs numériques valides.");
                alert.showAndWait();
            }
        });
        cancelButton.setOnAction(e -> dialogStage.close());
        dialogGrid.add(createButton, 0, 9);
        dialogGrid.add(cancelButton, 1, 9);
        VBox dialogVBox = new VBox(10);
        dialogVBox.setPadding(new Insets(10));
        dialogVBox.getChildren().addAll(dialogGrid);
        Scene dialogScene = new Scene(dialogVBox, 400, 500);
        dialogStage.setScene(dialogScene);
        dialogStage.show();
    }
    private void planifierVol() {
        try {
            if (datePicker.getValue() == null || destinationField.getText().isEmpty() ||
                departField.getText().isEmpty() || dureeField.getText().isEmpty() ||
                avionComboBox.getValue() == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Attention");
                alert.setHeaderText(null);
                alert.setContentText("Veuillez remplir tous les champs.");
                alert.showAndWait();
                return;
            }
            LocalDate localDate = datePicker.getValue();
            Date dateVol = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            String destination = destinationField.getText();
            String depart = departField.getText();
            int duree = Integer.parseInt(dureeField.getText());
            Avion selectedAvion = avionComboBox.getValue();
            if (!"disponible".equals(selectedAvion.getEtat())) {
                throw new AvionIndisponibleException(selectedAvion.getMatricule());
            }
            Vol newVol = new Vol(selectedAvion, new ArrayList<>(), new ArrayList<>(),
                    destination, depart, dateVol, duree);
            aeroport.planifier(newVol);
            try {
                if (dataManager != null) {
                    dataManager.insererVol(newVol);
                    System.out.println("Vol inséré avec succès dans la base de données. ID: " + newVol.getId_vol());
                } else {
                    System.err.println("Erreur: DataManager est null!");
                }
            } catch (SQLException e) {
                System.err.println("Erreur SQL lors de l'insertion du vol: " + e.getMessage());
                System.err.println("Code SQL: " + e.getSQLState());
                System.err.println("Code d'erreur: " + e.getErrorCode());
                e.printStackTrace();
                Alert errorAlert = new Alert(Alert.AlertType.WARNING);
                errorAlert.setTitle("Avertissement Base de Données");
                errorAlert.setHeaderText("Problème lors de la sauvegarde");
                errorAlert.setContentText("Le vol a été créé en mémoire mais n'a pas pu être sauvegardé dans la base de données.\n" +
                                         "Erreur: " + e.getMessage());
                errorAlert.showAndWait();
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setHeaderText(null);
            alert.setContentText("Vol planifié avec succès! ID: " + newVol.getId_vol());
            alert.showAndWait();
            datePicker.setValue(LocalDate.now());
            destinationField.clear();
            departField.clear();
            dureeField.clear();
            updateAvionComboBox();
            avionComboBox.getSelectionModel().clearSelection();
        } catch (AvionIndisponibleException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Avion Indisponible");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez entrer une durée valide (nombre).");
            alert.showAndWait();
        }
    }
}
