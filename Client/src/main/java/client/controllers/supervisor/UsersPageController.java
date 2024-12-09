package client.controllers.supervisor;

import client.controllers.AbstractController;
import common.command.ErrorResponse;
import common.command.Response;
import common.command.supervisor.FindAllUsersRequest;
import common.command.supervisor.UsersResponse;
import common.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class UsersPageController extends AbstractController {
    @FXML
    public ListView<User> usersListView;
    @FXML
    public TextField userSearchField;

    @Override
    public void initialize() {
        super.initialize();
        usersListView.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(User user, boolean empty) {
                super.updateItem(user, empty);
                if (empty || user == null) {
                    setText(null);
                } else {
                    String userText;
                    userText = "Имя пользователя: " + user.getUsername() + "\n" +
                            "Роль: " + user.getRole();
                    setText(userText);
                }
            }
        });

        loadAllUsers();
    }

    private void loadAllUsers() {
        try {
            Response res = call(new FindAllUsersRequest());
            if (res instanceof UsersResponse usersResponse) {
                usersListView.getItems().clear();
                usersListView.getItems().addAll(usersResponse.getUsers());
            } else if (res instanceof ErrorResponse errorResponse) {
                showErrorAlert(errorResponse.getMessage());
            } else {
                showErrorAlert("Не удалось загрузить список пользователей.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showErrorAlert("Ошибка при загрузке пользователей.");
        }
    }

    public void goBack(ActionEvent actionEvent) {
        switchPage("/supervisor/main.fxml");
    }

    public void goToUserDetails(MouseEvent mouseEvent) {
        User selectedUser = usersListView.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            UserDetailsController controller = switchPage("/supervisor/user.fxml");
            controller.setUser(selectedUser);
        }
    }

    public void toSearchUser(ActionEvent actionEvent) {

    }

    public void showAllUsers(ActionEvent actionEvent) {
        loadAllUsers();
    }
}
