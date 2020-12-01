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
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
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
    public TextField tfFecha;
    public TextField tfDistancia;
    public TextField tfDesnivel;
    public ComboBox<String> cbTipo;

    public CheckBox chbLugar;
    public CheckBox chbDistancia;
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
        ObservableList<String> listTipo = FXCollections.observableArrayList("<Selecciona Tipo>", "Km Vertical", "Media Distancia", "Gran Distancia", "Ultra");
        cbTipo.setItems(listTipo);
        cbTipo.setValue("Selecciona Tipo");

        ObservableList<String> listFDist = FXCollections.observableArrayList("<Selecciona Tipo>", "< 5Km.", "5Km. - 25Km.", "25Km. - 50Km.", "> 50Km.");
        cbFiltroDistancia.setItems(listFDist);
        cbFiltroDistancia.setValue("Selecciona Tipo");

        engine = webView.getEngine();

        try {
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
        cbTipo.setValue(tvRaces.getSelectionModel().selectedItemProperty().getValue().getTipo());
    }

    @FXML
    public void nuevo(Event event) {


    }

    @FXML
    public void guardar(Event event) {
        try {
            String nombre = tfNombre.getText();
            String lugar = tfLugar.getText();
            String fecha = tfFecha.getText();
            int distancia = Integer.parseInt(tfDistancia.getText());
            int desnivel = Integer.parseInt(tfDesnivel.getText());
            String tipo = cbTipo.getValue();
            Race race = new Race(nombre, lugar, fecha, distancia, desnivel, tipo);

            raceDAO.guardarRace(race);
            lAviso.setText("Registro guardado");
            listarRaces(raceDAO.listarRaces());
        } catch (SQLException sql) {
            AlertUtils.mostrarError("Error al guardar datos");
        }

    }

    @FXML
    public void modificar(Event event) {
        try {
            String nombreViejo = tvRaces.getSelectionModel().selectedItemProperty().getValue().getNombre();
            String lugarViejo = tvRaces.getSelectionModel().selectedItemProperty().getValue().getLugar();
            String fechaVieja = tvRaces.getSelectionModel().selectedItemProperty().getValue().getFecha();
            String nombre = tfNombre.getText();
            String lugar = tfLugar.getText();
            String fecha = tfFecha.getText();
            int distancia = Integer.parseInt(tfDistancia.getText());
            int desnivel = Integer.parseInt(tfDesnivel.getText());
            String tipo = cbTipo.getValue();

            Race race = new Race(nombreViejo, lugarViejo, fechaVieja, nombre, lugar, fecha, distancia, desnivel, tipo);

            raceDAO.modificarRace(race);
            lAviso.setText("Registro modificado");
            listarRaces(raceDAO.listarRaces());
        } catch (SQLException sql) {
            AlertUtils.mostrarError("Error al modificar");
        }

    }

    @FXML
    public void eliminar(Event event) {
        try {
            String nombre = tfNombre.getText();
            String lugar = tfLugar.getText();
            String fecha = tfFecha.getText();
            int distancia = Integer.parseInt(tfDistancia.getText());
            int desnivel = Integer.parseInt(tfDesnivel.getText());
            String tipo = cbTipo.getValue();
            Race race = new Race(nombre, lugar, fecha, distancia, desnivel, tipo);
            recuperarRace = race;

            raceDAO.eliminarRace(race);
            lAviso.setText("Registro eliminado");
            listarRaces(raceDAO.listarRaces());
        } catch(SQLException sql) {
            AlertUtils.mostrarError("Error al eliminar");
        }

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
    public void showAll(ActionEvent event) {
        try {
            listarRaces(raceDAO.listarRaces());
        } catch (SQLException sql) {
            AlertUtils.mostrarError("Error al mostrar datos");
        }

    }

    @FXML
    public void importar(ActionEvent Event) {

    }

    @FXML
    public void exportar(ActionEvent Event) {
        try {
            FileChooser fileChooser = new FileChooser();
            File fichero = fileChooser.showSaveDialog(null);
            FileWriter fileWriter = new FileWriter(fichero);

            CSVPrinter printer = new CSVPrinter(fileWriter, CSVFormat.DEFAULT.withHeader("Id", "Nombre", "Lugar", "Fecha", "Distancia","Desnivel", "Tipo"));

            List<Race> races = raceDAO.listarRaces();
            for (Race race : races) {
                printer.printRecord(race.getId(), race.getNombre(), race.getLugar(), race.getFecha(), race.getDistancia(), race.getDesnivel(), race.getTipo());
            }
            printer.close();
            lAviso.setText("Datos transferidos a su fichero");
        } catch (SQLException sql) {
            AlertUtils.mostrarError("Error al conectar con la base de datos");
        } catch (IOException ioe) {
            AlertUtils.mostrarError("Error al exportar los datos");
        }
    }

    @FXML
    public void cargarMapa(ActionEvent event) {
        /*tarea = new Tarea(engine, webView, tfUrl);
        tarea.start();*/

        String url = tfUrl.getText(); // Página de Instituto Nacional Geográfico --> https://www.ign.es/iberpix2/visor/

        if (url.equals("")) {
            engine.load("https://www.ign.es/iberpix2/visor/");
        } else {
            engine.load(url);
        }

    }

    @FXML
    public void recuperar(ActionEvent event) {
        try {
            raceDAO.guardarRace(recuperarRace);
            lAviso.setText("Registro recuperado");
            listarRaces(raceDAO.listarRaces());

        } catch (SQLException sql) {
            AlertUtils.mostrarError("Error al recuperar registro");
        }
    }

    public void listarRaces(List<Race> list) throws SQLException {

        listaRaces = FXCollections.observableArrayList(list);
        tvRaces.setItems(listaRaces);

        tcNombre.setCellValueFactory(new PropertyValueFactory<Race, String>("nombre"));
        tcLugar.setCellValueFactory(new PropertyValueFactory<Race, String>("lugar"));
        tcFecha.setCellValueFactory(new PropertyValueFactory<Race, String>("fecha"));
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
        chbLugar.setSelected(false);
        chbDistancia.setSelected(false);
        tfFiltroLugar.setText("");
        cbFiltroDistancia.setValue("<Selecciona Tipo>");
        tfUrl.setText("");
        tfNombre.requestFocus();
    }

}

/*--------------------------------TODO TAREAS PENDIENTES---------------------------------*/
// Todo Mostrar Alerts de confirmación
// Todo Habilitar/Deshabilitar botones según proceda
// Todo Metodo para Eliminar todos los registros.
// Todo Pulir el formato csv del metodo Exportar para que se separe con punto y coma
// Todo Hacer fechas tipo Date y utilizar DatePicker en vez de TextField de fecha
// Todo Revisión general
