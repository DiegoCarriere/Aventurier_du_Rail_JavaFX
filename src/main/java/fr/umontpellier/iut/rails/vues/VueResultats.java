package fr.umontpellier.iut.rails.vues;

import fr.umontpellier.iut.rails.IJoueur;
import fr.umontpellier.iut.rails.RailsIHM;
import fr.umontpellier.iut.rails.mecanique.Joueur;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
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
        this.ihm = ihm;

        VBox vbox = new VBox();


        setPadding(new Insets(20));

        Label labelRes = new Label("Résultats du jeu");
        labelRes.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        labelRes.setTextAlignment(TextAlignment.CENTER);

        GridPane gridPaneScore = new GridPane();
        gridPaneScore.setVgap(10);
        int i = 0;
        for (Joueur j : ihm.getJeu().getJoueurs()) {
            Label nomJoueur = new Label(j.getNom());
            nomJoueur.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
            Label score = new Label(String.valueOf(j.getScore()));
            score.setFont(Font.font("Arial", FontWeight.BOLD, 16));
            gridPaneScore.add(nomJoueur, 0, i);
            gridPaneScore.add(score, 1, i);
            i++;
        }
        vbox.getChildren().addAll(labelRes, gridPaneScore);
        vbox.setAlignment(Pos.CENTER);
        getChildren().addAll(vbox);
    }



}
