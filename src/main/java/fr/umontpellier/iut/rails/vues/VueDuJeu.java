package fr.umontpellier.iut.rails.vues;

import fr.umontpellier.iut.rails.*;
import fr.umontpellier.iut.rails.mecanique.Joueur;
import fr.umontpellier.iut.rails.mecanique.data.Destination;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.List;

/**
 * Cette classe correspond à la fenêtre principale de l'application.
 *
 * Elle est initialisée avec une référence sur la partie en cours (Jeu).
 *
 * On y définit les bindings sur les éléments internes qui peuvent changer
 * (le joueur courant, les cartes Transport visibles, les destinations lors de l'étape d'initialisation de la partie, ...)
 * ainsi que les listeners à exécuter lorsque ces éléments changent
 */
public class VueDuJeu extends BorderPane {

    private final IJeu jeu;
    private VuePlateau plateau;

    private VBox joueurCourantVBox;

    private BorderPane instructionAutreJoueursCarteVisible;


    public VueDuJeu(IJeu jeu) {

        this.jeu = jeu;
        plateau = new VuePlateau();
        joueurCourantVBox = new VBox();
        instructionAutreJoueursCarteVisible = new BorderPane();


        /** init console info sur le joueur courant*/
        jeu.joueurCourantProperty().addListener((observableValue, oldJoueur, newJoueur) -> {

            // joueur courant
            joueurCourantVBox = new VueJoueurCourant(newJoueur);
            setRight(joueurCourantVBox);

            // autres joueurs
            List<IJoueur> liste = new ArrayList<>();
            liste.addAll(jeu.getJoueurs());
            liste.remove(newJoueur);
            instructionAutreJoueursCarteVisible.setRight(new VueAutresJoueurs(liste, jeu));

            if (jeu.jeuEnPreparationProperty().get()){
                HBox destinationsHbox = new HBox();
                Joueur joueurCourant = (Joueur) newJoueur;
                List<IDestination> listeD = new ArrayList<>(jeu.destinationsInitialesProperty());
                destinationsHbox.getChildren().add( new VueDestination(listeD.get(0)));
                instructionAutreJoueursCarteVisible.setBottom(destinationsHbox);
            }


            System.out.println("/---/ " + newJoueur.getNom() + " /---/");

            if(!jeu.finDePartieProperty().get() && !jeu.jeuEnPreparationProperty().get()){
                // destination
                for(IDestination d : newJoueur.getDestinations()){
                    System.out.print(" [ ");
                    for(String v : d.getVilles()){
                        System.out.print(v + ",");
                    }
                    System.out.print(" ] ");
                }
                System.out.println();

                // cartes
                for(ICarteTransport c : newJoueur.getCartesTransport()){
                    System.out.print(c.toString());
                }
            }
            System.out.println("\n");

        });

        Label instruction = new Label();
        instruction.setPadding(new Insets(10,0,0,10));
        instruction.setAlignment(Pos.TOP_CENTER);
        instruction.textProperty().bind(jeu.instructionProperty());
        //instruction.setStyle("-fx-font-family: Arial ;  -fx-font-size: 16px; -fx-font-weight: bold");
        instructionAutreJoueursCarteVisible.setTop(instruction);

        //instructionAutreJoueursCarteVisible.setBottom(new VueCarteTransport(joueurCourantVBox.));

        joueurCourantVBox.setAlignment(Pos.TOP_CENTER);

        setCenter(plateau);
        setBottom(instructionAutreJoueursCarteVisible);
        setRight(joueurCourantVBox);
        //BorderPane.setMargin(labelEtBouton, new Insets(20, 10, 400, 100));
    }

    public void creerBindings() {
        plateau.setLayoutX(0);
        plateau.setLayoutY(0);
        plateau.prefWidthProperty().bind(getScene().widthProperty().multiply(0.7));
        plateau.prefHeightProperty().bind(getScene().heightProperty().multiply(0.7));

        plateau.creerBindings();

        joueurCourantVBox.prefWidthProperty().bind(getScene().widthProperty().multiply(0.3));
        joueurCourantVBox.prefHeightProperty().bind(getScene().heightProperty().multiply(0.3));

    }

    public IJeu getJeu() {
        return jeu;
    }

    EventHandler<? super MouseEvent> actionPasserParDefaut = (mouseEvent -> getJeu().passerAEteChoisi());

}
