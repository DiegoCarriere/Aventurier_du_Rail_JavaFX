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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Cette classe affiche les scores en fin de partie.
 * On peut éventuellement proposer de rejouer, et donc de revenir à la fenêtre principale
 *
 */
public class VueResultats extends VBox {

    private RailsIHM ihm;

    public VueResultats(RailsIHM ihm) {
        this.ihm = ihm;

        setPadding(new Insets(20));
        setSpacing(10);

        Label labelRes = new Label("Résultats du jeu");
        labelRes.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        labelRes.setTextAlignment(TextAlignment.CENTER);

        GridPane gridPaneScore = new GridPane();
        gridPaneScore.setVgap(10);
        gridPaneScore.setHgap(10);
        gridPaneScore.setAlignment(Pos.CENTER);

        List<Joueur> joueurs = ihm.getJeu().getJoueurs();

        // Trie les joueurs par score dans l'ordre décroissant
        joueurs.sort(Comparator.comparingInt(Joueur::getScore).reversed());

        int ligne = 0;
        int meilleurScore = joueurs.get(0).getScore();
        for (Joueur joueur : joueurs) {
            IJoueur.CouleurJoueur couleurJoueur = joueur.getCouleur();
            Color couleurFX;

            switch (couleurJoueur) {
                case JAUNE:
                    couleurFX = Color.YELLOW;
                    break;
                case ROUGE:
                    couleurFX = Color.RED;
                    break;
                case BLEU:
                    couleurFX = Color.BLUE;
                    break;
                case VERT:
                    couleurFX = Color.GREEN;
                    break;
                case ROSE:
                    couleurFX = Color.PINK;
                    break;
                default:
                    couleurFX = Color.WHITE;
            }

            StackPane stackPane = new StackPane();
            stackPane.setBackground(new Background(new BackgroundFill(couleurFX, new CornerRadii(10), null)));

            ImageView avatar = creerAvatar(couleurJoueur);
            Label nomJoueur = new Label(joueur.getNom());
            nomJoueur.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
            Label score = new Label(String.valueOf(joueur.getScore()));
            score.setFont(Font.font("Arial", FontWeight.BOLD, 16));

            GridPane.setConstraints(stackPane, 0, ligne);
            GridPane.setConstraints(nomJoueur, 1, ligne);
            GridPane.setConstraints(score, 2, ligne);

            gridPaneScore.getChildren().addAll(stackPane, nomJoueur, score);

            // verifie si le joueur a le meilleur score et ajoute la médaille
            if (joueur.getScore() == meilleurScore) {
                ImageView medaille = new ImageView(new Image("/images/medaille.png"));
                medaille.setFitHeight(20);
                medaille.setFitWidth(20);
                GridPane.setConstraints(medaille, 3, ligne);
                gridPaneScore.getChildren().add(medaille);
            }

            stackPane.getChildren().add(avatar);

            ligne++;
        }

        getChildren().addAll(labelRes, gridPaneScore);
        setAlignment(Pos.CENTER);
    }

    private ImageView creerAvatar(IJoueur.CouleurJoueur couleurJoueur) {
        ImageView avatar = new ImageView();
        avatar.setFitWidth(70);
        avatar.setFitHeight(70);

        String chemin = "/images/cartesWagons/avatar-" + couleurJoueur.toString() + ".png";
        Image avatarImage = new Image(getClass().getResourceAsStream(chemin));
        avatar.setImage(avatarImage);

        return avatar;
    }
}
