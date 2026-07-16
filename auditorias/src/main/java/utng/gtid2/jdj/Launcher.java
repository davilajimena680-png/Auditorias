package utng.gtid2.jdj;

/**
* Punto de entrada alterno para ejecutar la aplicacion desde el boton &quot;Run&quot; de VS Code
* (o cualquier IDE) sin pasar por el plugin de Maven. Al no extender Application
* directamente, evita la verificacion de modulo que provoca el error
* &quot;JavaFX runtime components are missing&quot;.
*/
public class Launcher {
    public static void main(String[] args) {
        App.main(args);
    }
}