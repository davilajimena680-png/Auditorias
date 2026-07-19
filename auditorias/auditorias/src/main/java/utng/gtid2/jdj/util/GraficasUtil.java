package utng.gtid2.jdj.util;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

/**
* Utilidad para exportar un nodo de JavaFX (por ejemplo, un PieChart o BarChart)
* a un archivo PNG temporal, para poder incrustarlo en los reportes PDF/Word.
*/
public class GraficasUtil {
    public static String exportarNodoComoPng(Node nodo, String rutaSalida) throws IOException{

        WritableImage imagen = nodo.snapshot(new SnapshotParameters(), null);
        File archivo = new File(rutaSalida);
        ImageIO.write(SwingFXUtils.fromFXImage(imagen, null), "png", archivo);
        return archivo.getAbsolutePath();
    }
}