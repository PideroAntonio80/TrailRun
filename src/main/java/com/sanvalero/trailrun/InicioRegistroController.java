package com.sanvalero.trailrun;

import com.sanvalero.trailrun.dao.InicioDAO;
import com.sanvalero.trailrun.domain.Usuario;
import com.sanvalero.trailrun.util.AlertUtils;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Creado por @ author: Pedro Orós
 * el 30/11/2020
 */
public class InicioRegistroController {

    private final InicioDAO inicioDAO;

    public TextField tfUsuario, tfUsuarioRegistro, tfNombreRegistro, tfEmailRegistro, tfPasswordRegistro;
    public PasswordField pfPassword;
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
    public void iniciar(ActionEvent event) {
        int opcion = 0;
        String usuario = tfUsuario.getText();
        String password = pfPassword.getText();
        Usuario user = new Usuario(usuario, password);
        try {
            opcion = inicioDAO.iniciarSesion(user);
            if (opcion == 1) {
                cerrarVentana(event);
            }
            else {

                AlertUtils.mostrarError("Usuario y/o password incorrectos");
            }
        } catch (SQLException sql) {
            AlertUtils.mostrarError("Error al iniciar sesión");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void enviar(ActionEvent event) {

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
            transicionLabelConfirmacion();

        } catch (SQLException sql) {
            AlertUtils.mostrarError("Error al registrar");
        }
    }

    @FXML
    public void reset(ActionEvent event) {
        tfUsuarioRegistro.setText("");
        tfNombreRegistro.setText("");
        tfEmailRegistro.setText("");
        tfPasswordRegistro.setText("");
    }

    public void cerrarVentana(ActionEvent event){
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    public void transicionLabelConfirmacion() {
        lConfirmacionRegistro.setVisible(true);
        PauseTransition visiblePause = new PauseTransition((Duration.seconds(3)));
        visiblePause.setOnFinished(event -> lConfirmacionRegistro.setVisible(false));
        visiblePause.play();
    }

}

