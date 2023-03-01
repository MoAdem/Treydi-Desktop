package GUI.Controllers;
import Entities.Utilisateur;
import Services.UtilisateurService;
import Utils.Enums.Roles;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import Entities.Utilisateur;
import Services.UtilisateurService;
import Utils.Enums.Roles;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.List;
import java.util.ResourceBundle;

public class DashbordAdminController implements Initializable {

    @FXML
    private TableView<Utilisateur> tableView;
    @FXML
    private TableColumn<Utilisateur, Integer> tfid_user;
    @FXML
    private TableColumn<Utilisateur, Integer> tfarchived;
    @FXML
    private TableColumn<Utilisateur, String> tfnom;

    @FXML
    private TableColumn<Utilisateur, String> tfprenom;

    @FXML
    private TableColumn<Utilisateur, String> tfemail;

    @FXML
    private TableColumn<Utilisateur, String> tfadresse;

    @FXML
    private TableColumn<Utilisateur, Roles> tfrole;
    @FXML
    private TableColumn<Utilisateur, Date> tfdate;
    @FXML
    private TableColumn<Utilisateur, Integer> tfscore;
    @FXML
    private ImageView minimize;
    @FXML
    private AnchorPane scenePane;

    Stage stage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        tfarchived.setCellValueFactory(new PropertyValueFactory<>("archived"));
        tfprenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        tfnom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        tfemail.setCellValueFactory(new PropertyValueFactory<>("email"));
        tfadresse.setCellValueFactory(new PropertyValueFactory<>("adresse"));
        tfrole.setCellValueFactory(new PropertyValueFactory<>("role"));
        tfscore.setCellValueFactory(new PropertyValueFactory<>("score"));
        tfdate.setCellValueFactory(new PropertyValueFactory<>("date_naissance"));


        afficher() ;



    }

    @FXML
    private void afficher() {
        UtilisateurService us = new UtilisateurService();
        List<Utilisateur> utilisateurs = us.afficher();

        tableView.getItems().clear();
        tableView.getItems().addAll(utilisateurs);




    }
    @FXML
    void supprimer(javafx.event.ActionEvent event) {
        Utilisateur selectedre = tableView.getSelectionModel().getSelectedItem();
        if (selectedre != null) {

            UtilisateurService us = new UtilisateurService();

            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Confirmation");
            confirmation.setHeaderText(null);
            confirmation.setContentText("Êtes-vous sûr de vouloir supprimer ?");
            Optional<ButtonType> result = confirmation.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                boolean supprime = us.supprimer(selectedre);
                if (supprime) {
                    tableView.getItems().remove(selectedre);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Suppression réussie");
                    alert.setHeaderText(null);
                    alert.setContentText("Suppression effectuée avec succès.");
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erreur");
                    alert.setHeaderText(null);
                    alert.setContentText("Erreur lors de la suppression.");
                    alert.showAndWait();
                }
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Avertissement");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner une réclamation.");
            alert.showAndWait();
        }

    }
    public void logout(javafx.event.ActionEvent actionEvent) {

        Alert alert=new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("You're about to exit ! ");

        if(alert.showAndWait().get() == ButtonType.OK){
            stage = (Stage) scenePane.getScene().getWindow();
            System.out.println("YOU XYZ");
            stage.close();
        }
    }

    @FXML
    public void Minimize(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }


/*
    @FXML
    private void route_AfficherUtilisateur(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../DashboardAdmin.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void route_ModifierUtilisateur(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../ModifierUser.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void route_SupprimerUtilisateur(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../SupprimerUser.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
*/
}