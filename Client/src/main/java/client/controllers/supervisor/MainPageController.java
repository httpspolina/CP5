package client.controllers.supervisor;

import client.controllers.AbstractController;
import javafx.event.ActionEvent;

public class MainPageController extends AbstractController {
    public void toWorkWithUsers(ActionEvent actionEvent) {
        switchPage("/supervisor/users.fxml");
    }
}
