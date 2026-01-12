package com.example.javafx2;
import com.example.javafx2.logic.Vol;
import com.example.javafx2.logic.gestion_aeroport;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
public class VolsJourTab extends VBox {
    private gestion_aeroport aeroport;
    private TableView<Vol> volsTable;
    private ObservableList<Vol> volsData;
    private Button refreshButton;
    public VolsJourTab(gestion_aeroport aeroport) {
        this.aeroport = aeroport;
        setupUI();
    }
    private void setupUI() {
        setPadding(new Insets(20));
        setSpacing(15);
        Label titleLabel = new Label("Vols du Jour - " + LocalDate.now());
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        refreshButton = new Button("Actualiser");
        refreshButton.setStyle("-fx-font-size: 14px; -fx-background-color: #4CAF50; -fx-text-fill: white;");
        refreshButton.setOnAction(e -> updateVolsTable());
        HBox headerBox = new HBox(10);
        headerBox.getChildren().addAll(titleLabel, refreshButton);
        headerBox.setPadding(new Insets(0, 0, 10, 0));
        volsTable = new TableView<>();
        volsData = FXCollections.observableArrayList();
        volsTable.setItems(volsData);
        TableColumn<Vol, Integer> idCol = new TableColumn<>("ID Vol");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id_vol"));
        idCol.setPrefWidth(80);
        TableColumn<Vol, String> departCol = new TableColumn<>("Départ");
        departCol.setCellValueFactory(new PropertyValueFactory<>("depart"));
        departCol.setPrefWidth(120);
        TableColumn<Vol, String> destinationCol = new TableColumn<>("Destination");
        destinationCol.setCellValueFactory(new PropertyValueFactory<>("destination"));
        destinationCol.setPrefWidth(120);
        TableColumn<Vol, Date> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date_vol"));
        dateCol.setPrefWidth(150);
        dateCol.setCellFactory(column -> new TableCell<Vol, Date>() {
            @Override
            protected void updateItem(Date date, boolean empty) {
                super.updateItem(date, empty);
                if (empty || date == null) {
                    setText(null);
                } else {
                    setText(date.toString());
                }
            }
        });
        TableColumn<Vol, Integer> dureeCol = new TableColumn<>("Durée (s)");
        dureeCol.setCellValueFactory(new PropertyValueFactory<>("duree_vol"));
        dureeCol.setPrefWidth(100);
        TableColumn<Vol, Integer> seatsOccupeeCol = new TableColumn<>("Sièges Occupés");
        seatsOccupeeCol.setCellValueFactory(new PropertyValueFactory<>("seat_occupee"));
        seatsOccupeeCol.setPrefWidth(120);
        TableColumn<Vol, String> avionCol = new TableColumn<>("Avion (Matricule)");
        avionCol.setCellValueFactory(p -> new javafx.beans.property.SimpleStringProperty(
                p.getValue().getAvion().getMatricule()));
        avionCol.setPrefWidth(150);
        TableColumn<Vol, Integer> capaciteCol = new TableColumn<>("Capacité");
        capaciteCol.setCellValueFactory(p -> new javafx.beans.property.SimpleIntegerProperty(
                p.getValue().getAvion().getSeat_capacite()).asObject());
        capaciteCol.setPrefWidth(100);
        TableColumn<Vol, String> etatAvionCol = new TableColumn<>("État Avion");
        etatAvionCol.setCellValueFactory(p -> new javafx.beans.property.SimpleStringProperty(
                p.getValue().getAvion().getEtat()));
        etatAvionCol.setPrefWidth(120);
        TableColumn<Vol, Integer> nbPassagersCol = new TableColumn<>("Nb Passagers");
        nbPassagersCol.setCellValueFactory(p -> new javafx.beans.property.SimpleIntegerProperty(
                p.getValue().getPassagers() != null ? p.getValue().getPassagers().size() : 0).asObject());
        nbPassagersCol.setPrefWidth(120);
        TableColumn<Vol, Integer> nbEmployeesCol = new TableColumn<>("Nb Employés");
        nbEmployeesCol.setCellValueFactory(p -> new javafx.beans.property.SimpleIntegerProperty(
                p.getValue().getEmployees() != null ? p.getValue().getEmployees().size() : 0).asObject());
        nbEmployeesCol.setPrefWidth(120);
        volsTable.getColumns().addAll(idCol, departCol, destinationCol, dateCol, dureeCol,
                seatsOccupeeCol, avionCol, capaciteCol, etatAvionCol, nbPassagersCol, nbEmployeesCol);
        updateVolsTable();
        getChildren().addAll(headerBox, volsTable);
    }
    public void updateVolsTable() {
        volsData.clear();
        LocalDate today = LocalDate.now();
        List<Vol> todayVols = aeroport.getVols().stream()
                .filter(vol -> {
                    if (vol.getDate_vol() == null) {
                        return false;
                    }
                    LocalDate volDate = vol.getDate_vol().toInstant()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate();
                    return volDate.isEqual(today);
                })
                .collect(Collectors.toList());
        volsData.addAll(todayVols);
        if (volsData.isEmpty()) {
            System.out.println("Aucun vol prévu pour aujourd'hui.");
        }
    }
}
