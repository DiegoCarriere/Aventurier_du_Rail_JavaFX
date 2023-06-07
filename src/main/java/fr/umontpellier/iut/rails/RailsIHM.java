package fr.umontpellier.iut.rails;

import fr.umontpellier.iut.rails.mecanique.Jeu;
import fr.umontpellier.iut.rails.vues.DonneesGraphiques;
import fr.umontpellier.iut.rails.vues.VueChoixJoueurs;
import fr.umontpellier.iut.rails.vues.VueDuJeu;
import fr.umontpellier.iut.rails.vues.VueResultats;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RailsIHM extends Application {

    private VueChoixJoueurs vueChoixJoueurs;
    private Stage primaryStage;
    private Jeu jeu;

    private final boolean avecVueChoixJoueurs = false;
    private final boolean avecResultat = false;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        debuterJeu();
    }

    public void debuterJeu() {
        if (avecVueChoixJoueurs) {
            vueChoixJoueurs = new VueChoixJoueurs();
            vueChoixJoueurs.setNomsDesJoueursDefinisListener(quandLesNomsJoueursSontDefinis);
            vueChoixJoueurs.show();
        } else {
            demarrerPartie();
        }
    }

    public void demarrerPartie() {
        List<String> nomsJoueurs;
        if (avecVueChoixJoueurs)
            nomsJoueurs = vueChoixJoueurs.getNomsJoueurs();
        else {
            nomsJoueurs = new ArrayList<>();
            nomsJoueurs.add("Guybrush");
            nomsJoueurs.add("Largo");
            nomsJoueurs.add("LeChuck");
            nomsJoueurs.add("Elaine");
        }
        jeu = new Jeu(nomsJoueurs.toArray(new String[0]));
        VueDuJeu vueDuJeu = new VueDuJeu(jeu);
        Scene scene = new Scene(vueDuJeu, Screen.getPrimary().getBounds().getWidth() * DonneesGraphiques.pourcentageEcran, Screen.getPrimary().getBounds().getHeight() * DonneesGraphiques.pourcentageEcran); // la scene doit être créée avant de mettre en place les bindings

        scene.setFill(Color.TRANSPARENT);

        /** icones bar */
        // Création de la HBox pour les boutons
        HBox buttonsBox = new HBox();
        buttonsBox.setAlignment(Pos.TOP_RIGHT);
        buttonsBox.setSpacing(10);


        // Création du bouton "Croix"
        Button closeButton = new Button("❌");
        closeButton.setStyle("-fx-font-size: 15px;");
        closeButton.setOnAction(e -> {
            // Code à exécuter lorsque le bouton "Croix" est cliqué
            arreterJeu();
        });

        // Création du bouton "Plein écran"
        Button fullscreenButton = new Button("⬛");
        fullscreenButton.setStyle("-fx-font-size: 15px;");
        fullscreenButton.setOnAction(e -> {
            // Code à exécuter lorsque le bouton "Plein écran" est cliqué
            if (primaryStage.isFullScreen()) {
                primaryStage.setFullScreen(false);
            } else {
                primaryStage.setFullScreen(true);
            }
        });


        // Création du bouton "Réduire"
        Button minimizeButton = new Button("_");
        minimizeButton.setStyle("-fx-font-size: 15px;");
        minimizeButton.setOnAction(e -> {
            // Code à exécuter lorsque le bouton "Réduire" est cliqué
            primaryStage.setIconified(true);
        });

        // Ajout des boutons à la HBox
        buttonsBox.setPadding(new Insets(0,0,5,0));
        buttonsBox.getChildren().addAll(minimizeButton, fullscreenButton, closeButton);

        // Ajout de la HBox à la vue du jeu
        vueDuJeu.setTop(buttonsBox);


        /**ajout du CSS*/
        try {
            File file = new File("src/main/resources/css/style.css");
            scene.getStylesheets().add(file.toURI().toURL().toExternalForm());
        } catch (MalformedURLException e){
            System.out.println(e.getMessage() + "bug1234");
        }

        vueDuJeu.creerBindings();
        jeu.run(); // le jeu doit être démarré après que les bindings ont été mis en place
        primaryStage.setMinWidth(Screen.getPrimary().getBounds().getWidth() * 0.7);
        primaryStage.setMinHeight(Screen.getPrimary().getBounds().getHeight() * 0.8);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Rails");
        primaryStage.centerOnScreen();
        primaryStage.setMaxWidth(Screen.getPrimary().getBounds().getWidth());
        primaryStage.setMaxHeight(Screen.getPrimary().getBounds().getHeight());
        primaryStage.setOnCloseRequest(event -> {
            this.arreterJeu();
            event.consume();
        });
        primaryStage.initStyle(StageStyle.TRANSPARENT);

        jeu.finDePartieProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue){
                primaryStage.setScene(new Scene(new VueResultats(this)));
            }
        });
        primaryStage.show();
    }

    private final ListChangeListener<String> quandLesNomsJoueursSontDefinis = change -> {
        if (!vueChoixJoueurs.getNomsJoueurs().isEmpty())
            demarrerPartie();
    };

    public void arreterJeu() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setContentText("On arrête de jouer ?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if(avecResultat){
                primaryStage.close();
                primaryStage = new Stage();
                primaryStage.setScene(new Scene(new VueResultats(this)));
                primaryStage.show();
            } else {
                Platform.exit();
            }

        }
    }

    public Jeu getJeu() {
        return jeu;
    }

    public static void main(String[] args) {
        launch(args);
    }

}