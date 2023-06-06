package fr.umontpellier.iut.rails.vues;

import fr.umontpellier.iut.rails.ICarteTransport;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

/**
 * Cette classe représente la vue d'une carte Transport.
 *
 * On y définit le listener à exécuter lorsque cette carte a été choisie par l'utilisateur
 */
public class VueCarteTransport extends StackPane {

    private final ICarteTransport carteTransport;
    private Label nbCarteLabel;

    public VueCarteTransport(ICarteTransport carteT, int nbCartes, boolean clickablePioche, boolean clickableJoue) {
        this.carteTransport = carteT;

        /**trouver le chemin de la carte*/
        StringBuilder nom = new StringBuilder("/images/cartesWagons/carte-");
        if(carteTransport.estBateau()){
            if(carteTransport.estDouble()){
                nom.append("DOUBLE-");
            } else {
                nom.append("BATEAU-");
            }

        } else if (carteTransport.estWagon()) {
            nom.append("WAGON-");
        } else {
            nom.append("JOKER-");
        }
        nom.append(carteTransport.getStringCouleur());
        nom.append(".png");
        String reelNom = nom.toString();

        /** création de l'image*/
        Image imageCarteFace = new Image(getClass().getResource(reelNom).toExternalForm());
        ImageView imageView = new ImageView(imageCarteFace);
        imageView.setFitWidth(150);
        imageView.setFitHeight(100);

        imageView.setOnMouseClicked((MouseEvent e) -> {
            if (clickablePioche) {
                ((VueDuJeu) getScene().getRoot()).getJeu().uneCarteTransportAEteChoisie(carteTransport);
            }
        });

        imageView.setOnMouseClicked((MouseEvent e) -> {
            if (clickableJoue) {
                ((VueDuJeu) getScene().getRoot()).getJeu().uneCarteDuJoueurEstJouee(carteTransport);
            }
        });


        this.getChildren().add(imageView);

        nbCarteLabel = new Label(String.valueOf(nbCartes));
        nbCarteLabel.setStyle(
                "-fx-text-fill: black;" +
                        "-fx-padding: 6px;" +
                        "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;"
        );

        CornerRadii cornerRadii = new CornerRadii(30);
        BackgroundFill backgroundFill = new BackgroundFill(Color.WHITE, cornerRadii, null);
        Background background = new Background(backgroundFill);
        nbCarteLabel.setBackground(background);

        nbCarteLabel.setVisible(false);
        this.getChildren().add(nbCarteLabel);
        StackPane.setAlignment(nbCarteLabel,Pos.TOP_RIGHT);
    }

    public ICarteTransport getCarteTransport() {
        return carteTransport;
    }

    public void setNbCartesLabel(){
        nbCarteLabel.setVisible(true);
    }



}
