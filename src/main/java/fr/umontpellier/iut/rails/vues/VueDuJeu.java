package fr.umontpellier.iut.rails.vues;

import fr.umontpellier.iut.rails.*;
import fr.umontpellier.iut.rails.mecanique.Joueur;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

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
    private Pane vueAutresJoueurs;
    private HBox clickableHbox;


    public VueDuJeu(IJeu jeu) {

        this.jeu = jeu;
        plateau = new VuePlateau();
        joueurCourantVBox = new VBox();
        instructionAutreJoueursCarteVisible = new BorderPane();
        clickableHbox = new HBox();
        clickableHbox.setAlignment(Pos.CENTER);
        clickableHbox.setSpacing(10);
        instructionAutreJoueursCarteVisible.setLeft(clickableHbox);

        /** pioches */
        ImageView piocheCartesBateau = new ImageView(new Image("images/cartesWagons/dos-BATEAU.png"));
        piocheCartesBateau.setOnMouseClicked((MouseEvent e) -> {
            ((VueDuJeu) getScene().getRoot()).getJeu().uneCarteBateauAEtePiochee();
        });
        ImageView piocheCartesWagons = new ImageView(new Image("images/cartesWagons/dos-WAGON.png"));
        piocheCartesWagons.setOnMouseClicked((MouseEvent e) -> {
            ((VueDuJeu) getScene().getRoot()).getJeu().uneCarteWagonAEtePiochee();
        });
        ImageView piocheDestinations = new ImageView(new Image("images/cartesWagons/destinations.png"));
        piocheDestinations.setOnMouseClicked((MouseEvent e) -> {
            ((VueDuJeu) getScene().getRoot()).getJeu().nouvelleDestinationDemandee();
        });
        ImageView piochePionsBateau = new ImageView(new Image("images/bouton-pions-bateau.png"));
        piochePionsBateau.setOnMouseClicked((MouseEvent e) -> {
            ((VueDuJeu) getScene().getRoot()).getJeu().nouveauxPionsBateauxDemandes();
        });
        ImageView piochePionsWagon = new ImageView(new Image("images/bouton-pions-wagon.png"));
        piochePionsWagon.setOnMouseClicked((MouseEvent e) -> {
            ((VueDuJeu) getScene().getRoot()).getJeu().nouveauxPionsWagonsDemandes();
        });

        piocheCartesBateau.setFitWidth(70);
        piocheCartesBateau.setFitHeight(100);
        piocheCartesWagons.setFitWidth(70);
        piocheCartesWagons.setFitHeight(100);
        piocheDestinations.setFitWidth(100);
        piocheDestinations.setFitHeight(70);
        piochePionsBateau.setFitWidth(50);
        piochePionsBateau.setFitHeight(50);
        piochePionsWagon.setFitWidth(50);
        piochePionsWagon.setFitHeight(50);


        VBox piochesBox = new VBox(10);
        piochesBox.setPadding(new Insets(10));
        piochesBox.getChildren().addAll(
                new HBox(piocheCartesBateau, piocheCartesWagons),
                piocheDestinations,
                new HBox(piochePionsBateau, piochePionsWagon)
        );


        jeu.joueurCourantProperty().addListener((observableValue, oldJoueur, newJoueur) -> {

            joueurCourantVBox = new VueJoueurCourant(newJoueur);
            setRight(joueurCourantVBox);

            newJoueur.cartesTransportProperty().addListener((ListChangeListener<ICarteTransport>) change -> {
                while (change.next()){
                    if(change.wasAdded()) {
                        System.out.println("fefefe");
                    }
                }
            });

            // autres joueurs
            List<IJoueur> liste = new ArrayList<>(jeu.getJoueurs());
            liste.remove(newJoueur);
            vueAutresJoueurs = new VueAutresJoueurs(liste, jeu);
            instructionAutreJoueursCarteVisible.setRight(new HBox(vueAutresJoueurs,piochesBox));

            consolInfo(newJoueur);

        });



        jeu.destinationsInitialesProperty().addListener((ListChangeListener<IDestination>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    clickableHbox.getChildren().clear();
                    for (IDestination destination : jeu.destinationsInitialesProperty()) {
                        clickableHbox.getChildren().add( new VueDestination(destination));
                    }
                }
            }
        });

        jeu.cartesTransportVisiblesProperty().addListener((ListChangeListener<ICarteTransport>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    clickableHbox.getChildren().clear();
                    for (ICarteTransport carteTransport : jeu.cartesTransportVisiblesProperty()) {

                        //on l'ajoute aux carteVisible
                        VueCarteTransport vueCarteTransport = new VueCarteTransport(carteTransport, 1);
                        clickableHbox.getChildren().add(vueCarteTransport);
                        vueCarteTransport.setDisable(false);


                        //init de si elle est choisie
                        vueCarteTransport.setOnMouseClicked((MouseEvent e) -> {

                            ((VueDuJeu) getScene().getRoot()).getJeu().uneCarteTransportAEteChoisie(carteTransport);
                            clickableHbox.getChildren().remove(vueCarteTransport);
                            for(Node vueCarteTransport1 : clickableHbox.getChildren()) {
                                vueCarteTransport1.setDisable(true);
                            }
                        });
                    }
                }
            }
        });



        /*
        imageView.setOnMouseClicked((MouseEvent e) -> {
            if (clickableJoue) {
                ((VueDuJeu) getScene().getRoot()).getJeu().uneCarteDuJoueurEstJouee(carteTransport);
            }
        });

         */

        TextField textfield = new TextField();
        jeu.saisieNbPionsWagonAutoriseeProperty().addListener((observableValue, ancienneValeur, nouvelleValeur) -> {
            if (nouvelleValeur){
                clickableHbox.getChildren().clear();
                clickableHbox.getChildren().add(textfield);
                textfield.setOnAction(event -> {
                    String entree = textfield.getText();
                    try {
                        int nombrePions = Integer.parseInt(entree);
                        if (nombrePions >= 10 && nombrePions <= 25) {
                            String nbPionsChoisis = Integer.toString(nombrePions);
                            jeu.leNombreDePionsSouhaiteAEteRenseigne(nbPionsChoisis);
                        } else {
                            textfield.clear();
                        }
                    } catch (NumberFormatException e) {
                        textfield.clear();
                    }
                });
            } else{
                if (clickableHbox.getChildren().contains(textfield)){
                    clickableHbox.getChildren().remove(textfield);
                }
            }

        });




        Label instruction = new Label();
        instruction.setPadding(new Insets(10,0,0,10));
        instruction.setAlignment(Pos.TOP_CENTER);
        instruction.textProperty().bind(jeu.instructionProperty());
        instructionAutreJoueursCarteVisible.setTop(instruction);


        joueurCourantVBox.setAlignment(Pos.TOP_CENTER);

        setCenter(plateau);
        setBottom(instructionAutreJoueursCarteVisible);
        BorderPane.setAlignment(joueurCourantVBox,Pos.CENTER_LEFT);
        //BorderPane.setMargin(labelEtBouton, new Insets(20, 10, 400, 100));
    }

    public void creerBindings() {
        plateau.setLayoutX(0);
        plateau.setLayoutY(0);
        plateau.prefWidthProperty().bind(getScene().widthProperty().multiply(0.85));
        plateau.prefHeightProperty().bind(getScene().heightProperty().multiply(0.85));

        plateau.creerBindings();

        joueurCourantVBox.prefWidthProperty().bind(getScene().widthProperty().subtract(plateau.widthProperty()));
        joueurCourantVBox.prefHeightProperty().bind(plateau.heightProperty());

        joueurCourantVBox.maxHeightProperty().bind(plateau.heightProperty());

        clickableHbox.prefWidthProperty().bind(plateau.widthProperty());

        instructionAutreJoueursCarteVisible.prefHeightProperty().bind(getScene().widthProperty().multiply(0.15));

    }

    public IJeu getJeu() {
        return jeu;
    }

    //EventHandler<? super MouseEvent> actionPasserParDefaut = (mouseEvent -> getJeu().passerAEteChoisi());

    private void consolInfo(IJoueur newJoueur){
        StringBuilder infos = new StringBuilder("/---/ " + newJoueur.getNom() + " /---/\n");

        if(!jeu.finDePartieProperty().get() && !jeu.jeuEnPreparationProperty().get()){
            // destination
            for(IDestination d : newJoueur.getDestinations()){
                infos.append('[');
                for(String v : d.getVilles()){
                    infos.append(v + " ");
                }
                infos.append(']');
            }
            infos.append('\n');

            // cartes
            for(ICarteTransport c : newJoueur.getCartesTransport()){
                infos.append(c.toString());
            }
        }
        System.out.println(infos + "\n");
    }

}
