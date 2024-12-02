package client.controllers.admin;

import client.controllers.AbstractController;
import javafx.event.ActionEvent;

public class MainPageController extends AbstractController {
    public void toWorkWithFilms(ActionEvent actionEvent) { switchPage("/admin/films.fxml"); }
}
