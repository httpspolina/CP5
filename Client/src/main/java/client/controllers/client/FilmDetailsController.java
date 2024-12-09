package client.controllers.client;

import client.controllers.AbstractController;
import common.command.Response;
import common.command.SuccessResponse;
import common.command.client.AddReviewRequest;
import common.command.client.FilmResponse;
import common.command.client.FindFilmByIdRequest;
import common.model.Film;
import common.model.Review;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.util.stream.Collectors;

public class FilmDetailsController extends AbstractController {
    @FXML
    private TextArea filmDetailsTextArea;

    @FXML
    private ComboBox<Integer> ratingComboBox;

    @FXML
    private TextField reviewTextField;

    @FXML
    private Button addReviewButton;

    @FXML
    private TextArea reviewTextArea;

    private Film film;

    @FXML
    private Text filmTitleText;

    @FXML
    public void initialize() {
        super.initialize();
        initializeRatingComboBox();
        reviewTextArea.setEditable(false);
        filmDetailsTextArea.setEditable(false);
        addReviewButton.setDisable(true);
    }

    public void setFilm(Film film) {
        this.film = film;
        loadFilmDetails();
    }

    public void loadFilmDetails() {
        if (film == null) return;
        try {
            FindFilmByIdRequest request = new FindFilmByIdRequest();
            request.setFilmId(film.getId());

            Response response = call(request);

            if (response instanceof FilmResponse filmResponse) {
                String filmDetails = getFilmDetails(filmResponse.getFilm());
                film = filmResponse.getFilm();
                filmDetailsTextArea.setText(filmDetails);

                String formattedReviews = film.getReviews().stream()
                        .map(review -> formatReview(review))
                        .collect(Collectors.joining("\n\n"));

                reviewTextArea.setText(formattedReviews.isEmpty() ? "Отзывов пока нет." : formattedReviews);

                filmTitleText.setText(film.getTitle());
            } else {
                showErrorAlert("Не удалось получить фильм.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showErrorAlert("Ошибка при загрузке фильма.");
        }
    }

    private String formatReview(Review review) {
        return "Имя пользователя: " + review.getClient().getUsername() + "\n" +
                "Дата: " + review.getCreatedAt() + "\n" +
                "Текст отзыва: " + review.getDescription();
    }

    private String getFilmDetails(Film film) {
        return "Название: " + film.getTitle() + "\n" +
                "Описание: " + film.getDescription() + "\n" +
                "Жанр: " + film.getGenre() + "\n" +
                "Год: " + film.getYear() + "\n" +
                "Рейтинг: " + Math.round(film.getRating() * 100.0) / 100.0 + "\n";
    }

    @FXML
    public void addReview() {
        try {
            Integer rating = ratingComboBox.getValue();
            String reviewText = reviewTextField.getText();

            if (rating == null || reviewText.isEmpty()) {
                showErrorAlert("Пожалуйста, выберите рейтинг и введите текст отзыва.");
                return;
            }

            AddReviewRequest request = new AddReviewRequest();
            request.setFilmId(film.getId());
            request.setRating(rating);
            request.setDescription(reviewText);

            Response response = call(request);

            if (response instanceof SuccessResponse) {
                showSuccessAlert("Отзыв успешно добавлен!");
                reviewTextField.clear();
                ratingComboBox.setValue(null);
                addReviewButton.setDisable(true);
                loadFilmDetails();
            } else {
                showErrorAlert("Не удалось добавить отзыв.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showErrorAlert("Произошла ошибка при добавлении отзыва.");
        }
    }

    private void showSuccessAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Успех");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void initializeRatingComboBox() {
        for (int i = 1; i <= 5; i++) {
            ratingComboBox.getItems().add(i);
        }

        ratingComboBox.setOnAction(event -> onRatingSelected());
    }

    public void onRatingSelected() {
        addReviewButton.setDisable(ratingComboBox.getValue() == null);
    }

    public void goBack(ActionEvent actionEvent) {
        switchPage("/client/main.fxml");
    }

    public void chooseFilm(ActionEvent actionEvent) {
        HallsPageController controller = switchPage("/client/halls.fxml");
        controller.setFilm(film);
    }
}
