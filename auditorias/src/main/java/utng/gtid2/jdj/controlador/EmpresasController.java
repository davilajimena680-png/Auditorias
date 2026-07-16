package utng.gtid2.jdj.controlador;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.fxml.FXML;

import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import utng.gtid2.jdj.dao.EmpresaDAO;
import utng.gtid2.jdj.modelo.Empresa;

import java.sql.SQLException;

public class EmpresasController {

    @FXML private TableView<Empresa> tblEmpresas;
    @FXML private TableColumn<Empresa, String> colRazonSocial;
    @FXML private TableColumn<Empresa, String> colRfc;
    @FXML private TableColumn<Empresa, String> colSector;
    @FXML private TableColumn<Empresa, Integer> colEmpleados;
    @FXML private TableColumn<Empresa, String> colEmail;

    @FXML private TextField txtRazonSocial;
    @FXML private TextField txtRfc;
    @FXML private TextField txtSector;
    @FXML private Spinner<Integer> spnEmpleados;
    @FXML private TextField txtEmail;

    private final EmpresaDAO empresaDAO = new EmpresaDAO();
    private Empresa empresaSeleccionada;

    @FXML
    public void initialize() {
        colRazonSocial.setCellValueFactory(new PropertyValueFactory<>("razonSocial"));
        colRfc.setCellValueFactory(new PropertyValueFactory<>("rfc"));
        colSector.setCellValueFactory(new PropertyValueFactory<>("sector"));
        colEmpleados.setCellValueFactory(new PropertyValueFactory<>("numEmpleados"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("contactoEmail"));

        spnEmpleados.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0,100000, 0));

        tblEmpresas.getSelectionModel().selectedItemProperty().addListener((obs, anterior, seleccionado) -> {
            if (seleccionado != null) cargarEnFormulario(seleccionado);
        });
        cargarEmpresas();
    }

    private void cargarEmpresas() {
        try {ObservableList<Empresa> datos = FXCollections.observableArrayList(empresaDAO.listarTodas());
            tblEmpresas.setItems(datos);
        } catch (SQLException e) {mostrarError("No se pudieron cargar las empresas", e);}
    }

    private void cargarEnFormulario(Empresa e) {
        empresaSeleccionada = e;
        txtRazonSocial.setText(e.getRazonSocial());
        txtRfc.setText(e.getRfc());
        txtSector.setPromptText(e.getSector());
        spnEmpleados.getValueFactory().setValue(e.getNumEmpleados());
        txtEmail.setText(e.getContactoEmail());
    }
    
    @FXML
    public void limpiarFormulario() {
        empresaSeleccionada = null;
        txtRazonSocial.clear();
        txtRfc.clear();
        txtSector.clear();
        spnEmpleados.getValueFactory().setValue(0);
        txtEmail.clear();
        tblEmpresas.getSelectionModel().clearSelection();
    }

    @FXML
    public void guardarEmpresa() {
        if (txtRazonSocial.getText().isBlank()) {
            mostrarAlerta("La razón social es obligatoria.");
            return;
        }
        try { Empresa e = (empresaSeleccionada != null) ? empresaSeleccionada : new Empresa();
            e.setRazonSocial(txtRazonSocial.getText());
            e.setRfc(txtRfc.getText());
            e.setSector(txtSector.getText());
            e.setNumEmpleados(spnEmpleados.getValue());
            e.setContactoEmail(txtEmail.getText());
            if (empresaSeleccionada != null) {
                empresaDAO.actualizar(e);
            } else {
                empresaDAO.insertar(e);
            }
            cargarEmpresas();
            limpiarFormulario();

        } catch (SQLException ex) {
            mostrarError("No se pudo guardar la empresa", ex);
        }
    }

    @FXML
    public void eliminarEmpresa() {
        Empresa seleccionado = tblEmpresas.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta("Selecciona una empresa de la tabla para eliminar.");
            return;
        }
        try {
            empresaDAO.eliminar(seleccionado.getIdEmpresa());
            cargarEmpresas();
            limpiarFormulario();
        } catch (SQLException ex) {
            mostrarError("No se pudo eliminar (verifica que no tenga auditorías asociadas)",ex);
        }
    }

    private void mostrarAlerta(String mensaje) {
        new Alert(Alert.AlertType.WARNING, mensaje).showAndWait();
    }

    private void mostrarError(String mensaje, Exception ex) {
        new Alert(Alert.AlertType.ERROR, mensaje + ": " + ex.getMessage()).showAndWait();
    }
}