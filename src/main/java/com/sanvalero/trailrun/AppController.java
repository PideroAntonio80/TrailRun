package com.sanvalero.trailrun;

import com.sanvalero.trailrun.dao.RaceDAO;
import com.sanvalero.trailrun.domain.Race;
import com.sanvalero.trailrun.util.AlertUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Creado por @ author: Pedro Orós
 * el 20/11/2020
 */
public class AppController implements Initializable {

    private final RaceDAO raceDAO;

    public TextField tfNombre;
    public TextField tfLugar;
    public TextField tfFecha;
    public TextField tfDistancia;
    public TextField tfDesnivel;
    public ComboBox<String> cbTipo;

    public ToggleGroup grupoBotones;
    public RadioButton rbLugar;
    public RadioButton rbDistancia;
    public TextField tfFiltroLugar;
    public ComboBox<String> cbFiltroDistancia;

    public TableView<Race> tvRaces;
    public TableColumn<Race, String> tcNombre;
    public TableColumn<Race, String> tcLugar;
    public TableColumn<Race, String> tcFecha;
    public TableColumn<Race, Integer> tcDistancia;
    public TableColumn<Race, Integer> tcDesnivel;
    public TableColumn<Race, String> tcTipo;
    ObservableList<Race> listaRaces;

    public Label lAviso;

    public AppController() {
        raceDAO = new RaceDAO();
        try {
            raceDAO.conectar();
        } catch (ClassNotFoundException cnfe) {
            AlertUtils.mostrarError("Error al iniciar la aplicación");
        } catch (SQLException sql) {
            AlertUtils.mostrarError("No se ha podido conectar");
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> listTipo = FXCollections.observableArrayList("<Selecciona Tipo>", "Km Vertical", "Media Distancia", "Gran Distancia", "Ultra");
        cbTipo.setItems(listTipo);
        cbTipo.setValue("Selecciona Tipo");

        ObservableList<String> listFDist = FXCollections.observableArrayList("<Selecciona Tipo>", "< 5Km.", "5Km. - 25Km.", "25Km. - 50Km.", "> 50Km.");
        cbFiltroDistancia.setItems(listFDist);
        cbFiltroDistancia.setValue("Selecciona Tipo");

        //grupoBotones = new ToggleGroup();
        rbLugar.setToggleGroup(grupoBotones);
        rbDistancia.setToggleGroup(grupoBotones);

        try {
            listarRaces();
        } catch (SQLException sql) {
            AlertUtils.mostrarError("Error al cargar los datos");
        }

    }

    @FXML
    public void desconectar(Event event) {
        try {
            raceDAO.desconectar();
            lAviso.setText("Base de datos desconectada");
        } catch (SQLException sql) {
            AlertUtils.mostrarError("No se ha podido desconectar");
        }

    }

    @FXML
    public void getDatosTabla() {
        tfNombre.setText(tvRaces.getSelectionModel().selectedItemProperty().getValue().getNombre());
        tfLugar.setText(tvRaces.getSelectionModel().selectedItemProperty().getValue().getLugar());
        tfFecha.setText(tvRaces.getSelectionModel().selectedItemProperty().getValue().getFecha());
        tfDistancia.setText(String.valueOf(tvRaces.getSelectionModel().selectedItemProperty().getValue().getDistancia()));
        tfDesnivel.setText(String.valueOf(tvRaces.getSelectionModel().selectedItemProperty().getValue().getDesnivel()));
        cbTipo.setValue(String.valueOf(tvRaces.getSelectionModel().selectedItemProperty().getValue().getTipo()));
    }

    @FXML
    public void nuevo(Event event) {


    }

    @FXML
    public void guardar(Event event) {


    }

    @FXML
    public void modificar(Event event) {


    }

    @FXML
    public void eliminar(Event event) {


    }

    @FXML
    public void reset(Event event) {
        limpiaCajas();
    }

    @FXML
    public void cancelar(Event event) {


    }

    @FXML
    public void filtrar(Event event) {


    }

    @FXML
    public void importar(ActionEvent Event) {

    }

    @FXML
    public void exportar(ActionEvent Event) {

    }

    public void listarRaces() throws SQLException {

        listaRaces = FXCollections.observableArrayList(raceDAO.listarRaces());
        tvRaces.setItems(listaRaces);

        tcNombre.setCellValueFactory(new PropertyValueFactory<Race, String>("nombre"));
        tcLugar.setCellValueFactory(new PropertyValueFactory<Race, String>("lugar"));
        tcFecha.setCellValueFactory(new PropertyValueFactory<Race, String>("Fecha"));
        tcDistancia.setCellValueFactory(new PropertyValueFactory<Race, Integer>("distancia"));
        tcDesnivel.setCellValueFactory(new PropertyValueFactory<Race, Integer>("desnivel"));
        tcTipo.setCellValueFactory(new PropertyValueFactory<Race, String>("tipo"));
    }

    private void limpiaCajas() {
        tfNombre.setText("");
        tfLugar.setText("");
        tfFecha.setText("");
        tfDistancia.setText("");
        tfDesnivel.setText("");
        cbTipo.setValue("<Selecciona Tipo>");
        lAviso.setText("");
        tfNombre.requestFocus();
    }

}
