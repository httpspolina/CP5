drop database cp5;
create database cp5;
use cp5;

create table user
(
    id       int          not null primary key auto_increment,
    username varchar(255) not null unique,
    password varchar(255) not null,
    role     varchar(255) not null default 'CLIENT'
        check (role = 'CLIENT' or role = 'ADMIN' or role = 'SUPERVISOR')
);

create table client
(
    id    int          not null primary key,
    name  varchar(255) not null,
    email varchar(255) not null,
    phone varchar(255) not null,
    constraint foreign key (id) references user (id)
);

create table payment_method
(
    id          int          not null primary key auto_increment,
    client_id   int          not null,
    card_number varchar(255) not null
        check (card_number regexp '^[0-9]{16}$'),
    expires_at  date         not null,
    cardholder  varchar(255) not null,
    cvv         varchar(255) not null
        check (cvv regexp '^[0-9]{3}$'),
    constraint foreign key (client_id) references client (id)
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
    film_id     int      not null,
    client_id   int      not null,
    rating      int      not null,
    check (rating > 0 and rating < 6),
    description varchar(255),
    created_at  datetime not null default now(),
    constraint foreign key (film_id) references film (id),
    constraint foreign key (client_id) references client (id)
);

create table hall
(
    id    int          not null primary key auto_increment,
    name  varchar(255) not null,
    seats int          not null
        check ( seats > 0 and seats % 10 = 0 ),
    price float        not null
        check ( price > 0 )
);

create table session
(
    id      int      not null primary key auto_increment,
    film_id int      not null,
    hall_id int      not null,
    date    datetime not null,
    constraint foreign key (film_id) references film (id),
    constraint foreign key (hall_id) references hall (id)
);

create table `order`
(
    id                int          not null primary key auto_increment,
    client_id         int          not null,
    payment_method_id int          not null,
    session_id        int          not null,
    seat_index        int          not null
        check (seat_index > 0),
    status            varchar(255) not null default 'PAYED'
        check (status = 'PAYED' or status = 'CANCELED'),
    created_at        datetime     not null default now(),
    constraint foreign key (client_id) references client (id),
    constraint foreign key (payment_method_id) references payment_method (id),
    constraint foreign key (session_id) references session (id)
);
