/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author User
 */
public class MainmenuController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private Parent root;
    @FXML
    private Stage stage;
    @FXML
    private Scene scene;
    @FXML
    private AnchorPane scenePane;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    @FXML
    private void MainGameBtn(ActionEvent event)throws IOException{
        root = FXMLLoader.load(getClass().getResource("MainGameStage.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    private void HowToPlayBtn(ActionEvent event)throws IOException{
        root = FXMLLoader.load(getClass().getResource("HowToPlay.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    public void ExitBtn(ActionEvent event){
        stage = (Stage)scenePane.getScene().getWindow();
        System.out.println("You successfully logged out!");
        stage.close();
    }
}
