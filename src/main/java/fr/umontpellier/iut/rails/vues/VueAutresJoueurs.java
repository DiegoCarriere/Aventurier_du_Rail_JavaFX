package fr.umontpellier.iut.rails.vues;

import fr.umontpellier.iut.rails.IJeu;
import fr.umontpellier.iut.rails.IJoueur;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.List;

/**
 * Cette classe présente les éléments des joueurs autres que le joueur courant,
 * en cachant ceux que le joueur courant n'a pas à connaitre.
 *
 * On y définit les bindings sur le joueur courant, ainsi que le listener à exécuter lorsque ce joueur change
 */
public class VueAutresJoueurs extends Pane {
    private List<IJoueur> joueurs;
    private VBox vbox;

    public VueAutresJoueurs(List<IJoueur> joueurs, IJeu jeu) {
        this.joueurs = joueurs;
        setPadding(new Insets(10));
        setPrefWidth(200);
        setPrefHeight(400);
        setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));

        vbox = new VBox();
        vbox.setSpacing(10);
        vbox.setAlignment(Pos.CENTER);

        for (IJoueur joueur : joueurs) {
            if (joueur != null ) {
                HBox hbox = createJoueurHBox(joueur);
                vbox.getChildren().add(hbox);
                hbox.setPadding(new Insets(10,10,10,10));
            }
        }

        // bouton passer
        Button passer = new Button("Passer");
        passer.setStyle("-fx-font-family: Arial ;  -fx-font-size: 16px; -fx-font-weight: bold");
        passer.setOnAction(actionEvent -> {
            jeu.passerAEteChoisi();
        });
        vbox.getChildren().add(passer);

        getChildren().add(vbox);
    }

    private HBox createJoueurHBox(IJoueur joueur) {
        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER);
        hbox.setSpacing(10);

        IJoueur.CouleurJoueur couleur = joueur.getCouleur();
        Color couleurFX = tradCouleur(couleur);

        ImageView avatarImageView = new ImageView();
        avatarImageView.setFitWidth(40);
        avatarImageView.setFitHeight(40);
        String imagePath = "/images/cartesWagons/avatar-" + couleur.toString() + ".png";
        Image avatarImage = new Image(getClass().getResourceAsStream(imagePath));
        avatarImageView.setImage(avatarImage);

        Label nomLabel = new Label(joueur.getNom());
        nomLabel.setStyle("-fx-font-family: Arial; -fx-font-size: 16px; -fx-font-weight: bold");
        hbox.setBackground(new Background(new BackgroundFill(couleurFX, CornerRadii.EMPTY, Insets.EMPTY)));

        hbox.getChildren().addAll(avatarImageView, nomLabel);

        hbox.setOnMouseEntered(event -> {
            afficherInfos(event.getX(), event.getY(), joueur);
        });

        hbox.setOnMouseExited(event -> {
            masquerInfos();
        });

        return hbox;
    }

    private void afficherInfos(double mouseX, double mouseY, IJoueur joueur) {
        int score = joueur.getScore();
        int nbPionsBateau = joueur.getNbPionsBateau();
        int nbPionsWagon = joueur.getNbPionsWagon();
        int nbDestinations = joueur.getDestinations().size();
        int nbCartesTransport = joueur.getCartesTransport().size();

        Label infoLabel = new Label("Score: " + score +
                "\nNombre de pions bateau: " + nbPionsBateau +
                "\nNombre de pions wagon: " + nbPionsWagon +
                "\nNombre de destinations: " + nbDestinations +
                "\nNombre de cartes transport: " + nbCartesTransport);
        infoLabel.setStyle("-fx-font-family: Arial; -fx-font-size: 12px;");
        infoLabel.setBackground(new Background(new BackgroundFill(tradCouleur(joueur.getCouleur()), CornerRadii.EMPTY, Insets.EMPTY)));
        infoLabel.setPadding(new Insets(5));

        // position des infos
        infoLabel.setLayoutX(mouseX);
        infoLabel.setLayoutY(mouseY + 10);


        getChildren().add(infoLabel);
    }

    private void masquerInfos() {
        getChildren().removeIf(node -> node instanceof Label);
    }

    private Color tradCouleur(IJoueur.CouleurJoueur couleur) {
        switch (couleur) {
            case JAUNE:
                return Color.YELLOW;
            case ROUGE:
                return Color.RED;
            case BLEU:
                return Color.BLUE;
            case VERT:
                return Color.GREEN;
            case ROSE:
                return Color.PINK;
            default:
                return Color.WHITE;
        }
    }


}
