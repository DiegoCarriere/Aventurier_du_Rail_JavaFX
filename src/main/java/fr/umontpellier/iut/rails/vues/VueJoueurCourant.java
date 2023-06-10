package fr.umontpellier.iut.rails.vues;

import fr.umontpellier.iut.rails.ICarteTransport;
import fr.umontpellier.iut.rails.IDestination;
import fr.umontpellier.iut.rails.IJeu;
import fr.umontpellier.iut.rails.IJoueur;
import fr.umontpellier.iut.rails.mecanique.Joueur;
import fr.umontpellier.iut.rails.mecanique.data.CarteTransport;
import fr.umontpellier.iut.rails.mecanique.data.Couleur;
import fr.umontpellier.iut.rails.mecanique.data.Destination;
import javafx.beans.InvalidationListener;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Box;

import java.util.*;

/**
 * Cette classe présente les éléments appartenant au joueur courant.
 *
 * On y définit les bindings sur le joueur courant, ainsi que le listener à exécuter lorsque ce joueur change
 */
public class VueJoueurCourant extends BorderPane {

    private IJoueur joueur;
    private GridPane cartesTransportGrid;

    public VueJoueurCourant(IJoueur j){
        this.joueur = j;
        if (joueur != null) {
            Label nomJoueur = new Label(joueur.getNom().toString());
            System.out.println(joueur.getNom().toString());
            nomJoueur.setPadding(new Insets(5,0,5,10));

            IJoueur.CouleurJoueur couleurJoueur = joueur.getCouleur();
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
            StackPane stackPane = new StackPane();
            stackPane.setBackground(new Background(new BackgroundFill(couleurFX, new CornerRadii(10), null)));

            // avatar
            ImageView avatar = new ImageView();
            avatar.setFitWidth(70);
            avatar.setFitHeight(70);
            String chemin = "/images/cartesWagons/avatar-" + couleurJoueur.toString() + ".png";
            Image avatarImage = new Image(getClass().getResourceAsStream(chemin));
            avatar.setImage(avatarImage);

            // score
            Label score = new Label("Score: " + joueur.getScore());

            score.setPadding(new Insets(5,10,5,0));
            stackPane.getChildren().addAll(avatar, nomJoueur, score);

            StackPane.setAlignment(avatar,Pos.CENTER);
            StackPane.setAlignment(nomJoueur,Pos.CENTER_LEFT);
            StackPane.setAlignment(score,Pos.CENTER_RIGHT);

            this.setTop(stackPane);

            // destinations
            Menu destinations = new Menu("Destinations");

            //Label infoLabel = new Label();
            for (IDestination d : joueur.getDestinations()) {
                StringBuilder nomVilles = new StringBuilder();
                int nbVille = d.getVilles().size();

                for (int i = 0; i < nbVille; i++) {
                    nomVilles.append(d.getVilles().get(i));
                    if (i < nbVille - 1) {
                        nomVilles.append(" / ");
                    }
                }
                destinations.getItems().add(new MenuItem(nomVilles.toString()));
            }
            if(joueur.getDestinations().isEmpty()) destinations.getItems().add(new MenuItem("Vide"));



            // infos pions et ports
            HBox pionsWagonHBox = creerInfosPions("/images/bouton-pions-wagon.png", String.valueOf(joueur.getNbPionsWagon()));
            HBox pionsBateauHBox = creerInfosPions("/images/bouton-pions-bateau.png", String.valueOf(joueur.getNbPionsBateau()));
            HBox portsRestantsHBox = creerInfosPions("/images/port.png", String.valueOf(joueur.getNbPorts()));


            // cartes transport
            cartesTransportGrid = initCartes(joueur.getCartesTransport());
            this.setCenter(cartesTransportGrid);

            joueur.cartesTransportProperty().addListener((ListChangeListener<ICarteTransport>) change -> {
                while (change.next()){
                    if(change.wasAdded() || change.wasRemoved()){
                        cartesTransportGrid = initCartes(change.getList());
                        this.setCenter(cartesTransportGrid);
                    }
                }
            });



            HBox infoHBox = new HBox(pionsWagonHBox, pionsBateauHBox, portsRestantsHBox);

            // bouton passer
            Button passer = new Button("Passer");
            VueDuJeu.effetHover(passer);
            passer.setOnAction(actionEvent -> {
                ((VueDuJeu) getScene().getRoot()).getJeu().passerAEteChoisi();
            });

            MenuBar menuBar = new MenuBar(destinations);
            VueDuJeu.effetHover(menuBar);
            HBox bottomAll = new HBox(menuBar, passer,infoHBox);
            bottomAll.setSpacing(10);

            this.setBottom(bottomAll);
            bottomAll.setPadding(new Insets(0, 30, 0, 30));

            infoHBox.setSpacing(20);

        }
    }

