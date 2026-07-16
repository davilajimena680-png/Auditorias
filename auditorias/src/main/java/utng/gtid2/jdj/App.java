package utng.gtid2.jdj;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import utng.gtid2.jdj.conexion.ConexionBD;

public class App extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader loader = new
        FXMLLoader(getClass().getResource("/utng/gtid2/arg/menu_principal.fxml"));
        Parent raiz = loader.load();
        Scene escena = new Scene(raiz, 1150, 720);
        escena.getStylesheets().add(getClass().getResource("/css/estilos.css").toExternalForm());
        stage.setTitle("Sistema de Auditoría de Igualdad Laboral y No Discriminación - UTNG");
        stage.setScene(escena);
        stage.setMinWidth(1000);
        stage.setMinHeight(650);
        stage.show();
    }

    @Override
    public void stop() {
        ConexionBD.cerrar();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}