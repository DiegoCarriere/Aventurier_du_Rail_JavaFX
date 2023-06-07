package fr.umontpellier.iut.rails.vues;

import fr.umontpellier.iut.rails.IJoueur;
import fr.umontpellier.iut.rails.RailsIHM;
import fr.umontpellier.iut.rails.mecanique.Joueur;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Cette classe affiche les scores en fin de partie.
 * On peut éventuellement proposer de rejouer, et donc de revenir à la fenêtre principale
 *
 */
public class VueResultats extends Pane {

    private RailsIHM ihm;

    public VueResultats(RailsIHM ihm) {

        Stage stage = new Stage();

        Label labelRes = new Label("Résultats du jeu");

        GridPane gridPaneScore = new GridPane();
        int i = 0;
        for(Joueur j : ihm.getJeu().getJoueurs()){
            gridPaneScore.add(new Label(j.getNom()),0,i);
            Label score = new Label(j.getScore() + "");
            gridPaneScore.add(score,1,i);
            i++;
        }

        Button quitterBouton = new Button("Quitter");

        quitterBouton.setOnAction(event -> {
            Platform.exit();
        });

        VBox root = new VBox(labelRes,gridPaneScore,quitterBouton);

        stage.setScene(new Scene(root));
        stage.show();
    }

}
