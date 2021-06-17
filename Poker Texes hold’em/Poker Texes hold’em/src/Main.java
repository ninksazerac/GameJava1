import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author notda
 */
public class Main extends Application{
    
    public Main () {
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Mainmenu.FXML"));
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.setTitle("Poker Texes hold’em");
        
        stage.show();
    }
}