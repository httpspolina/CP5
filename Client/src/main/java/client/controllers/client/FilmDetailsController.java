package client.controllers.client;

import client.controllers.AbstractController;
import common.command.Response;
import common.command.SuccessResponse;
import common.command.client.AddReviewRequest;
import common.command.client.FilmResponse;
import common.command.client.FindFilmByIdRequest;
import common.model.Film;
import common.model.Review;
import javafx.fxml.FXML;
import javafx.scene.control.*;

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

    private Integer filmId;

    @FXML
    public void initialize() {
        super.initialize();
        initializeRatingComboBox();
        addReviewButton.setDisable(true);
    }

    public void loadFilmDetails() {
        if (filmId == null) return;
        try {
            FindFilmByIdRequest request = new FindFilmByIdRequest();
            request.setFilmId(filmId);

            Response response = call(request);

            if (response instanceof FilmResponse) {
                FilmResponse filmResponse = (FilmResponse) response;
                String filmDetails = getFilmDetails(filmResponse.getFilm());

                filmDetailsTextArea.setText(filmDetails);
                reviewTextArea.setText(filmResponse.getFilm().getReviews().stream().map(Review::toString).collect(Collectors.joining("\n\n")));
            } else {
                showErrorAlert("Не удалось получить фильм.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showErrorAlert("Ошибка при загрузке фильма.");
        }
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
            request.setFilmId(1);
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

    public void setFilmId(Integer filmId) {
        this.filmId = filmId;
        loadFilmDetails();
    }
}
