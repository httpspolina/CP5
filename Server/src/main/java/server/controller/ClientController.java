package server.controller;

import common.command.CommonErrorResponse;
import common.command.Response;
import common.command.SuccessResponse;
import common.command.client.*;
import common.model.*;
import server.db.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ClientController {

    private final ClientRepository clientRepository = new ClientRepository();
    private final FilmRepository filmRepository = new FilmRepository();
    private final HallRepository hallRepository = new HallRepository();
    private final OrderRepository orderRepository = new OrderRepository();
    private final PaymentMethodRepository paymentMethodRepository = new PaymentMethodRepository();
    private final ReviewRepository reviewRepository = new ReviewRepository();
    private final SessionRepository sessionRepository = new SessionRepository();

    public Response login(ClientLoginRequest req) throws Exception {
        Client foundClient = clientRepository.findByUsername(req.getUsername().toLowerCase());
        if (foundClient == null || foundClient.getRole() != UserRole.CLIENT || !PasswordHashing.verifyPassword(req.getPassword(), foundClient.getPassword())) {
            return new CommonErrorResponse("Неправильное имя пользователя или пароль");
        }
        return new ClientResponse(foundClient);
    }

    public Response register(ClientRegisterRequest req) throws Exception {
        Client existingClient = clientRepository.findByUsername(req.getUsername().toLowerCase());
        if (existingClient != null) {
            return new CommonErrorResponse("Пользователь с таким именем уже существует");
        }
        Client newClient = new Client();
        newClient.setUsername(req.getUsername().toLowerCase());
        newClient.setPassword(PasswordHashing.hashPassword(req.getPassword()));
        newClient.setRole(UserRole.CLIENT);
        newClient.setName(req.getName() != null ? req.getName() : "Введите имя");
        newClient.setEmail(req.getEmail() != null ? req.getEmail() : "Введите почту");
        newClient.setPhone(req.getPhone() != null ? req.getPhone() : "Введите телефон");
        Integer newUserId = clientRepository.create(newClient);
        if (newUserId == null) {
            return new CommonErrorResponse("Не удалось создать клиента");
        }
        Client foundClient = clientRepository.findByUsername(req.getUsername().toLowerCase());
        return new ClientResponse(foundClient);
    }

    public Response update(Integer currentClientId, UpdateClientRequest req) throws Exception {
        int updated = clientRepository.update(currentClientId, req.getClient());
        if (updated == 0) {
            return new CommonErrorResponse("Не удалось обновить клиента");
        }
        Client foundClient = clientRepository.findByUsername(req.getClient().getUsername().toLowerCase());
        return new ClientResponse(foundClient);
    }

    public Response findAllFilms(FindAllFilmsRequest req) throws Exception {
        List<Film> films = filmRepository.findAll();
        return new FilmsResponse(films);
    }

    public Response findFilmById(FindFilmByIdRequest req) throws Exception {
        Film film = filmRepository.findById(req.getFilmId());
        if (film == null) {
            return new CommonErrorResponse("Не удалось найти фильм");
        }
        return new FilmResponse(film);
    }

    public Response addReview(Integer currentClientId, AddReviewRequest req) {
        Review review = new Review();
        review.setFilmId(req.getFilmId());
        review.setClientId(currentClientId);
        review.setRating(req.getRating());
        review.setDescription(req.getDescription());
        Integer newReviewId = reviewRepository.create(review);
        if (newReviewId == null) {
            return new CommonErrorResponse("Не удалось добавить отзыв");
        }
        return SuccessResponse.INSTANCE;
    }

    public Response findHalls(FindHallsRequest req) {
        List<Hall> halls = hallRepository.findByFilmId(req.getFilmId());
        return new HallsResponse(halls);
    }

    public Response findSessions(FindSessionsRequest req) {
        List<Session> sessions = sessionRepository.findByFilmIdAndHallId(req.getFilmId(), req.getHallId());
        return new SessionsResponse(sessions);
    }

    public Response findSessionById(FindSessionByIdRequest req) {
        Session session = sessionRepository.findById(req.getSessionId());
        if (session == null) {
            return new CommonErrorResponse("Не удалось найти сеанс");
        }
        return new SessionResponse(session);
    }

    public Response createPaymentMethod(Integer currentClientId, CreatePaymentMethodRequest req) {
        PaymentMethod paymentMethod = req.getPaymentMethod();
        paymentMethod.setClientId(currentClientId);
        Integer newId = paymentMethodRepository.create(paymentMethod);
        if (newId == null) {
            return new CommonErrorResponse("Не удалось добавить способ оплаты");
        }
        return SuccessResponse.INSTANCE;
    }

    public Response findPaymentMethodsByClientId(Integer currentClientId, FindMyPaymentMethodsRequest req) {
        List<PaymentMethod> paymentMethods = paymentMethodRepository.findByClientId(currentClientId);
        return new PaymentMethodsResponse(paymentMethods);
    }

    public Response createOrder(Integer currentClientId, CreateOrderRequest req) {
        Integer sessionId = req.getSessionId();
        Integer paymentMethodId = req.getPaymentMethodId();
        Set<Integer> seatIndexes = req.getSelectedSeatIndexes();
        List<Order> orders = new ArrayList<>();
        for (Integer seatIndex : seatIndexes) {
            Order order = new Order();
            order.setClientId(currentClientId);
            order.setPaymentMethodId(paymentMethodId);
            order.setSessionId(sessionId);
            order.setSeatIndex(seatIndex);
            order.setStatus(OrderStatus.PAYED);
            Integer newOrderId = orderRepository.create(order);
            Order newOrder = orderRepository.findById(newOrderId);
            if (newOrder != null) {
                orders.add(newOrder);
            }
        }
        return new OrdersResponse(orders);
    }

    public Response findOrdersByClientId(Integer currentClientId, FindOrdersRequest req) {
        List<Order> orders = orderRepository.findByClientId(currentClientId);
        return new OrdersResponse(orders);
    }

    public Response updateOrderStatus(UpdateOrderStatusRequest req) {
        Integer orderId = req.getOrderId();
        OrderStatus newStatus = req.getStatus();

        Order order = orderRepository.findById(orderId);
        if (order == null) {
            return new CommonErrorResponse("Заказ не найден.");
        }

        // Обновляем статус заказа
        order.setStatus(newStatus);
        boolean updated = orderRepository.updateStatus(order);
        if (updated) {
            return SuccessResponse.INSTANCE;
        } else {
            return new CommonErrorResponse("Не удалось обновить статус заказа.");
        }
    }

    public Response findFilmByTitle(FindFilmByTitleRequest req) throws Exception {
        String title = req.getTitle();
        Film film = filmRepository.findByTitle(title);
        if (film == null) {
            return new CommonErrorResponse("Фильм не найден");
        }
        return new FilmResponse(film);
    }
}
