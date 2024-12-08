package client.controllers.supervisor;

import client.controllers.AbstractController;
import common.command.CommonErrorResponse;
import common.command.Response;
import common.command.supervisor.DeleteUserRequest;
import common.command.supervisor.FindUserByIdRequest;
import common.command.supervisor.UserResponse;
import common.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;

public class UserDetailsController extends AbstractController {
    @FXML
    public TextArea userDetailsTextArea;
    @FXML
    public Text usernameTitleText;

    private User user;

    public void setUser(User selectedUser) {
        this.user = selectedUser;
        loadUserDetails();
        userDetailsTextArea.setEditable(false);
    }

    public void loadUserDetails() {
        if (user == null) return;

        try {
            FindUserByIdRequest request = new FindUserByIdRequest();
            request.setUserId(user.getId());

            Response response = call(request);

            if (response instanceof UserResponse userResponse) {
                User userFromResponse = userResponse.getUser();

                String userDetails = getUserDetails(userFromResponse);

                userDetailsTextArea.setText(userDetails);

                usernameTitleText.setText("Имя пользователя: " + userFromResponse.getUsername());
            } else {
                showErrorAlert("Не удалось получить информацию о пользователе.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showErrorAlert("Ошибка при загрузке данных о пользователе.");
        }
    }

    private String getUserDetails(User user) {
        return "Роль: " + user.getRole() + "\n" +
                "Имя пользователя: " + user.getUsername() + "\n" +
                "Пароль: Не отображается\n";
    }

    public void goBack(ActionEvent actionEvent) {
        switchPage("/supervisor/users.fxml");
    }

    public void toDeleteUser(ActionEvent actionEvent) {
        if (user == null) {
            showErrorAlert("Пользователь не выбран для удаления.");
            return;
        }

        try {
            DeleteUserRequest request = new DeleteUserRequest();
            request.setUserId(user.getId());

            Response response = call(request);

            if (response instanceof CommonErrorResponse errorResponse) {
                String message = errorResponse.getMessage();
                if (message.equalsIgnoreCase("Пользователь успешно удалён")) {
                    showSuccessAlert(message);
                } else {
                    showErrorAlert("Не удалось удалить пользователя: " + message);
                }
            } else {
                showSuccessAlert("Пользователь успешно удалён.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showErrorAlert("Произошла ошибка при удалении пользователя.");
        }
    }


    private void showSuccessAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Успех");
        alert.setHeaderText(message);

        ButtonType goToFilms = new ButtonType("Перейти к пользователям");
        alert.getButtonTypes().setAll(goToFilms);

        alert.showAndWait().ifPresent(response -> {
            if (response == goToFilms) {
                switchPage("/supervisor/users.fxml");
                alert.close();
            }
        });
    }
}
