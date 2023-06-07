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
    private boolean infoEstAffiche;

    public VueAutresJoueurs(List<IJoueur> joueurs, IJeu jeu) {
        infoEstAffiche = false;
        this.joueurs = joueurs;
        setPadding(new Insets(10));
        setPrefWidth(200);
        setPrefHeight(400);

        vbox = new VBox();
        vbox.setSpacing(10);
        vbox.setAlignment(Pos.CENTER);

        for (IJoueur joueur : joueurs) {
            if (joueur != null ) {
                HBox hbox = createJoueurHBox(joueur);
                VueDuJeu.effetHover(hbox);
                vbox.getChildren().add(hbox);
                hbox.setPadding(new Insets(10,10,10,10));
            }
        }

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

        hbox.setOnMousePressed(mouseEvent -> {
            afficherInfos(joueur);
        });
        hbox.setOnMouseReleased(mouseDragEvent -> {
            masquerInfos();
        });

        Label nomLabel = new Label(joueur.getNom());
        hbox.setBackground(new Background(new BackgroundFill(couleurFX, new CornerRadii(10), Insets.EMPTY)));

        hbox.getChildren().addAll(avatarImageView, nomLabel);

        return hbox;
    }

    private void afficherInfos(IJoueur joueur) {

        Label infoLabel = new Label("Score: " + joueur.getScore() +
                "\nNombre de pions bateau: " + joueur.getNbPionsBateau() +
                "\nNombre de pions wagon: " + joueur.getNbPionsWagon() +
                "\nNombre de destinations: " + joueur.getDestinations().size() +
                "\nNombre de cartes transport: " + joueur.getCartesTransport().size());
        infoLabel.setStyle(" -fx-font-size: 15px;");
        infoLabel.setBackground(new Background(new BackgroundFill(tradCouleur(joueur.getCouleur()), CornerRadii.EMPTY, Insets.EMPTY)));
        infoLabel.setPadding(new Insets(5));

        // position des infos
        infoLabel.setLayoutX(-210);

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
