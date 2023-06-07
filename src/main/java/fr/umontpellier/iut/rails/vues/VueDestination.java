package fr.umontpellier.iut.rails.vues;

import fr.umontpellier.iut.rails.IDestination;
import fr.umontpellier.iut.rails.mecanique.data.Ville;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

/**
 * Cette classe représente la vue d'une carte Destination.
 *
 * On y définit le listener à exécuter lorsque cette carte a été choisie par l'utilisateur
 */
public class VueDestination extends Button {

    private final IDestination destination;

    public VueDestination(IDestination dest) {
        this.destination = dest;

        StringBuilder nomVilles = new StringBuilder();
        int nbVille = destination.getVilles().size();

        for (int i = 0; i < nbVille; i++) {
            nomVilles.append(destination.getVilles().get(i));
            if(i <nbVille-1){
                nomVilles.append(" / ");
            }
        }
        this.setOnMouseEntered(mouseEvent -> {
            DropShadow ds = new DropShadow();
            ds.setOffsetY(3.0f);
            ds.setColor(Color.color(0.4f, 0.4f, 0.4f));

            this.setEffect(ds);
        });
        this.setOnMouseExited(mouseEvent -> {
            this.setEffect(null);
        });

        this.setText(nomVilles.toString());
    }

    public IDestination getDestination() {
        return destination;
    }

}
