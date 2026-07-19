package utng.gtid2.jdj.controlador;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import utng.gtid2.jdj.dao.AuditoriaDAO;
import utng.gtid2.jdj.dao.EvaluacionDAO;
import utng.gtid2.jdj.dao.HallazgoDAO;
import utng.gtid2.jdj.dao.PlanAccionDAO;
import utng.gtid2.jdj.modelo.*;

import java.sql.SQLException;

public class HallazgosController {
    @FXML private ComboBox<Auditoria> cmbAuditoria;
    @FXML private TableView<Evaluacion> tblEvaluaciones;
    @FXML private TableColumn<Evaluacion, String> colCriterioEval;
    @FXML private TableColumn<Evaluacion, String> colCumplimientoEval;
    @FXML private ChoiceBox<String> chkTipo;
    @FXML private ChoiceBox<String> chkRiesgo;
    
    @FXML private TextArea txtDescripcionHallazgo;

    @FXML private TableView<Hallazgo> tblHallazgos;
    @FXML private TableColumn<Hallazgo, String> colTipoHallazgo;
    @FXML private TableColumn<Hallazgo, String> colRiesgoHallazgo;
    @FXML private TableColumn<Hallazgo, String> colDescripcionHallazgo;

    @FXML private TextField txtResponsable;
    @FXML private DatePicker dpFechaCompromiso;

    @FXML private ChoiceBox<String> chkEstatusPlan;
    private final AuditoriaDAO auditoriaDAO = new AuditoriaDAO();
    private final EvaluacionDAO evaluacionDAO = new EvaluacionDAO();
    private final HallazgoDAO hallazgoDAO = new HallazgoDAO();
    private final PlanAccionDAO planAccionDAO = new PlanAccionDAO();

    @FXML
    public void initialize() {
        colCriterioEval.setCellValueFactory(new PropertyValueFactory<>("descripcionCriterio"));
        colCumplimientoEval.setCellValueFactory(new PropertyValueFactory<>("cumplimiento"));
        colTipoHallazgo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colRiesgoHallazgo.setCellValueFactory(new PropertyValueFactory<>("nivelRiesgo"));
        colDescripcionHallazgo.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        chkTipo.setItems(FXCollections.observableArrayList("No conformidad", "Oportunidad de mejora"));
        chkTipo.setValue("No conformidad");
        chkRiesgo.setItems(FXCollections.observableArrayList("Alto", "Medio", "Bajo"));
        chkRiesgo.setValue("Medio");
        chkEstatusPlan.setItems(FXCollections.observableArrayList("Pendiente", "En proceso","Cerrado"));
        chkEstatusPlan.setValue("Pendiente");

        try {
            cmbAuditoria.setItems(FXCollections.observableArrayList(auditoriaDAO.listarTodas()));
        } catch (SQLException e) {
            mostrarError("No se pudieron cargar las auditorías", e);
        }
    }
    @FXML
    public void cargarDatosAuditoria() {
        if (cmbAuditoria.getValue() == null) {
            new Alert(Alert.AlertType.WARNING, "Selecciona una auditoría.").showAndWait();
            return;
        }

        int idAuditoria = cmbAuditoria.getValue().getIdAuditoria();

        try { ObservableList<Evaluacion> evaluaciones = FXCollections.observableArrayList(evaluacionDAO.obtenerPorAuditoria(idAuditoria));
            tblEvaluaciones.setItems(evaluaciones);
            ObservableList<Hallazgo> hallazgos = FXCollections.observableArrayList(hallazgoDAO.obtenerPorAuditoria(idAuditoria));
            tblHallazgos.setItems(hallazgos);
        } catch (SQLException e) {
            mostrarError("No se pudieron cargar evaluaciones/hallazgos", e);
        }
    }

    @FXML
    public void registrarHallazgo() {

        Evaluacion evaluacionSeleccionada = tblEvaluaciones.getSelectionModel().getSelectedItem();

        if (evaluacionSeleccionada == null) {
            new Alert(Alert.AlertType.WARNING, "Selecciona una evaluación de la tabla izquierda.").showAndWait();
            return;
        }

        if (txtDescripcionHallazgo.getText().isBlank()) {
            new Alert(Alert.AlertType.WARNING, "Describe el hallazgo antes de registrarlo.").showAndWait();
            return;
        }

        try {
            Hallazgo h = new Hallazgo(evaluacionSeleccionada.getIdEvaluacion(), chkTipo.getValue(), chkRiesgo.getValue(), txtDescripcionHallazgo.getText());
            hallazgoDAO.insertar(h);
            txtDescripcionHallazgo.clear();
            cargarDatosAuditoria();
        } catch (SQLException e) {
            mostrarError("No se pudo registrar el hallazgo", e);
        }
    }

    @FXML
    public void guardarPlanAccion() {
        Hallazgo hallazgoSeleccionado = tblHallazgos.getSelectionModel().getSelectedItem();
        if (hallazgoSeleccionado == null) {
            new Alert(Alert.AlertType.WARNING, "Selecciona un hallazgo de la tabla derecha.").showAndWait();
            return;
        }

        if (txtResponsable.getText().isBlank()) {
            new Alert(Alert.AlertType.WARNING, "Indica el responsable del plan de acción.").showAndWait();
            return;
        }
        
        try {
            PlanAccion p = new PlanAccion(hallazgoSeleccionado.getIdHallazgo(),
            txtResponsable.getText(), dpFechaCompromiso.getValue(),
            chkEstatusPlan.getValue());
            planAccionDAO.insertar(p);
            txtResponsable.clear();
            dpFechaCompromiso.setValue(null);
            chkEstatusPlan.setValue("Pendiente");

            new Alert(Alert.AlertType.INFORMATION, "Plan de acción guardado correctamente.").showAndWait();
        }catch (SQLException e) {
            mostrarError("No se pudo guardar el plan de acción", e);
        }
    }

    private void mostrarError(String mensaje, Exception ex) {
        new Alert(Alert.AlertType.ERROR, mensaje + ": " + ex.getMessage()).showAndWait();
    }
}