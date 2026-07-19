package utng.gtid2.jdj.controlador;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.fxml.FXML;

import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import utng.gtid2.jdj.dao.AuditoriaDAO;
import utng.gtid2.jdj.dao.CategoriaDAO;
import utng.gtid2.jdj.dao.CriterioDAO;
import utng.gtid2.jdj.dao.EvaluacionDAO;
import utng.gtid2.jdj.modelo.*;

import java.sql.SQLException;

import java.util.List;
import java.util.Map;

import java.util.stream.Collectors;

public class CapturaEvaluacionController {

    @FXML private ComboBox<Auditoria> cmbAuditoria;
    @FXML private ComboBox<Categoria> cmbCategoria;
    
    @FXML private TableView<Criterio> tblCriterios;

    @FXML private TableColumn<Criterio, String> colDescripcion;
    @FXML private TableColumn<Criterio, Double> colPeso;
    @FXML private TableColumn<Criterio, String> colCumplimiento;
    @FXML private TableColumn<Criterio, String> colObservaciones;

    private final AuditoriaDAO auditoriaDAO = new AuditoriaDAO();
    private final CategoriaDAO categoriaDAO = new CategoriaDAO();
    private final CriterioDAO criterioDAO = new CriterioDAO();
    private final EvaluacionDAO evaluacionDAO = new EvaluacionDAO();
    @FXML
    public void initialize() {
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        colPeso.setCellValueFactory(new PropertyValueFactory<>("peso"));
        colCumplimiento.setCellValueFactory(new
        PropertyValueFactory<>("cumplimientoSeleccionado"));
        colCumplimiento.setCellFactory(ComboBoxTableCell.forTableColumn("Cumple", "Parcial", "No cumple"));
        colCumplimiento.setOnEditCommit(evento ->
        evento.getRowValue().setCumplimientoSeleccionado(evento.getNewValue()));
        colObservaciones.setCellValueFactory(new
        PropertyValueFactory<>("observacionesCapturadas"));
        colObservaciones.setCellFactory(TextFieldTableCell.forTableColumn());
        colObservaciones.setOnEditCommit(evento ->
        evento.getRowValue().setObservacionesCapturadas(evento.getNewValue()));

        cargarCombos();
    }

    private void cargarCombos() {
        try {
            List<Auditoria> activas = auditoriaDAO.listarTodas().stream().filter(a -> "En proceso".equals(a.getEstatus())).collect(Collectors.toList());
            cmbAuditoria.setItems(FXCollections.observableArrayList(activas));
            cmbCategoria.setItems(FXCollections.observableArrayList(categoriaDAO.listarTodas()));
        } catch (SQLException e) {
            mostrarError("No se pudieron cargar auditorías/categorías", e);
        }
    }

    @FXML
    public void cargarCriterios() {
        if (cmbAuditoria.getValue() == null || cmbCategoria.getValue() == null) {
            new Alert(Alert.AlertType.WARNING, "Selecciona una auditoría y una categoría.").showAndWait();
            return;
        }

        try {
            int idAuditoria = cmbAuditoria.getValue().getIdAuditoria();
            List<Criterio> criterios =
            criterioDAO.obtenerPorCategoria(cmbCategoria.getValue().getIdCategoria());

            // Precargar valores ya capturados previamente, si existen, para permitir edicion
            Map<Integer, Evaluacion> yaCapturadas = evaluacionDAO.obtenerPorAuditoria(idAuditoria).stream().collect(Collectors.toMap(Evaluacion::getIdCriterio, ev -> ev));
            for (Criterio c : criterios) {
                Evaluacion existente = yaCapturadas.get(c.getIdCriterio());
                if (existente != null) {
                    c.setCumplimientoSeleccionado(existente.getCumplimiento());
                    c.setObservacionesCapturadas(existente.getObservaciones() != null ?
                    existente.getObservaciones() : "");
                }
            }
            ObservableList<Criterio> datos = FXCollections.observableArrayList(criterios);
                tblCriterios.setItems(datos);
            } catch (SQLException e) {
            mostrarError("No se pudieron cargar los criterios", e);
        }
    }
    @FXML
    public void guardarEvaluacion() {
        if (cmbAuditoria.getValue() == null || tblCriterios.getItems().isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Carga primero los criterios de una categoría.").showAndWait();
            return;
        }
        try {
            int idAuditoria = cmbAuditoria.getValue().getIdAuditoria();
            for (Criterio c : tblCriterios.getItems()) {
                Evaluacion e = new Evaluacion();
                e.setIdAuditoria(idAuditoria);
                e.setIdCriterio(c.getIdCriterio());
                e.setCumplimiento(c.getCumplimientoSeleccionado());
                e.setObservaciones(c.getObservacionesCapturadas());
                evaluacionDAO.guardarOActualizar(e);
            }
            new Alert(Alert.AlertType.INFORMATION,
                    "Evaluación guardada correctamente (" + tblCriterios.getItems().size() + "criterios).").showAndWait();
        } catch (SQLException e) {
            mostrarError("No se pudo guardar la evaluación", e);
        }
    }

    private void mostrarError(String mensaje, Exception ex) {
        new Alert(Alert.AlertType.ERROR, mensaje + ": " + ex.getMessage()).showAndWait();
    }
}