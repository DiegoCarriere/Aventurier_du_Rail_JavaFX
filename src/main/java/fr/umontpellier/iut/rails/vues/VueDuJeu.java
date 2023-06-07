package fr.umontpellier.iut.rails.vues;

import fr.umontpellier.iut.rails.*;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.collections.ListChangeListener;
import javafx.event.EventTarget;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Arrays;
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

    private BorderPane joueurCourant;

    private BorderPane instructionAutreJoueursCarteVisible;
    private Pane vueAutresJoueurs;
    private FlowPane clickableHbox;


    public VueDuJeu(IJeu jeu) {

        this.jeu = jeu;
        plateau = new VuePlateau();
        joueurCourant = new BorderPane();
        instructionAutreJoueursCarteVisible = new BorderPane();
        clickableHbox = new FlowPane();
        clickableHbox.setAlignment(Pos.CENTER);
        clickableHbox.setVgap(10);
        clickableHbox.setHgap(10);
        clickableHbox.setPadding(new Insets(0,0,90,0));
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

        effetHover(Arrays.asList(piocheCartesBateau,piocheCartesWagons,piocheDestinations,piochePionsBateau,piochePionsWagon));

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
        HBox piocheCartes = new HBox(piocheCartesBateau, piocheCartesWagons);
        piocheCartes.setSpacing(10);
        piochesBox.setAlignment(Pos.CENTER);
        piochesBox.getChildren().addAll(
                piocheCartes,
                piocheDestinations,
                new HBox(piochePionsBateau, piochePionsWagon)
        );


        jeu.joueurCourantProperty().addListener((observableValue, oldJoueur, newJoueur) -> {

            joueurCourant = new VueJoueurCourant(newJoueur);
            setRight(joueurCourant);

            newJoueur.cartesTransportProperty().addListener((ListChangeListener<ICarteTransport>) change -> {
                while (change.next()){
                    if(change.wasAdded()) {
                        System.out.println("Faut actualiser DIRECT"); //TODO
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

                        //on l'ajoute aux destinations
                        VueDestination vueDestination = new VueDestination(destination);
                        clickableHbox.getChildren().add(vueDestination);

                        //init de si elle est choisie
                        vueDestination.setOnMouseClicked(mouseEvent -> {
                            ((VueDuJeu) getScene().getRoot()).getJeu().uneDestinationAEteChoisie(destination);
                            clickableHbox.getChildren().remove(vueDestination);
                        });
                    }
                }
            }
        });

        jeu.cartesTransportVisiblesProperty().addListener((ListChangeListener<ICarteTransport>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    clickableHbox.getChildren().clear();
                    int compteur = 0;
                    for (ICarteTransport carteTransport : jeu.cartesTransportVisiblesProperty()) {

                        //on l'ajoute aux carteVisible
                        VueCarteTransport vueCarteTransport = new VueCarteTransport(carteTransport, 1);
                        clickableHbox.getChildren().add(vueCarteTransport);
                        vueCarteTransport.setDisable(false);

                        //animation
                        vueCarteTransport.setAnimation(compteur, jeu.cartesTransportVisiblesProperty().size());
                        compteur ++;


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
        instruction.setStyle("-fx-font-size: 25px;");
        instruction.setAlignment(Pos.TOP_CENTER);
        instruction.textProperty().bind(jeu.instructionProperty());
        instructionAutreJoueursCarteVisible.setTop(instruction);


        // Création des marges
        setPadding(new Insets(30,50,0,40));

        setCenter(plateau);
        setBottom(instructionAutreJoueursCarteVisible);
        BorderPane.setAlignment(joueurCourant,Pos.CENTER_LEFT);
        //BorderPane.setMargin(labelEtBouton, new Insets(20, 10, 400, 100));
    }

    public void creerBindings() {
        plateau.setLayoutX(0);
        plateau.setLayoutY(0);
        plateau.prefWidthProperty().bind(getScene().widthProperty().multiply(0.85));
        plateau.prefHeightProperty().bind(getScene().heightProperty().multiply(0.85));

        plateau.creerBindings();

        joueurCourant.prefWidthProperty().bind(getScene().widthProperty().subtract(plateau.widthProperty()));
        joueurCourant.prefHeightProperty().bind(plateau.heightProperty());

        joueurCourant.maxHeightProperty().bind(plateau.heightProperty());

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

    protected static void effetHover(List<Node> listeN){
        for(Node n : listeN){
            effetHover(n);
        }
    }

    protected static void effetHover(Node n){
        n.setOnMouseEntered(mouseEvent -> {
            DropShadow ds = new DropShadow();
            ds.setOffsetY(3.0f);
            ds.setColor(Color.color(0.4f, 0.4f, 0.4f));

            n.setEffect(ds);
        });
        n.setOnMouseExited(mouseEvent -> {
            n.setEffect(null);
        });
    }
}
