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

        // Verificar que los recursos existan
        System.out.println("FXML: " + getClass().getResource("/utng/gtid2/jdj/menu_principal.fxml"));
        System.out.println("CSS : " + getClass().getResource("/estilos.css"));

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/utng/gtid2/jdj/menu_principal.fxml"));

        Parent raiz = loader.load();

        Scene escena = new Scene(raiz, 1150, 720);

        escena.getStylesheets().add(
                getClass().getResource("/estilos.css").toExternalForm());

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