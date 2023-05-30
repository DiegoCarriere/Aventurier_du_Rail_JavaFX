package fr.umontpellier.iut.rails.vues;

import fr.umontpellier.iut.rails.ICarteTransport;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

/**
 * Cette classe représente la vue d'une carte Transport.
 *
 * On y définit le listener à exécuter lorsque cette carte a été choisie par l'utilisateur
 */
public class VueCarteTransport extends StackPane {

    private final ICarteTransport carteTransport;

    public VueCarteTransport(ICarteTransport carteT, int nbCartes) {
        this.carteTransport = carteT;

        StringBuilder nom = new StringBuilder("carte-");
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

        System.out.println(nom.toString());

        Image imageCarteFace = new Image(getClass().getResource("images/cartesWagons/" + nom.toString() + ".png").toExternalForm());

        ImageView imageView = new ImageView(imageCarteFace);

        this.getChildren().add(imageView);

        Label nbCarteLabel = new Label(String.valueOf(nbCartes));
        nbCarteLabel.setAlignment(Pos.TOP_RIGHT);
        this.getChildren().add(nbCarteLabel);


    }

    public ICarteTransport getCarteTransport() {
        return carteTransport;
    }



}
