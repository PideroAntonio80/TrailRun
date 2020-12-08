package com.sanvalero.trailrun;

import com.sanvalero.trailrun.dao.RaceDAO;
import com.sanvalero.trailrun.domain.Race;
import com.sanvalero.trailrun.util.AlertUtils;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Creado por @ author: Pedro Orós
 * el 20/11/2020
 */
public class AppController implements Initializable {

    private final RaceDAO raceDAO;
    private WebEngine engine;
    private Race recuperarRace;

    public TextField tfNombre;
    public TextField tfLugar;
    public DatePicker dpFecha;
    public TextField tfDistancia;
    public TextField tfDesnivel;
    public ComboBox<String> cbTipo;

    public Button btNuevo;
    public Button btGuardar;
    public Button btModificar;
    public Button btEliminar;
    public Button btReset;
    public Button btCancelar;
    public Button btRecuperar;
    public Button btBorrarTodo;

    public CheckBox chbLugar;
    public CheckBox chbDistancia;
    public TextField tfFiltroLugar;
    public ComboBox<String> cbFiltroDistancia;

    public TableView<Race> tvRaces;
    public TableColumn<Race, String> tcNombre;
    public TableColumn<Race, String> tcLugar;
    public TableColumn<Race, Date> tcFecha;
    public TableColumn<Race, Integer> tcDistancia;
    public TableColumn<Race, Integer> tcDesnivel;
    public TableColumn<Race, String> tcTipo;
    ObservableList<Race> listaRaces;

    public Label lAviso;

    public WebView webView;
    public TextField tfUrl;

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

