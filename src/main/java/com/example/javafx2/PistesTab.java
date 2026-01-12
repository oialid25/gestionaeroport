package com.example.javafx2;
import com.example.javafx2.logic.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
public class PistesTab extends BorderPane {
    private gestion_aeroport aeroport;
    private TableView<Piste> pistesTable;
    private ObservableList<Piste> pistesData;
    private ComboBox<Vol> volComboBox;
    private Timeline refreshTimeline;
    public PistesTab(gestion_aeroport aeroport) {
        this.aeroport = aeroport;
        setupUI();
        setupAutoRefresh();
    }
    private void setupAutoRefresh() {
        refreshTimeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            for (Piste p : aeroport.getPistes()) {
                p.verifierEtMettreDisponible();
            }
            pistesTable.refresh();
        }));
        refreshTimeline.setCycleCount(Timeline.INDEFINITE);
        refreshTimeline.play();
    }
    private void setupUI() {
        setPadding(new Insets(20));
        pistesTable = new TableView<>();
        pistesData = FXCollections.observableArrayList();
        pistesTable.setItems(pistesData);
        TableColumn<Piste, Double> coordXCol = new TableColumn<>("Coordonnée X");
        coordXCol.setCellValueFactory(p -> new javafx.beans.property.SimpleDoubleProperty(
                p.getValue().getCoordonnees()[0]).asObject());
        coordXCol.setPrefWidth(100);
        TableColumn<Piste, Double> coordYCol = new TableColumn<>("Coordonnée Y");
        coordYCol.setCellValueFactory(p -> new javafx.beans.property.SimpleDoubleProperty(
                p.getValue().getCoordonnees()[1]).asObject());
        coordYCol.setPrefWidth(100);
        TableColumn<Piste, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        typeCol.setPrefWidth(100);
        TableColumn<Piste, Double> dimWCol = new TableColumn<>("Largeur");
        dimWCol.setCellValueFactory(p -> new javafx.beans.property.SimpleDoubleProperty(
                p.getValue().getDimensions()[0]).asObject());
        dimWCol.setPrefWidth(100);
        TableColumn<Piste, Double> dimHCol = new TableColumn<>("Hauteur");
        dimHCol.setCellValueFactory(p -> new javafx.beans.property.SimpleDoubleProperty(
                p.getValue().getDimensions()[1]).asObject());
        dimHCol.setPrefWidth(100);
        TableColumn<Piste, Boolean> dispoCol = new TableColumn<>("Disponible");
        dispoCol.setCellValueFactory(new PropertyValueFactory<>("est_dispo"));
        dispoCol.setPrefWidth(100);
        TableColumn<Piste, Double> tempsOccupCol = new TableColumn<>("Durée Occup. (s)");
        tempsOccupCol.setCellValueFactory(new PropertyValueFactory<>("temps_occup"));
        tempsOccupCol.setPrefWidth(130);
        TableColumn<Piste, String> tempsRestantCol = new TableColumn<>("Temps Restant (s)");
        tempsRestantCol.setCellValueFactory(p -> {
            Piste piste = p.getValue();
            if (piste.getEst_dispo() || piste.getTemps_occup() == 0) {
                return new javafx.beans.property.SimpleStringProperty("-");
            }
            double tempsRestant = piste.getTempsRestantOccupation();
            return new javafx.beans.property.SimpleStringProperty(String.format("%.1f", tempsRestant));
        });
        tempsRestantCol.setPrefWidth(150);
        pistesTable.getColumns().addAll(coordXCol, coordYCol, typeCol, dimWCol, dimHCol, dispoCol, tempsOccupCol, tempsRestantCol);
        pistesTable.setRowFactory(tv -> new TableRow<Piste>() {
            @Override
            protected void updateItem(Piste piste, boolean empty) {
                super.updateItem(piste, empty);
                if (empty || piste == null) {
                    setStyle("");
                } else {
                    if (piste.getEst_dispo()) {
                        setStyle("-fx-background-color: #90EE90;");
                    } else {
                        setStyle("-fx-background-color: #FFB6C1;");
                    }
                }
            }
        });
        updatePistesTable();
        setCenter(pistesTable);
        HBox operationsBox = new HBox(10);
        operationsBox.setPadding(new Insets(15));
        operationsBox.setAlignment(Pos.CENTER);
        Button addPisteButton = new Button("Ajouter Piste");
        addPisteButton.setStyle("-fx-font-size: 14px; -fx-background-color: #4CAF50; -fx-text-fill: white;");
        addPisteButton.setOnAction(e -> openAddPisteDialog());
        Label volLabel = new Label("Vol:");
        volComboBox = new ComboBox<>();
        updateVolComboBox();
        volComboBox.setCellFactory(param -> new ListCell<Vol>() {
            @Override
            protected void updateItem(Vol vol, boolean empty) {
                super.updateItem(vol, empty);
                if (empty || vol == null) {
                    setText(null);
                } else {
                    setText("Vol ID: " + vol.getId_vol() + " - " + vol.getDepart() + " → " + vol.getDestination());
                }
            }
        });
        volComboBox.setButtonCell(new ListCell<Vol>() {
            @Override
            protected void updateItem(Vol vol, boolean empty) {
                super.updateItem(vol, empty);
                if (empty || vol == null) {
                    setText(null);
                } else {
                    setText("Vol ID: " + vol.getId_vol() + " - " + vol.getDepart() + " → " + vol.getDestination());
                }
            }
        });
        Button autoriserDescenteButton = new Button("Autoriser Descente");
        autoriserDescenteButton.setStyle("-fx-font-size: 14px; -fx-background-color: #2196F3; -fx-text-fill: white;");
        autoriserDescenteButton.setOnAction(e -> autoriserDescente());
        Button parkerButton = new Button("Parker");
        parkerButton.setStyle("-fx-font-size: 14px; -fx-background-color: #FF9800; -fx-text-fill: white;");
        parkerButton.setOnAction(e -> parker());
        Button autoriserDepartButton = new Button("Autoriser Départ");
        autoriserDepartButton.setStyle("-fx-font-size: 14px; -fx-background-color: #9C27B0; -fx-text-fill: white;");
        autoriserDepartButton.setOnAction(e -> autoriserDepart());
        operationsBox.getChildren().addAll(addPisteButton, volLabel, volComboBox,
                autoriserDescenteButton, parkerButton, autoriserDepartButton);
        setBottom(operationsBox);
    }
    public void updatePistesTable() {
        for (Piste p : aeroport.getPistes()) {
            p.verifierEtMettreDisponible();
        }
        pistesData.clear();
        pistesData.addAll(aeroport.getPistes());
    }
    public void updateVolComboBox() {
        Vol selected = volComboBox.getValue();
        volComboBox.getItems().clear();
        volComboBox.getItems().addAll(aeroport.getVols());
        if (selected != null && volComboBox.getItems().contains(selected)) {
            volComboBox.setValue(selected);
        }
    }
    private void openAddPisteDialog() {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setTitle("Ajouter une Piste");
        GridPane dialogGrid = new GridPane();
        dialogGrid.setHgap(10);
        dialogGrid.setVgap(10);
        dialogGrid.setPadding(new Insets(20));
        Label coordXLabel = new Label("Coordonnée X:");
        TextField coordXField = new TextField();
        Label coordYLabel = new Label("Coordonnée Y:");
        TextField coordYField = new TextField();
        dialogGrid.add(coordXLabel, 0, 0);
        dialogGrid.add(coordXField, 1, 0);
        dialogGrid.add(coordYLabel, 0, 1);
        dialogGrid.add(coordYField, 1, 1);
        Label typeLabel = new Label("Type (escale/parking):");
        ComboBox<String> typeComboBox = new ComboBox<>();
        typeComboBox.getItems().addAll("escale", "parking");
        typeComboBox.setValue("escale");
        dialogGrid.add(typeLabel, 0, 2);
        dialogGrid.add(typeComboBox, 1, 2);
        Label dimWLabel = new Label("Largeur:");
        TextField dimWField = new TextField();
        Label dimHLabel = new Label("Hauteur:");
        TextField dimHField = new TextField();
        dialogGrid.add(dimWLabel, 0, 3);
        dialogGrid.add(dimWField, 1, 3);
        dialogGrid.add(dimHLabel, 0, 4);
        dialogGrid.add(dimHField, 1, 4);
        Label dispoLabel = new Label("Disponible:");
        CheckBox dispoCheckBox = new CheckBox();
        dispoCheckBox.setSelected(true);
        dialogGrid.add(dispoLabel, 0, 5);
        dialogGrid.add(dispoCheckBox, 1, 5);
        Button createButton = new Button("Créer");
        Button cancelButton = new Button("Annuler");
        createButton.setOnAction(e -> {
            try {
                double coordX = Double.parseDouble(coordXField.getText());
                double coordY = Double.parseDouble(coordYField.getText());
                String type = typeComboBox.getValue();
                double dimW = Double.parseDouble(dimWField.getText());
                double dimH = Double.parseDouble(dimHField.getText());
                boolean estDispo = dispoCheckBox.isSelected();
                Piste newPiste = new Piste(coordX, coordY, estDispo, type, dimW, dimH);
                aeroport.ajouter_piste(newPiste);
                updatePistesTable();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Succès");
                alert.setHeaderText(null);
                alert.setContentText("Piste ajoutée avec succès!");
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
        dialogGrid.add(createButton, 0, 6);
        dialogGrid.add(cancelButton, 1, 6);
        VBox dialogVBox = new VBox(10);
        dialogVBox.setPadding(new Insets(10));
        dialogVBox.getChildren().add(dialogGrid);
        Scene dialogScene = new Scene(dialogVBox, 400, 350);
        dialogStage.setScene(dialogScene);
        dialogStage.show();
    }
    private void autoriserDescente() {
        Vol selectedVol = volComboBox.getValue();
        if (selectedVol == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Attention");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner un vol.");
            alert.showAndWait();
            return;
        }
        try {
            Piste piste = aeroport.autoriser_descente_(selectedVol);
            updatePistesTable();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setHeaderText(null);
            alert.setContentText("Descente autorisée sur la piste de type " + piste.getType() + ".");
            alert.showAndWait();
        } catch (PisteNonDisponibleException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Piste Non Disponible");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
    private void parker() {
        Vol selectedVol = volComboBox.getValue();
        if (selectedVol == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Attention");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner un vol.");
            alert.showAndWait();
            return;
        }
        try {
            Piste piste = aeroport.Parker(selectedVol);
            updatePistesTable();
            if (piste != null) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Succès");
                alert.setHeaderText(null);
                alert.setContentText("Avion garé sur la piste de parking.");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Attention");
                alert.setHeaderText(null);
                alert.setContentText("Aucune piste de parking disponible pour cet avion.");
                alert.showAndWait();
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Erreur lors du parking: " + e.getMessage());
            alert.showAndWait();
        }
    }
    private void autoriserDepart() {
        Vol selectedVol = volComboBox.getValue();
        if (selectedVol == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Attention");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner un vol.");
            alert.showAndWait();
            return;
        }
        try {
            Piste piste = aeroport.autoriser_depart(selectedVol);
            updatePistesTable();
            if (piste != null) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Succès");
                alert.setHeaderText(null);
                alert.setContentText("Départ autorisé depuis la piste d'escale.");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Attention");
                alert.setHeaderText(null);
                alert.setContentText("Aucune piste d'escale disponible pour le départ.");
                alert.showAndWait();
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Erreur lors de l'autorisation de départ: " + e.getMessage());
            alert.showAndWait();
        }
    }
}