    private HBox creerInfosPions(String chemin, String text) {
        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER);
        hbox.setSpacing(10);

        ImageView iconImageView = new ImageView(new Image(getClass().getResourceAsStream(chemin)));
        iconImageView.setFitWidth(30);
        iconImageView.setFitHeight(30);

        Label label = new Label(text);
        label.setStyle("-fx-font-size: 14px;");

        hbox.getChildren().addAll(iconImageView, label);

        return hbox;
    }

    public void setCartesTransportGrid(){
        cartesTransportGrid = initCartes(joueur.getCartesTransport());
    }

    private GridPane initCartes(List<? extends ICarteTransport> cartesTransport) {
        GridPane cartesTransportGrid = new GridPane();
        cartesTransportGrid.setHgap(10);
        cartesTransportGrid.setVgap(10);
        cartesTransportGrid.setAlignment(Pos.CENTER);

        Map<String, Map<Boolean, Map<Boolean, List<VueCarteTransport>>>> cartesParGroupe = new HashMap<>();

        for (ICarteTransport carte : cartesTransport) {
            String couleur = carte.getStringCouleur();
            boolean estDouble = carte.estDouble();
            boolean estWagon = carte.estWagon();

            cartesParGroupe.computeIfAbsent(couleur, k -> new HashMap<>())
                    .computeIfAbsent(estDouble, k -> new HashMap<>())
                    .computeIfAbsent(estWagon, k -> new ArrayList<>())
                    .add(new VueCarteTransport(carte, 0)); // L'indice initial est défini à 0
        }

        int col = 0;
        int ligne = 0;
        for (Map.Entry<String, Map<Boolean, Map<Boolean, List<VueCarteTransport>>>> entryCouleur : cartesParGroupe.entrySet()) {
            for (Map.Entry<Boolean, Map<Boolean, List<VueCarteTransport>>> entryDouble : entryCouleur.getValue().entrySet()) {
                for (Map.Entry<Boolean, List<VueCarteTransport>> entryWagon : entryDouble.getValue().entrySet()) {
                    List<VueCarteTransport> cartes = entryWagon.getValue();
                    Collections.reverse(cartes);

                    StackPane stackPane = new StackPane();
                    stackPane.setPadding(new Insets(5));

                    int index = 1;
                    for (VueCarteTransport vueCarte : cartes) {
                        vueCarte.setNbCartesLabel(index);
                        stackPane.getChildren().add(vueCarte);
                        index++;
                    }


                    stackPane.setOnMouseClicked(e -> {
                        VueCarteTransport vueCarte = (VueCarteTransport) stackPane.getChildren().get(stackPane.getChildren().size() - 1);
                        ICarteTransport carteTransport = vueCarte.getCarteTransport();
                        ((VueDuJeu) getScene().getRoot()).getJeu().uneCarteDuJoueurEstJouee(carteTransport);
                        if (joueur.cartesTransportPoseesProperty().contains(carteTransport)){
                            stackPane.getChildren().remove(vueCarte);
                        }

                    });

                    cartesTransportGrid.add(stackPane, col, ligne);

                    col++;
                    if (col >= 3) {
                        col = 0;
                        ligne++;
                    }
                }
            }
        }

        return cartesTransportGrid;
    }
}
