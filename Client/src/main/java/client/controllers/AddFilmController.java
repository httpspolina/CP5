package client.controllers;

import client.ClientConfig;
import client.models.Film;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.ObjectOutputStream;
import java.net.Socket;

public class AddFilmController {

    private static final String SERVER_ADDRESS = ClientConfig.INSTANCE.getServerHost();
    private static final int SERVER_PORT = ClientConfig.INSTANCE.getServerPort();
    public Button addFilmButton;

    @FXML
    private TextField titleField;
    @FXML
    private TextField countryField;
    @FXML
    private TextField yearField;
    @FXML
    private TextField directorField;
    @FXML
    private TextField rolesField;
    @FXML
    private TextField genreField;
    @FXML
    private TextArea descriptionField;
    @FXML
    private TextField posterUrlField;

    @FXML
    public void addFilm(ActionEvent event) {
        try {
            String title = titleField.getText();
            String country = countryField.getText();
            int year = Integer.parseInt(yearField.getText());
            String director = directorField.getText();
            String roles = rolesField.getText();
            String genre = genreField.getText();
            String description = descriptionField.getText();
            String posterUrl = posterUrlField.getText();

            Film newFilm = new Film(title, country, year, director, roles, genre, description, posterUrl);

            try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
                 ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream())) {
                output.writeObject("ADD_FILM");
                output.writeObject(newFilm);
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Успех");
            alert.setHeaderText(null);
            alert.setContentText("Фильм успешно добавлен!");
            alert.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText(null);
            alert.setContentText("Не удалось добавить фильм. Проверьте данные.");
            alert.showAndWait();
        }
    }
}
