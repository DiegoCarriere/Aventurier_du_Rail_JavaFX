package fr.umontpellier.iut.rails.vues;

import fr.umontpellier.iut.rails.ICarteTransport;
import fr.umontpellier.iut.rails.IDestination;
import fr.umontpellier.iut.rails.IJeu;
import fr.umontpellier.iut.rails.IJoueur;
import fr.umontpellier.iut.rails.mecanique.Joueur;
import fr.umontpellier.iut.rails.mecanique.data.CarteTransport;
import fr.umontpellier.iut.rails.mecanique.data.Couleur;
import fr.umontpellier.iut.rails.mecanique.data.Destination;
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
public class VueJoueurCourant extends VBox {

    private IJoueur j;
    private Label score;
    private Menu destinations;
    private GridPane cartesTransportGrid;

    public VueJoueurCourant(IJoueur joueur){
        j = joueur;
        if (j != null) {
            Label nomJoueur = new Label(j.getNom().toString());
            System.out.println(j.getNom().toString());
            nomJoueur.setPadding(new Insets(20,300,10,0));
            nomJoueur.setStyle("-fx-font-size: 20px; -fx-font-weight: bold");

            IJoueur.CouleurJoueur couleurJoueur = j.getCouleur();
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
            score = new Label("Score: " + j.getScore());
            score.setStyle("-fx-font-size: 18px;");
            score.setPadding(new Insets(5,5,5,300));
            stackPane.getChildren().addAll(avatar, nomJoueur, score);

            StackPane.setAlignment(avatar,Pos.CENTER);
            StackPane.setAlignment(nomJoueur,Pos.CENTER_LEFT);
            StackPane.setAlignment(score,Pos.CENTER_RIGHT);

            getChildren().add(stackPane);

            // destinations
            destinations = new Menu("Destinations");

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
            HBox pionsWagonHBox = creerInfosPions("/images/bouton-pions-wagon.png", String.valueOf(j.getNbPionsWagon()));
            HBox pionsBateauHBox = creerInfosPions("/images/bouton-pions-bateau.png", String.valueOf(j.getNbPionsBateau()));
            HBox portsRestantsHBox = creerInfosPions("/images/port.png", String.valueOf(j.getNbPorts()));

            // cartes transport
            cartesTransportGrid = new GridPane();
            cartesTransportGrid.setHgap(10);
            cartesTransportGrid.setVgap(10);
            cartesTransportGrid.setAlignment(Pos.CENTER);

            List<? extends ICarteTransport> cartesTransport = joueur.getCartesTransport();
            Map<ICarteTransport, Integer> cartesParCarte = new TreeMap<>();

            for (ICarteTransport carte : cartesTransport) {
                if (!carte.estJoker()) {
                    // vérifie si la carte existe déjà dans la map
                    boolean existe = false;
                    for (Map.Entry<ICarteTransport, Integer> entry : cartesParCarte.entrySet()) {
                        ICarteTransport carteExistante = entry.getKey();
                        if ((carteExistante.getStringCouleur().equals(carte.getStringCouleur()) &&
                                ( (carteExistante.estBateau() && carte.estBateau()) || (carteExistante.estWagon() && carte.estWagon()) && !carteExistante.estDouble() && !carte.estDouble()))
                        || (carteExistante.getStringCouleur().equals(carte.getStringCouleur()) &&
                                ( (carteExistante.estBateau() && carte.estBateau()) || (carteExistante.estWagon() && carte.estWagon()) && carteExistante.estDouble() && carte.estDouble()))) {
                            // la carte existe on incrémente
                            int nbCartes = entry.getValue();
                            entry.setValue(nbCartes + 1);
                            existe = true;
                            break;
                        }
                    }

                    if (!existe) {
                        // si elle y est pas deja, on ajoute
                        cartesParCarte.put(carte, 1);
                    }
                }
            }

            int col = 0;
            int row = 0;
            for (Map.Entry<ICarteTransport, Integer> entree : cartesParCarte.entrySet()) {
                ICarteTransport carte = entree.getKey();
                int nbCartes = entree.getValue();

                VueCarteTransport vueCarte = new VueCarteTransport(carte, nbCartes);
                vueCarte.setNbCartesLabel();
                cartesTransportGrid.add(vueCarte, col, row);

                vueCarte.setOnMouseClicked((MouseEvent e) -> {
                    ((VueDuJeu) getScene().getRoot()).getJeu().uneCarteDuJoueurEstJouee(vueCarte.getCarteTransport());
                });

                col++;
                if (col >= 3) {
                    col = 0;
                    row++;
                }
            }

            getChildren().add(cartesTransportGrid);

            HBox infoHBox = new HBox(pionsWagonHBox, pionsBateauHBox, portsRestantsHBox);
            infoHBox.setAlignment(Pos.BOTTOM_CENTER);
            if (cartesTransportGrid.getRowCount() < 1) {
                infoHBox.setPadding(new Insets(400, 150, 0, 150));
            } else if (cartesTransportGrid.getRowCount() <= 2){
                infoHBox.setPadding(new Insets(0, 150, 0, 150));
            } else {
                infoHBox.setPadding(new Insets(0, 150, 0, 150));
            }
            infoHBox.setSpacing(20);
            this.setSpacing(10);
            getChildren().add(infoHBox);
            getChildren().add(new Pane(new MenuBar(destinations)));
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


}
