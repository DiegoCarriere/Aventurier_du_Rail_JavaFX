package fr.umontpellier.iut.rails.vues;

import fr.umontpellier.iut.rails.ICarteTransport;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

/**
 * Cette classe représente la vue d'une carte Transport.
 *
 * On y définit le listener à exécuter lorsque cette carte a été choisie par l'utilisateur
 */
public class VueCarteTransport extends Pane {

    private final ICarteTransport carteTransport;
    private Image imageCarteFace;
    private Image imageCartePile;

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

        imageCarteFace = new Image(getClass().getResource("images/cartesWagons/" + nom.toString() + ".png").toExternalForm());
        ImageView imageView = new ImageView(imageCarteFace);

        this.getChildren().add(imageView);
    }

    public ICarteTransport getCarteTransport() {
        return carteTransport;
    }



}
