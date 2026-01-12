package com.example.javafx2;
import com.example.javafx2.logic.gestion_aeroport;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
public class AeroportApplication extends Application {
    private gestion_aeroport aeroport;
    @Override
    public void start(Stage primaryStage) {
        aeroport = new gestion_aeroport();
        TabPane tabPane = new TabPane();
        VolsTab volsTabContent = new VolsTab(aeroport);
        Tab tabVols = new Tab("Gestion des Vols");
        tabVols.setClosable(false);
        tabVols.setContent(volsTabContent);
        PassagersTab passagersTabContent = new PassagersTab(aeroport);
        Tab tabPassagers = new Tab("Passagers & Employés");
        tabPassagers.setClosable(false);
        tabPassagers.setContent(passagersTabContent);
        PistesTab pistesTabContent = new PistesTab(aeroport);
        Tab tabPistes = new Tab("Contrôle des Pistes");
        tabPistes.setClosable(false);
        tabPistes.setContent(pistesTabContent);
        VolsJourTab volsJourTabContent = new VolsJourTab(aeroport);
        Tab tabVolsJour = new Tab("Vols du Jour");
        tabVolsJour.setClosable(false);
        tabVolsJour.setContent(volsJourTabContent);
        tabPassagers.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                passagersTabContent.updateVolComboBox();
            }
        });
        tabPistes.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                pistesTabContent.updateVolComboBox();
                pistesTabContent.updatePistesTable();
            }
        });
        tabVolsJour.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                volsJourTabContent.updateVolsTable();
            }
        });
        tabPane.getTabs().addAll(tabVols, tabPassagers, tabPistes, tabVolsJour);
        Scene scene = new Scene(tabPane, 1000, 700);
        primaryStage.setTitle("Gestion Aéroport");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
