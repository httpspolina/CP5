package client.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;

public class FilmController extends BaseController {

    @FXML
    private Text filmTitle;
    @FXML
    private TextArea filmDescription;

    // Метод для установки данных фильма
    public void setFilmDetails(String title, String description) {
        filmTitle.setText(title);
        filmDescription.setText(description);
    }
}
