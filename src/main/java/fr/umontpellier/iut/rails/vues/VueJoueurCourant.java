package fr.umontpellier.iut.rails.vues;

import fr.umontpellier.iut.rails.ICarteTransport;
import fr.umontpellier.iut.rails.IDestination;
import fr.umontpellier.iut.rails.IJeu;
import fr.umontpellier.iut.rails.IJoueur;
import fr.umontpellier.iut.rails.mecanique.Joueur;
import fr.umontpellier.iut.rails.mecanique.data.Couleur;
import fr.umontpellier.iut.rails.mecanique.data.Destination;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.*;

/**
 * Cette classe présente les éléments appartenant au joueur courant.
 *
 * On y définit les bindings sur le joueur courant, ainsi que le listener à exécuter lorsque ce joueur change
 */
public class VueJoueurCourant extends VBox {

    private Label nomJoueur;
    private IJoueur j;
    private IJoueur.CouleurJoueur couleur;
    private Label score;
    private ImageView avatar;
    private Menu destinations;
    private Label infoLabel;
    private GridPane cartesTransportGrid;

    public VueJoueurCourant(IJoueur joueur){
        j = joueur;
        if (j != null) {
            nomJoueur = new Label(j.getNom().toString());
            nomJoueur.setPadding(new Insets(20,400,10,0));
            nomJoueur.setStyle("-fx-font-size: 16px; -fx-font-weight: bold");
            couleur = j.getCouleur();

            IJoueur.CouleurJoueur couleurJoueur = couleur;
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

            setBackground(new Background(new BackgroundFill(couleurFX, null, null)));
            HBox hbox = new HBox();
            hbox.setSpacing(10);

            // avatar
            avatar = new ImageView();
            avatar.setFitWidth(70);
            avatar.setFitHeight(70);
            String chemin = "/images/cartesWagons/avatar-" + couleurJoueur.toString() + ".png";
            Image avatarImage = new Image(getClass().getResourceAsStream(chemin));
            avatar.setImage(avatarImage);

            // score
            score = new Label("Score: " + j.getScore());
            score.setStyle("-fx-font-size: 14px;");
            score.setPadding(new Insets(10,10,10,0));
            hbox.getChildren().addAll(avatar, nomJoueur, score);

            getChildren().add(hbox);

            // destinations
            destinations = new Menu("Destinations");

            infoLabel = new Label();
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



            // infos pions et ports
            HBox pionsWagonHBox = creerInfosPions("/images/bouton-pions-wagon.png", String.valueOf(j.getNbPionsWagon()));
            HBox pionsBateauHBox = creerInfosPions("/images/bouton-pions-bateau.png", String.valueOf(j.getNbPionsBateau()));
            HBox portsRestantsHBox = creerInfosPions("/images/port.png", String.valueOf(j.getNbPorts()));

            // cartes transport
            cartesTransportGrid = new GridPane();
            cartesTransportGrid.setHgap(10);
            cartesTransportGrid.setVgap(10);

            List<? extends ICarteTransport> cartesTransport = joueur.getCartesTransport();
            Map<ICarteTransport, Integer> cartesParCarte = new TreeMap<>();

            for (ICarteTransport carte : cartesTransport) {
                if (!carte.estJoker()) {
                    // vérifie si la carte existe déjà dans la map
                    boolean existe = false;
                    for (Map.Entry<ICarteTransport, Integer> entry : cartesParCarte.entrySet()) {
                        ICarteTransport carteExistante = entry.getKey();
                        if (carteExistante.equals(carte)) {
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
                cartesTransportGrid.add(vueCarte, col, row);

                col++;
                if (col >= 3) {
                    col = 0;
                    row++;
                }
            }

            getChildren().add(cartesTransportGrid);


            HBox infoHBox = new HBox(pionsWagonHBox, pionsBateauHBox, portsRestantsHBox);
            infoHBox.setAlignment(Pos.BOTTOM_CENTER);
            //infoHBox.setPadding(new Insets(300,0,0,0));
            infoHBox.setSpacing(10);
            getChildren().add(infoHBox);
            getChildren().add(new MenuBar(destinations));
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
        label.setStyle("-fx-font-family: Arial; -fx-font-size: 14px;");

        hbox.getChildren().addAll(iconImageView, label);

        return hbox;
    }


}