        try {

            ObservableList<String> listTipo = FXCollections.observableArrayList("<Selecciona Tipo>", "Km Vertical", "Media Distancia", "Gran Distancia", "Ultra");
            cbTipo.setItems(listTipo);
            cbTipo.setValue("Selecciona Tipo");

            ObservableList<String> listFDist = FXCollections.observableArrayList("<Selecciona Tipo>", "< 5Km.", "5Km. - 25Km.", "25Km. - 50Km.", "> 50Km.");
            cbFiltroDistancia.setItems(listFDist);
            cbFiltroDistancia.setValue("Selecciona Tipo");

            engine = webView.getEngine();
            engine.load("https://www.ign.es/iberpix2/visor/");

            lAviso.setText("Cargando cartografía, espere unos segundos...");
            transicionLabelAviso(9);

            btGuardar.setDisable(true);
            btEliminar.setDisable(true);
            btModificar.setDisable(true);
            btRecuperar.setDisable(true);

            listarRaces(raceDAO.listarRaces());
        } catch (SQLException sql) {
            AlertUtils.mostrarError("Error al cargar los datos");
        }

    }

    @FXML
    public void desconectar(Event event) {
        try {
            raceDAO.desconectar();
            lAviso.setText("Base de datos desconectada");
            transicionLabelAviso(3);
        } catch (SQLException sql) {
            AlertUtils.mostrarError("No se ha podido desconectar");
        }

    }

    @FXML
    public void getDatosTabla() {
        tfNombre.setText(tvRaces.getSelectionModel().selectedItemProperty().getValue().getNombre());
        tfLugar.setText(tvRaces.getSelectionModel().selectedItemProperty().getValue().getLugar());
        dpFecha.setValue(tvRaces.getSelectionModel().selectedItemProperty().getValue().getFecha().toLocalDate());
        tfDistancia.setText(String.valueOf(tvRaces.getSelectionModel().selectedItemProperty().getValue().getDistancia()));
        tfDesnivel.setText(String.valueOf(tvRaces.getSelectionModel().selectedItemProperty().getValue().getDesnivel()));
        cbTipo.setValue(tvRaces.getSelectionModel().selectedItemProperty().getValue().getTipo());

        btNuevo.setDisable(false);
        btGuardar.setDisable(true);
        btEliminar.setDisable(false);
        btModificar.setDisable(false);
        btCancelar.setDisable(false);
        btBorrarTodo.setDisable(false);
    }

    @FXML
    public void nuevo(Event event) {
        limpiaCajas();
        btNuevo.setDisable(true);
        btGuardar.setDisable(false);
        btEliminar.setDisable(true);
        btModificar.setDisable(true);
        btCancelar.setDisable(false);
        dpFecha.setValue(LocalDate.now());
    }

    @FXML
    public void guardar(Event event) {
        try {
            String nombre = tfNombre.getText();
            String lugar = tfLugar.getText();
            Date fecha = Date.valueOf(dpFecha.getValue());
            int distancia = Integer.parseInt(tfDistancia.getText());
            int desnivel = Integer.parseInt(tfDesnivel.getText());
            String tipo = cbTipo.getValue();
            Race race = new Race(nombre, lugar, fecha, distancia, desnivel, tipo);
            if(raceDAO.existeRace(race)) {
                AlertUtils.mostrarError("Esa carrera ya existe en la base de datos");
                return;
            }

            raceDAO.guardarRace(race);
            listarRaces(raceDAO.listarRaces());
            lAviso.setText("Registro guardado");
            transicionLabelAviso(3);

            btNuevo.setDisable(false);
            btGuardar.setDisable(true);
            btEliminar.setDisable(false);
            btModificar.setDisable(false);
            btRecuperar.setDisable(false);
            btCancelar.setDisable(false);
            btBorrarTodo.setDisable(false);

        } catch (SQLException sql) {
            AlertUtils.mostrarError("Error al guardar datos");
        }

    }

    @FXML
    public void modificar(Event event) {
        try {
            String nombreViejo = tvRaces.getSelectionModel().selectedItemProperty().getValue().getNombre();
            String lugarViejo = tvRaces.getSelectionModel().selectedItemProperty().getValue().getLugar();
            Date fechaVieja = tvRaces.getSelectionModel().selectedItemProperty().getValue().getFecha();
            if(nombreViejo.equals("") && lugarViejo.equals("") && fechaVieja.equals("")) {
                AlertUtils.mostrarError("Debes seleccionar una carrera en la tabla");
                return;
            }// TODO arreglar esto
            AlertUtils.mostrarConfirmacion("Modificación");
            String nombre = tfNombre.getText();
            String lugar = tfLugar.getText();
            Date fecha = Date.valueOf(dpFecha.getValue());
            int distancia = Integer.parseInt(tfDistancia.getText());
            int desnivel = Integer.parseInt(tfDesnivel.getText());
            String tipo = cbTipo.getValue();

            Race race = new Race(nombreViejo, lugarViejo, fechaVieja, nombre, lugar, fecha, distancia, desnivel, tipo);

            raceDAO.modificarRace(race);
            listarRaces(raceDAO.listarRaces());

            limpiaCajas();

            btEliminar.setDisable(true);
            btGuardar.setDisable(true);
            btModificar.setDisable(true);

            lAviso.setText("Registro modificado");
            transicionLabelAviso(3);
        } catch (SQLException sql) {
            AlertUtils.mostrarError("Error al modificar");
        }

    }

    @FXML
    public void eliminar(Event event) {
        try {
            AlertUtils.mostrarConfirmacion("Eliminación");

            String nombre = tfNombre.getText();
            String lugar = tfLugar.getText();
            Date fecha = Date.valueOf(dpFecha.getValue());
            int distancia = Integer.parseInt(tfDistancia.getText());
            int desnivel = Integer.parseInt(tfDesnivel.getText());
            String tipo = cbTipo.getValue();
            Race race = new Race(nombre, lugar, fecha, distancia, desnivel, tipo);
            recuperarRace = race;

            raceDAO.eliminarRace(race);
            listarRaces(raceDAO.listarRaces());
            limpiaCajas();

            btEliminar.setDisable(true);
            btGuardar.setDisable(true);
            btModificar.setDisable(true);
            btRecuperar.setDisable(false);

            lAviso.setText("Registro eliminado");
            transicionLabelAviso(3);
        } catch(SQLException sql) {
            AlertUtils.mostrarError("Error al eliminar");
        }

    }

    @FXML
    public void reset(Event event) {
        limpiaCajas();
        try {
            listarRaces(raceDAO.listarRaces());
        } catch (SQLException sql) {
            AlertUtils.mostrarError("Error al mostrar datos");
        }
    }

    @FXML
    public void cancelar(Event event) {
        AlertUtils.mostrarConfirmacion("Cancelar");
        limpiaCajas();

        btNuevo.setDisable(false);
        btEliminar.setDisable(true);
        btGuardar.setDisable(true);
        btModificar.setDisable(true);

    }

    @FXML
    public void filtrar(Event event) {
        try {
            String lugar = tfFiltroLugar.getText().toUpperCase();
            int opcion = 0;
            if(chbLugar.isSelected() && chbDistancia.isSelected()) {
                switch (cbFiltroDistancia.getValue()) {
                    case "< 5Km.":
                        opcion = 1;
                        break;
                    case "5Km. - 25Km.":
                        opcion = 2;
                        break;
                    case "25Km. - 50Km.":
                        opcion = 3;
                        break;
                    case "> 50Km.":
                        opcion = 4;
                        break;
                }
                listarRaces(raceDAO.filtroLugarDistancia(lugar, opcion));

            }  else if (chbDistancia.isSelected()){
                switch (cbFiltroDistancia.getValue()) {
                    case "< 5Km.":
                        opcion = 1;
                        break;
                    case "5Km. - 25Km.":
                        opcion = 2;
                        break;
                    case "25Km. - 50Km.":
                        opcion = 3;
                        break;
                    case "> 50Km.":
                        opcion = 4;
                        break;
                }
                listarRaces(raceDAO.filtroDistancia(opcion));
            }
            else if (chbLugar.isSelected()) {
                listarRaces(raceDAO.filtroLugar(lugar));
            }

            else if (!chbLugar.isSelected() && !chbDistancia.isSelected()){
                AlertUtils.mostrarError("Debes marcar al menos una opción");
            }
        } catch (SQLException sql) {
            AlertUtils.mostrarError("Error al filtrar datos");
        }

    }

    @FXML
    public void borrarTodo(ActionEvent event) {
        try {
            AlertUtils.mostrarConfirmacion("Borrar Todo");
            raceDAO.borrarTodo();
            listarRaces(raceDAO.listarRaces());
            lAviso.setText("Tabla borrada");
            transicionLabelAviso(3);
        } catch (SQLException sql) {
            AlertUtils.mostrarError("Error al borrar todos los datos");
        }

    }

    @FXML
    public void exportar(ActionEvent Event) {
        try {
            FileChooser fileChooser = new FileChooser();
            File fichero = fileChooser.showSaveDialog(null);
            FileWriter fileWriter = new FileWriter(fichero);

            CSVPrinter printer = new CSVPrinter(fileWriter, CSVFormat.TDF.withHeader("Nombre;", "Lugar;", "Fecha;", "Distancia;","Desnivel;", "Tipo;"));

            List<Race> races = raceDAO.listarRaces();
            for (Race race : races) {
                printer.printRecord(race.getNombre(), ';', race.getLugar(), ';', race.getFecha(), ';', race.getDistancia(), ';', race.getDesnivel(), ';', race.getTipo());
            }
            printer.close();
            lAviso.setText("Datos transferidos a su fichero");
            transicionLabelAviso(3);

        } catch (SQLException sql) {
            AlertUtils.mostrarError("Error al conectar con la base de datos");
        } catch (IOException ioe) {
            AlertUtils.mostrarError("Error al exportar los datos");
        }
    }

    @FXML
    public void cargarMapa(ActionEvent event) {
        String url = tfUrl.getText(); // Página de Instituto Nacional Geográfico --> https://www.ign.es/iberpix2/visor/
        engine.load(url);
    }

    @FXML
    public void recuperar(ActionEvent event) {
        try {
            raceDAO.guardarRace(recuperarRace);
            listarRaces(raceDAO.listarRaces());
            lAviso.setText("Registro recuperado");
            transicionLabelAviso(3);

            btRecuperar.setDisable(true);

        } catch (SQLException sql) {
            AlertUtils.mostrarError("Error al recuperar registro");
        }
    }

    public void listarRaces(List<Race> list) throws SQLException {

        listaRaces = FXCollections.observableArrayList(list);
        tvRaces.setItems(listaRaces);

        tcNombre.setCellValueFactory(new PropertyValueFactory<Race, String>("nombre"));
        tcLugar.setCellValueFactory(new PropertyValueFactory<Race, String>("lugar"));
        tcFecha.setCellValueFactory(new PropertyValueFactory<Race, Date>("fecha"));
        tcDistancia.setCellValueFactory(new PropertyValueFactory<Race, Integer>("distancia"));
        tcDesnivel.setCellValueFactory(new PropertyValueFactory<Race, Integer>("desnivel"));
        tcTipo.setCellValueFactory(new PropertyValueFactory<Race, String>("tipo"));
    }

    private void limpiaCajas() {
        tfNombre.setText("");
        tfLugar.setText("");
        dpFecha.setValue(null);
        tfDistancia.setText("");
        tfDesnivel.setText("");
        cbTipo.setValue("<Selecciona Tipo>");
        lAviso.setText("");
        chbLugar.setSelected(false);
        chbDistancia.setSelected(false);
        tfFiltroLugar.setText("");
        cbFiltroDistancia.setValue("<Selecciona Tipo>");
        tfUrl.setText("");
        tfNombre.requestFocus();
    }

    public void transicionLabelAviso(int segundos) {
        lAviso.setVisible(true);
        PauseTransition visiblePause = new PauseTransition((Duration.seconds(segundos)));
        visiblePause.setOnFinished(event -> lAviso.setVisible(false));
        visiblePause.play();
    }

}

