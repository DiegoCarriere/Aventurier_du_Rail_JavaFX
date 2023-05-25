package fr.umontpellier.iut.rails.vues;

import fr.umontpellier.iut.rails.IJeu;
import fr.umontpellier.iut.rails.IJoueur;
import fr.umontpellier.iut.rails.mecanique.Joueur;
import fr.umontpellier.iut.rails.mecanique.data.Couleur;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * Cette classe présente les éléments appartenant au joueur courant.
 *
 * On y définit les bindings sur le joueur courant, ainsi que le listener à exécuter lorsque ce joueur change
 */
public class VueJoueurCourant extends VBox {

    private Label nomJoueur;
    private IJoueur j;
    private IJoueur.CouleurJoueur couleur;

    public VueJoueurCourant(IJoueur joueur){
        j = joueur;
        if (j != null) {
            nomJoueur = new Label(j.getNom().toString());
            nomJoueur.setPadding(new Insets(10,400,10,10));
            nomJoueur.setStyle("-fx-font-family: Arial ;  -fx-font-size: 16px; -fx-font-weight: bold");
            couleur = j.getCouleur();

            IJoueur.CouleurJoueur couleurJoueur = couleur;
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

            setBackground(new Background(new BackgroundFill(couleurFX, null, null)));
            getChildren().add(nomJoueur);
        }



    }

}
