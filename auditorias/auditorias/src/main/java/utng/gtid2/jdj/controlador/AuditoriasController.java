package utng.gtid2.jdj.controlador;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.fxml.FXML;

import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import utng.gtid2.jdj.dao.AuditorDAO;
import utng.gtid2.jdj.dao.AuditoriaDAO;
import utng.gtid2.jdj.dao.EmpresaDAO;
import utng.gtid2.jdj.modelo.Auditor;
import utng.gtid2.jdj.modelo.Auditoria;
import utng.gtid2.jdj.modelo.Empresa;

import java.sql.SQLException;
import java.time.LocalDate;

public class AuditoriasController {
    @FXML private TableView<Auditoria> tblAuditorias;
    @FXML private TableColumn<Auditoria, Integer> colId;
    @FXML private TableColumn<Auditoria, String> colEmpresa;
    @FXML private TableColumn<Auditoria, String> colAuditor;
    @FXML private TableColumn<Auditoria, String> colFechaInicio;
    @FXML private TableColumn<Auditoria, String> colFechaFin;
    @FXML private TableColumn<Auditoria, String> colEstatus;
    @FXML private ComboBox<Empresa> cmbEmpresa;
    @FXML private ComboBox<Auditor> cmbAuditor;
    @FXML private DatePicker dpFechaInicio;
    private final AuditoriaDAO auditoriaDAO = new AuditoriaDAO();
    private final EmpresaDAO empresaDAO = new EmpresaDAO();
    private final AuditorDAO auditorDAO = new AuditorDAO();
    @FXML
    public void initialize() {

        colId.setCellValueFactory(new PropertyValueFactory<>("idAuditoria"));
        colEmpresa.setCellValueFactory(new PropertyValueFactory<>("nombreEmpresa"));
        colAuditor.setCellValueFactory(new PropertyValueFactory<>("nombreAuditor"));
        colFechaInicio.setCellValueFactory(cellData -> {
            LocalDate f = cellData.getValue().getFehcaInicio();
            return new javafx.beans.property.SimpleStringProperty(f != null ? f.toString() : "");
        });

        colFechaFin.setCellValueFactory(cellData -> {
            LocalDate f = cellData.getValue().getFechaFin();
            return new javafx.beans.property.SimpleStringProperty(f != null ? f.toString() : "");
        });
        
        colEstatus.setCellValueFactory(new PropertyValueFactory<>("estatus"));

        dpFechaInicio.setValue(LocalDate.now());
        cargarCombos();
        cargarAuditorias();
    }

    private void cargarCombos() {
        try {
        cmbEmpresa.setItems(FXCollections.observableArrayList(empresaDAO.listarTodas()));
        cmbAuditor.setItems(FXCollections.observableArrayList(auditorDAO.listarTodos()));
        } catch (SQLException e) {
        mostrarError("No se pudieron cargar empresas/auditores", e);
        }
    }

    private void cargarAuditorias() {
        try {
            ObservableList<Auditoria> datos = FXCollections.observableArrayList(auditoriaDAO.listarTodas());
            tblAuditorias.setItems(datos);
        } catch (SQLException e) {
            mostrarError("No se pudieron cargar las auditorías", e);
        }
    }

    @FXML
    public void crearAuditoria() {
        if (cmbEmpresa.getValue() == null || cmbAuditor.getValue() == null || dpFechaInicio.getValue() == null) {
            new Alert(Alert.AlertType.WARNING, "Selecciona empresa, auditor y fecha de inicio.").showAndWait();
            return;
        }

        try {
            Auditoria a = new Auditoria(cmbEmpresa.getValue().getIdEmpresa(),
            cmbAuditor.getValue().getIdAuditor(), dpFechaInicio.getValue());
            auditoriaDAO.insertar(a);
            cargarAuditorias();
        } catch (SQLException e) {
            mostrarError("No se pudo crear la auditoría", e);
        }
    }

    @FXML
    public void concluirAuditoria() {
        Auditoria seleccionada = tblAuditorias.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            new Alert(Alert.AlertType.WARNING, "Selecciona una auditoría de la tabla.").showAndWait();
            return;
        }
        try {
            auditoriaDAO.actualizarEstatus(seleccionada.getIdAuditoria(), "Concluida", LocalDate.now());
            cargarAuditorias();
        } catch (SQLException e) {
            mostrarError("No se pudo concluir la auditoría", e);
        }
    }

    @FXML
    public void cancelarAuditoria() {
        Auditoria seleccionada = tblAuditorias.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            new Alert(Alert.AlertType.WARNING, "Selecciona una auditoría de la tabla.").showAndWait();
            return;
        }
        try {
            auditoriaDAO.actualizarEstatus(seleccionada.getIdAuditoria(), "Cancelada",
            seleccionada.getFechaFin());
            cargarAuditorias();
        } catch (SQLException e) {
            mostrarError("No se pudo cancelar la auditoría", e);
        }
    }
    @FXML
    public void eliminarAuditoria() {
        Auditoria seleccionada = tblAuditorias.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            new Alert(Alert.AlertType.WARNING, "Selecciona una auditoría de la tabla.").showAndWait();
            return;
        }
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION,
            "Esta acción también eliminará en cascada sus evaluaciones, hallazgos y planes de acción. ¿Continuar?");
        confirmacion.showAndWait().ifPresent(respuesta -> {
            if (respuesta == ButtonType.OK) {
                try {
                    auditoriaDAO.eliminar(seleccionada.getIdAuditoria());
                    cargarAuditorias();
                } catch (SQLException e) {
                    mostrarError("No se pudo eliminar la auditoría", e);
                }
            }
        });
    }
    
    private void mostrarError(String mensaje, Exception ex) {
        new Alert(Alert.AlertType.ERROR, mensaje + ": " + ex.getMessage()).showAndWait();
    }
}