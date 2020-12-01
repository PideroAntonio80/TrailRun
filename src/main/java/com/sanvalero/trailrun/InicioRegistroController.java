package com.sanvalero.trailrun;

import com.sanvalero.trailrun.dao.RaceDAO;
import com.sanvalero.trailrun.dao.InicioDAO;
import com.sanvalero.trailrun.domain.Usuario;
import com.sanvalero.trailrun.util.AlertUtils;
import com.sanvalero.trailrun.util.R;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Creado por @ author: Pedro Orós
 * el 30/11/2020
 */
public class InicioRegistroController {

    private final InicioDAO inicioDAO;

    public TextField tfUsuario, tfPassword, tfUsuarioRegistro, tfNombreRegistro, tfEmailRegistro, tfPasswordRegistro;
    public Label lConfirmacionRegistro;

    public InicioRegistroController() {
        inicioDAO = new InicioDAO();

        try {
            inicioDAO.conectar();
        } catch (ClassNotFoundException cnfe) {
            AlertUtils.mostrarError("Error al iniciar la aplicación");
        } catch (SQLException sql) {
            AlertUtils.mostrarError("No se ha podido conectar");
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    @FXML
    public void iniciar(Event event) {
        int opcion = 0;
        String usuario = tfUsuario.getText();
        String password = tfPassword.getText();
        Usuario user = new Usuario(usuario, password);
        try {
            opcion = inicioDAO.iniciarSesion(user);
            if (opcion == 1) {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(R.getUI("trailrun.fxml"));
                loader.setController(new AppController());
                VBox vbox = loader.load();

                Scene scene = new Scene(vbox);
                Stage appStage = new Stage();
                appStage.setScene(scene);
                appStage.show();

            }
            else {
                AlertUtils.mostrarError("Usuario y/o password incorrectos");
            }
        } catch (SQLException | IOException sql) {
            AlertUtils.mostrarError("Error al iniciar sesión");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void enviar(Event event) {

        String usuario = tfUsuarioRegistro.getText();
        String nombre = tfNombreRegistro.getText();
        String email = tfEmailRegistro.getText();
        String password = tfPasswordRegistro.getText();

        Usuario user = new Usuario(usuario, nombre, email, password);
        try {
            if(inicioDAO.existeUsuario(user.getUsuario())) {
                AlertUtils.mostrarError("Ese nombre de usuario ya existe, elige otro");
                return;
            }
            inicioDAO.guardarUsuario(user);
            lConfirmacionRegistro.setText("Usuario registrado");
        } catch (SQLException sql) {
            AlertUtils.mostrarError("Error al registrar");
        }
    }

}

