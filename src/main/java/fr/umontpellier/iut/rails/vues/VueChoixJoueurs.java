package fr.umontpellier.iut.rails.vues;

import fr.umontpellier.iut.rails.IJoueur;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Cette classe correspond à une nouvelle fenêtre permettant de choisir le nombre et les noms des joueurs de la partie.
 *
 * Sa présentation graphique peut automatiquement être actualisée chaque fois que le nombre de joueurs change.
 * Lorsque l'utilisateur a fini de saisir les noms de joueurs, il demandera à démarrer la partie.
 */
public class VueChoixJoueurs extends Stage {

    private final ObservableList<String> nomsJoueurs;
    public ObservableList<String> nomsJoueursProperty() {
        return nomsJoueurs;
    }
    private ListView<String> listeJoueurs;
    private SimpleStringProperty nbJoueurStringProperty;

    public VueChoixJoueurs() {
        nomsJoueurs = FXCollections.observableArrayList();

        listeJoueurs = new ListView<>();

        Button boutonDemarrer = new Button("Jouer !");

        Menu menuNbJoueurs = new Menu();
        Label nbJoueurLabel = new Label("nombre de joueurs : ");
        HBox nbJoueursBox = new HBox(nbJoueurLabel,new MenuBar(menuNbJoueurs));

        VBox lancerEtNbJoueurs = new VBox(nbJoueursBox, boutonDemarrer);

        HBox root = new HBox(listeJoueurs,lancerEtNbJoueurs);
        Scene scene = new Scene(root,640,320);

        try {
            File file = new File("src/main/resources/css/style.css");
            scene.getStylesheets().add(file.toURI().toURL().toExternalForm());
        } catch (MalformedURLException e){
            System.out.println(e.getMessage() + "bug");
        }

        this.setScene(scene);

        /**choix des joueurs*/

        listeJoueurs.getItems().addAll(Arrays.asList("Guybrush","Largo","LeChuck","Elaine"));
        listeJoueurs.setEditable(true);
        listeJoueurs.setCellFactory(TextFieldListCell.forListView());

        /** bouton lancer */
        boutonDemarrer.setStyle("-fx-font-size: 30px;");
        boutonDemarrer.prefHeightProperty().bind(getScene().heightProperty().divide(2));
        boutonDemarrer.prefWidthProperty().bind(getScene().widthProperty().divide(2));

        boutonDemarrer.setOnAction(actionEvent -> setListeDesNomsDeJoueurs());

        /**lancer*/
        root.setSpacing(50);
        root.setAlignment(Pos.CENTER);

        /**nbJoueurs*/
        nbJoueurStringProperty = new SimpleStringProperty("4");
        menuNbJoueurs.textProperty().bind(nbJoueurStringProperty);
        for(int i = 2; i < 6; i++){
            MenuItem m = new MenuItem(String.valueOf(i));
            int finalI = i;

            m.setOnAction(actionEvent -> {
               if(listeJoueurs.getItems().size() > Integer.parseInt( m.getText())){
                   listeJoueurs.getItems().remove(finalI,listeJoueurs.getItems().size());
               }
               else if(listeJoueurs.getItems().size() < Integer.parseInt( m.getText())) {
                   while (listeJoueurs.getItems().size() < finalI){
                       listeJoueurs.getItems().add("joueur" + (listeJoueurs.getItems().size()+1) );
                       listeJoueurs.refresh();
                   }
               }
                nbJoueurStringProperty.setValue(String.valueOf(listeJoueurs.getItems().size()));
            });

          menuNbJoueurs.getItems().add(m);
        }
        lancerEtNbJoueurs.spacingProperty().bind(getScene().heightProperty().divide(5));

    }

    public List<String> getNomsJoueurs() {
        return listeJoueurs.getItems();
    }

    /**
     * Définit l'action à exécuter lorsque la liste des participants est correctement initialisée
     */
    public void setNomsDesJoueursDefinisListener(ListChangeListener<String> quandLesNomsDesJoueursSontDefinis) {
        nomsJoueurs.addListener(quandLesNomsDesJoueursSontDefinis);
    }

    /**
     * Définit l'action à exécuter lorsque le nombre de participants change
     */
    protected void setChangementDuNombreDeJoueursListener(ChangeListener<Integer> quandLeNombreDeJoueursChange) {}

    /**
     * Vérifie que tous les noms des participants sont renseignés
     * et affecte la liste définitive des participants
     */
    protected void setListeDesNomsDeJoueurs() {
        ArrayList<String> tempNamesList = new ArrayList<>();
        for (int i = 1; i < getNombreDeJoueurs() ; i++) {
            String name = getJoueurParNumero(i);
            if (name == null || name.equals("")) {
                tempNamesList.clear();
                break;
            }
            else
                tempNamesList.add(name);
        }
        if (!tempNamesList.isEmpty()) {
            hide();
            nomsJoueurs.clear();
            nomsJoueurs.addAll(tempNamesList);
        }
    }

    /**
     * Retourne le nombre de participants à la partie que l'utilisateur a renseigné
     */
    protected int getNombreDeJoueurs() {
        return listeJoueurs.getItems().size();
    }

    /**
     * Retourne le nom que l'utilisateur a renseigné pour le ième participant à la partie
     * @param playerNumber : le numéro du participant
     */
    protected String getJoueurParNumero(int playerNumber) {
        return listeJoueurs.getItems().get(playerNumber);
    }

}
