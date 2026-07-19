package utng.gtid2.jdj.controlador;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import utng.gtid2.jdj.dao.AuditoriaDAO;
import utng.gtid2.jdj.dao.EvaluacionDAO;
import utng.gtid2.jdj.dao.HallazgoDAO;
import utng.gtid2.jdj.dao.PlanAccionDAO;
import utng.gtid2.jdj.modelo.Auditoria;
import utng.gtid2.jdj.modelo.Evaluacion;
import utng.gtid2.jdj.modelo.Hallazgo;
import utng.gtid2.jdj.modelo.PlanAccion;
import utng.gtid2.jdj.reportes.ReporteExcel;
import utng.gtid2.jdj.reportes.ReportePDF;
import utng.gtid2.jdj.reportes.ReporteWord;
import utng.gtid2.jdj.util.GraficasUtil;

import java.io.File;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class DashboardController {

    @FXML private ComboBox<Auditoria> cmbAuditoria;
    @FXML private Label lblPorcentajeGlobal;
    @FXML private PieChart pieCumplimiento;
    @FXML private BarChart<String, Number> barPorCategoria;
    @FXML private HBox hboxGraficas;

    private final AuditoriaDAO auditoriaDAO = new AuditoriaDAO();
    private final EvaluacionDAO evaluacionDAO = new EvaluacionDAO();
    private final HallazgoDAO hallazgoDAO = new HallazgoDAO();
    private final PlanAccionDAO planAccionDAO = new PlanAccionDAO();

    @FXML
    public void initialize() {
        
        try {
            cmbAuditoria.setItems(FXCollections.observableArrayList(auditoriaDAO.listarTodas()));
        } catch (SQLException e) {
            mostrarError("No se pudieron cargar las auditorías", e);
        }
    }

    @FXML
    public void actualizarDashboard() {

        Auditoria auditoria = cmbAuditoria.getValue();

        if (auditoria == null) {
            new Alert(Alert.AlertType.WARNING, "Selecciona una auditoría.").showAndWait();
            return;
        }

        try {
            int idAuditoria = auditoria.getIdAuditoria();

            // ---- Grafica de pastel ----
            Map<String, Integer> resumen = evaluacionDAO.resumenCumplimiento(idAuditoria);
            pieCumplimiento.getData().clear();
            resumen.forEach((estado, total) -> pieCumplimiento.getData().add(new PieChart.Data(estado + " (" + total + ")", total)));

            // ---- Grafica de barras ----
            Map<String, Double> porCategoria = evaluacionDAO.porcentajePorCategoria(idAuditoria);
            barPorCategoria.getData().clear();
            XYChart.Series<String, Number> serie = new XYChart.Series<>();
            serie.setName("Cumplimiento (%)");
            porCategoria.forEach((categoria, pct) -> serie.getData().add(new XYChart.Data<>(categoria, pct)));
            barPorCategoria.getData().add(serie);

            // ---- Porcentaje global ponderado ----
            double global = evaluacionDAO.porcentajeGlobalPonderado(idAuditoria);
            lblPorcentajeGlobal.setText(global + " %");

        } catch (SQLException e) {
            mostrarError("No se pudo actualizar el dashboard", e);
        }
    }

    // ---------------------------------------------------------------
    // Generacion de reportes: un metodo independiente por cada formato
    // ---------------------------------------------------------------
    @FXML
    public void generarReportePDF() {

        Auditoria auditoria = validarSeleccion();

        if (auditoria == null) return;

        FileChooser selector = new FileChooser();
        selector.setTitle("Guardar reporte PDF");
        selector.setInitialFileName("auditoria_" + auditoria.getIdAuditoria() + "_reporte.pdf");
        selector.getExtensionFilters().add(new FileChooser.ExtensionFilter("Documento PDF", "*.pdf"));
        File destino = selector.showSaveDialog(ventanaActual());
        
        if (destino == null) return;
        
        try {
            DatosReporte datos = cargarDatos(auditoria);
            String rutaImagen = exportarGraficaTemporal(auditoria.getIdAuditoria());
            new ReportePDF().generar(destino.getAbsolutePath(), datos.empresa, datos.auditor, datos.estatus, datos.global,
            datos.resumen, datos.evaluaciones, datos.hallazgos, datos.planes, rutaImagen);
            new Alert(Alert.AlertType.INFORMATION, "Reporte PDF generado en:\n" + destino.getAbsolutePath()).showAndWait();

        } catch (Exception e) {
            mostrarError("No se pudo generar el reporte PDF", e);
        }
    }

    @FXML
    public void generarReporteWord() {

        Auditoria auditoria = validarSeleccion();
        
        if (auditoria == null) return;

        FileChooser selector = new FileChooser();
        selector.setTitle("Guardar reporte Word");
        selector.setInitialFileName("auditoria_" + auditoria.getIdAuditoria() + "_reporte.docx");
        selector.getExtensionFilters().add(new FileChooser.ExtensionFilter("Documento Word", "*.docx"));
        File destino = selector.showSaveDialog(ventanaActual());
        
        if (destino == null) return;
        
        try {
            DatosReporte datos = cargarDatos(auditoria);
            String rutaImagen = exportarGraficaTemporal(auditoria.getIdAuditoria());
            new ReporteWord().generar(destino.getAbsolutePath(),
            datos.empresa, datos.auditor, datos.estatus, datos.global,
            datos.resumen, datos.hallazgos, datos.planes, rutaImagen);
            new Alert(Alert.AlertType.INFORMATION, "Reporte Word generado en:\n" +
            destino.getAbsolutePath()).showAndWait();
        } catch (Exception e) {
            mostrarError("No se pudo generar el reporte Word", e);
        }
    }

    @FXML
    public void generarReporteExcel() {Auditoria auditoria = validarSeleccion();

        if (auditoria == null) return;

        FileChooser selector = new FileChooser();
        selector.setTitle("Guardar reporte Excel");
        selector.setInitialFileName("auditoria_" + auditoria.getIdAuditoria() + "_reporte.xlsx");
        selector.getExtensionFilters().add(new FileChooser.ExtensionFilter("Libro de Excel", "*.xlsx"));
        File destino = selector.showSaveDialog(ventanaActual());
        
        if (destino == null) return;
                
        try {
            DatosReporte datos = cargarDatos(auditoria);
            new ReporteExcel().generar(destino.getAbsolutePath(), datos.empresa, datos.evaluaciones, datos.porCategoria);
            new Alert(Alert.AlertType.INFORMATION, "Reporte Excel generado en:\n" + destino.getAbsolutePath()).showAndWait();
        } catch (Exception e) {
            mostrarError("No se pudo generar el reporte Excel", e);
        }
    }

    // ---------------------------------------------------------------
    // Helpers privados
    // ---------------------------------------------------------------
    /** Valida que haya una auditoría seleccionada y que el dashboard ya se haya actualizado.
    */
    private Auditoria validarSeleccion() {

        Auditoria auditoria = cmbAuditoria.getValue();

        if (auditoria == null) {
            new Alert(Alert.AlertType.WARNING, "Selecciona una auditoría y actualiza el dashboard primero.").showAndWait();
            return null;
        }

        if (pieCumplimiento.getData().isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Actualiza el dashboard antes de generar el reporte.").showAndWait();
            return null;
        }
        return auditoria;
    }

    private Stage ventanaActual() {
        return (Stage) hboxGraficas.getScene().getWindow();
    }

    /** Exporta la gráfica actual del dashboard a un PNG temporal, para incrustarla en
    PDF/Word. */
    private String exportarGraficaTemporal(int idAuditoria) throws Exception {
        File temporal = File.createTempFile("grafica_auditoria_" + idAuditoria + "_", ".png");
        temporal.deleteOnExit();
        return GraficasUtil.exportarNodoComoPng(hboxGraficas, temporal.getAbsolutePath());
    }

    /** Agrupa toda la consulta de datos necesaria para cualquiera de los tres reportes. */
    private DatosReporte cargarDatos(Auditoria auditoria) throws SQLException {
        int idAuditoria = auditoria.getIdAuditoria();
        DatosReporte datos = new DatosReporte();
        datos.empresa = auditoria.getNombreEmpresa();
        datos.auditor = auditoria.getNombreAuditor();
        datos.estatus = auditoria.getEstatus();
        datos.resumen = evaluacionDAO.resumenCumplimiento(idAuditoria);
        datos.porCategoria = evaluacionDAO.porcentajePorCategoria(idAuditoria);
        datos.global = evaluacionDAO.porcentajeGlobalPonderado(idAuditoria);
        datos.evaluaciones = evaluacionDAO.obtenerPorAuditoria(idAuditoria);
        datos.hallazgos = hallazgoDAO.obtenerPorAuditoria(idAuditoria);
        datos.planes = planAccionDAO.obtenerPorAuditoria(idAuditoria);
        return datos;
    }

    private void mostrarError(String mensaje, Exception ex) {
        new Alert(Alert.AlertType.ERROR, mensaje + ": " + ex.getMessage()).showAndWait();
    }

    /** Contenedor simple para no repetir 7 parametros sueltos entre los 3 metodos de reporte.
    */
    private static class DatosReporte {
        String empresa;
        String auditor;
        String estatus;
        double global;
        Map<String, Integer> resumen;
        Map<String, Double> porCategoria;
        List<Evaluacion> evaluaciones;
        List<Hallazgo> hallazgos;
        List<PlanAccion> planes;
    }
}