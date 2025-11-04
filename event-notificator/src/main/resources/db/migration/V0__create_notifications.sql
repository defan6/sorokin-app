create table notifications (
    id bigserial primary key,
    username varchar(255) not null,
    event_id bigint not null,
    message varchar(255) null,
    booking_status varchar(50) not null,
    read_status varchar(50) not null,
    registered_at timestamp default current_timestamp
);