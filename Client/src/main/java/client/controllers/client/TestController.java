package client.controllers.client;

import client.controllers.AbstractController;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ThreadLocalRandom;

public class TestController extends AbstractController {
    public GridPane grid;

    private Integer seats = 100;
    private Set<Integer> occupiedSeats = new HashSet<>();
    private Set<Integer> selectedSeats = new TreeSet<>();

    public void initialize() {
        super.initialize();
        for (int i = 1; i <= seats; i++) {
            if (ThreadLocalRandom.current().nextBoolean()) {
                occupiedSeats.add(i);
            }
        }
        for (int rowIndex = 0; rowIndex < seats / 10; rowIndex++) {
            for (int columnIndex = 0; columnIndex < 10; columnIndex++) {
                var seatIndex = 10 * rowIndex + columnIndex + 1;
                Button seat = new Button(String.valueOf(seatIndex));
                seat.setPrefWidth(50.0);
                grid.add(seat, columnIndex, rowIndex);

                if (occupiedSeats.contains(seatIndex)) {
                    seat.setBackground(Background.fill(Color.GRAY));
                    continue;
                }
                seat.setBackground(Background.fill(Color.WHITE));
                seat.setOnMouseClicked(event -> {
                    if (!selectedSeats.contains(seatIndex)) {
                        selectedSeats.add(seatIndex);
                        seat.setBackground(Background.fill(Color.GREEN));
                    } else {
                        selectedSeats.remove(seatIndex);
                        seat.setBackground(Background.fill(Color.WHITE));
                    }
                    System.out.println(selectedSeats);
                });
            }
        }
    }
}
