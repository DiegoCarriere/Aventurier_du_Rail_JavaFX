package fr.umontpellier.iut.rails.vues;

import fr.umontpellier.iut.rails.IDestination;
import fr.umontpellier.iut.rails.IJeu;
import fr.umontpellier.iut.rails.IVille;
import fr.umontpellier.iut.rails.mecanique.data.Destination;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 * Cette classe correspond à la fenêtre principale de l'application.
 *
 * Elle est initialisée avec une référence sur la partie en cours (Jeu).
 *
 * On y définit les bindings sur les éléments internes qui peuvent changer
 * (le joueur courant, les cartes Transport visibles, les destinations lors de l'étape d'initialisation de la partie, ...)
 * ainsi que les listeners à exécuter lorsque ces éléments changent
 */
public class VueDuJeu extends HBox {

    private final IJeu jeu;
    private VuePlateau plateau;

    private VBox vbox;

    private VBox vboxPlateau;

    public VueDuJeu(IJeu jeu) {
        this.jeu = jeu;
        plateau = new VuePlateau();
        vbox = new VBox();
        HBox labelEtBouton = new HBox();


        Button passer = new Button("Passer");
        passer.setOnAction(actionEvent -> {
            getJeu().passerAEteChoisi();
        });
        jeu.joueurCourantProperty().addListener((observableValue, oldJoueur, newJoueur) -> {
            for(IDestination d : newJoueur.getDestinations()){
                for(String v : d.getVilles()){
                    System.out.print(v + ",");
                }
                System.out.print("  /  ");
            }
            System.out.println("\n");
        });

        Label instruction = new Label();
        instruction.textProperty().bind(jeu.instructionProperty());
        labelEtBouton.getChildren().add(instruction);
        labelEtBouton.getChildren().add(passer);

        vbox.setAlignment(Pos.TOP_CENTER);
        vboxPlateau = new VBox(plateau, labelEtBouton);
        getChildren().addAll(vboxPlateau, vbox);
    }

    public void creerBindings() {
        vboxPlateau.prefWidthProperty().bind(getScene().widthProperty().multiply(0.7));
        vboxPlateau.prefHeightProperty().bind(getScene().heightProperty().multiply(0.7));

        plateau.prefWidthProperty().bind(getScene().widthProperty().multiply(0.7));
        plateau.prefHeightProperty().bind(getScene().heightProperty().multiply(0.7));

        plateau.creerBindings();

        vbox.prefWidthProperty().bind(getScene().widthProperty().multiply(0.3));
        vbox.prefHeightProperty().bind(getScene().heightProperty().multiply(0.3));
    }

    public IJeu getJeu() {
        return jeu;
    }

    EventHandler<? super MouseEvent> actionPasserParDefaut = (mouseEvent -> getJeu().passerAEteChoisi());

}
