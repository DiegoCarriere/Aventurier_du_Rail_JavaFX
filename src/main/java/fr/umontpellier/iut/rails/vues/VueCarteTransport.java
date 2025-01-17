package fr.umontpellier.iut.rails.vues;

import fr.umontpellier.iut.rails.ICarteTransport;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.util.Duration;

import java.util.Random;

/**
 * Cette classe représente la vue d'une carte Transport.
 *
 * On y définit le listener à exécuter lorsque cette carte a été choisie par l'utilisateur
 */
public class VueCarteTransport extends StackPane {

    private final ICarteTransport carteTransport;
    private Label nbCarteLabel;
    private ImageView imageView;


    public VueCarteTransport(ICarteTransport carteT, int nbCartes) {
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
        imageView = new ImageView(imageCarteFace);
        imageView.setFitWidth(150);
        imageView.setFitHeight(100);

        this.getChildren().add(imageView);

        nbCarteLabel = new Label(String.valueOf(nbCartes));
        nbCarteLabel.setStyle(
                "-fx-text-fill: black;" +
                        //"-fx-padding: 1px;" +
                        "-fx-font-size: 20px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-alignment: center"
        );

        CornerRadii cornerRadii = new CornerRadii(20);
        BackgroundFill backgroundFill = new BackgroundFill(Color.rgb(255, 212, 128), cornerRadii, null);
        Background background = new Background(backgroundFill);

        Border border = new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(20), new BorderWidths(2)));
        nbCarteLabel.setBorder(border);

        nbCarteLabel.setBackground(background);
        nbCarteLabel.setPrefWidth(30);

        nbCarteLabel.setVisible(false);
        this.getChildren().add(nbCarteLabel);
        StackPane.setAlignment(nbCarteLabel,Pos.TOP_RIGHT);

        VueDuJeu.effetHover(this);
    }

    public ICarteTransport getCarteTransport() {
        return carteTransport;
    }

    public void setNbCartesLabel(int indice){
        nbCarteLabel.setVisible(true);
        nbCarteLabel.setText(String.valueOf(indice));
    }

    public void setAnimation(int indiceActuel, int nombreTotalCartes) {
        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(1), imageView);

        double amplitude = 10.0;
        double offsetY = -10.0;

        double averageAmplitude = amplitude * 0.5;

        double startX = -((nombreTotalCartes - 1) / 2.0);

        double positionX = startX + indiceActuel;
        double startY = averageAmplitude * Math.sin(positionX * (2 * Math.PI / nombreTotalCartes)) + offsetY;

        translateTransition.setFromY(startY);
        translateTransition.setToY(offsetY);
        translateTransition.setCycleCount(TranslateTransition.INDEFINITE);
        translateTransition.setAutoReverse(true);
        translateTransition.setInterpolator(Interpolator.SPLINE(0.5, 0.1, 0.3, 1.0));
        translateTransition.play();
    }








}
