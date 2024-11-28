create database cp5;

create table user
(
    id       int          not null primary key auto_increment,
    username varchar(255) not null unique,
    password varchar(255) not null,
    role     varchar(255) not null default 'client'
        check (role = 'client' or role = 'admin' or role = 'supervisor')
);

create table client
(
    id    int          not null primary key
        references user (id),
    name  varchar(255) not null,
    email varchar(255) not null,
    phone varchar(255) not null
);

create table payment_method
(
    id          int          not null primary key auto_increment,
    client_id   int          not null
        references client (id),
    card_number varchar(255) not null
        check (card_number regexp '^[0-9]{16}$'),
    expires_at  date         not null,
    cardholder  varchar(255) not null,
    cvv         varchar(255) not null
        check (cvv regexp '^[0-9]{3}$')

);

create table film
(
    id          int          not null primary key auto_increment,
    title       varchar(255) not null,
    country     varchar(255) not null,
    year        int          not null
        check (year >= 1800 and year <= 9999),
    director    varchar(255) not null,
    roles       varchar(255) not null,
    genre       varchar(255) not null,
    description varchar(255) not null,
    poster_url  varchar(255) not null
);

create table review
(
    id          int      not null primary key auto_increment,
    film_id     int      not null
        references film (id),
    client      int      not null
        references client (id),
    rating      int      not null
        check (rating > 0 and rating < 6),
    description varchar(255),
    created_at  datetime not null default now()
);

create table cinema
(
    id      int          not null primary key auto_increment,
    name    varchar(255) not null,
    address varchar(255) not null
);

create table cinema_hall
(
    id        int          not null primary key auto_increment,
    cinema_id int          not null
        references cinema (id),
    name      varchar(255) not null,
    seats     int          not null
        check ( seats > 0 and seats % 10 = 0 ),
    price     float        not null
        check ( price > 0 )
);

create table cinema_session
(
    id        int      not null primary key auto_increment,
    film_id   int      not null
        references film (id),
    cinema_id int      not null
        references cinema (id),
    hall_id   int      not null
        references cinema_hall (id),
    date      datetime not null
);

create table `order`
(
    id                int          not null primary key auto_increment,
    client_id         int          not null
        references client (id),
    payment_method_id int          not null
        references payment_method (id),
    cinema_session_id int          not null
        references cinema_session (id),
    seat_index        int          not null
        check (seat_index > 0),
    status            varchar(255) not null default 'payed'
        check (status = 'payed' or status = 'canceled'),
    created_at        datetime     not null default now()
);
