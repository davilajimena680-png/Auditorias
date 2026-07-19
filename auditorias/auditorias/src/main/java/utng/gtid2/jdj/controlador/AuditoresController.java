package utng.gtid2.jdj.controlador;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.fxml.FXML;

import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import javafx.scene.control.cell.PropertyValueFactory;
import utng.gtid2.jdj.dao.AuditorDAO;
import utng.gtid2.jdj.modelo.Auditor;

import java.sql.SQLException;

public class AuditoresController {
    @FXML private TableView<Auditor> tblAuditores;
    @FXML private TableColumn<Auditor, String> colNombre;
    @FXML private TableColumn<Auditor, String> colCedula;
    @FXML private TableColumn<Auditor, String> colEmail;

    @FXML private TextField txtNombre;
    @FXML private TextField txtCedula;
    @FXML private TextField txtEmail;

    private final AuditorDAO auditorDAO = new AuditorDAO();
    private Auditor auditorSeleccionado;

    @FXML
    public void initialize() {

        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombreCompleto"));
        colCedula.setCellValueFactory(new PropertyValueFactory<>("cedulaProfesional"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        tblAuditores.getSelectionModel().selectedItemProperty().addListener((obs, anterior, seleccionado) -> {
            if (seleccionado != null) cargarEnFormulario(seleccionado);
        });

        cargarAuditores();
    }

    private void cargarAuditores() {
        try { 
            ObservableList<Auditor> datos = FXCollections.observableArrayList(auditorDAO.listarTodos());
            tblAuditores.setItems(datos);
        } catch (SQLException e) {
            mostrarError("No se pudieron cargar los auditores", e);
        }
    }

    private void cargarEnFormulario(Auditor a) {

        auditorSeleccionado = a;
        txtNombre.setText(a.getNombreCompleto());
        txtCedula.setText(a.getCedulaProfesional());
        txtEmail.setText(a.getEmail());
    }
    @FXML
    public void limpiarFormulario() {
        auditorSeleccionado = null;
        txtNombre.clear();
        txtCedula.clear();
        txtEmail.clear();
        tblAuditores.getSelectionModel().clearSelection();
    }

    @FXML
    public void guardarAuditor() {
        if (txtNombre.getText().isBlank()) {
            new Alert(Alert.AlertType.WARNING, "El nombre completo es obligatorio.").showAndWait();
            return;
        }
        try {
        Auditor a = (auditorSeleccionado != null) ? auditorSeleccionado : new Auditor(0, null, null, null);
        a.setNombreCompleto(txtNombre.getText());
        a.setCedulaProfesional(txtCedula.getText());
        a.setEmail(txtEmail.getText());
        if (auditorSeleccionado != null) {
            auditorDAO.actualizar(a);
        } else {
            auditorDAO.insertar(a);
        }
            cargarAuditores();
            limpiarFormulario();
        } catch (SQLException ex) {
            mostrarError("No se pudo guardar el auditor", ex);
        }
    }
    
    @FXML
    public void eliminarAuditor() {
        Auditor seleccionado = tblAuditores.getSelectionModel().getSelectedItem();

        if (seleccionado == null) {
            new Alert(Alert.AlertType.WARNING, "Selecciona un auditor de la tabla para eliminar.").showAndWait();
            return;
        }
        try {
            auditorDAO.eliminar(seleccionado.getIdAuditor());
            cargarAuditores();
            limpiarFormulario();
        } catch (SQLException ex) {
            mostrarError("No se pudo eliminar (verifica que no tenga auditorías asignadas)",ex);
        }
    }

    private void mostrarError(String mensaje, Exception ex) {
        new Alert(Alert.AlertType.ERROR, mensaje + ": " + ex.getMessage()).showAndWait();
    }
}