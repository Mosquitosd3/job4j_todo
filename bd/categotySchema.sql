create table category(
    id serial primary key,
    name varchar(255)
);

insert into category(name)
values
       ('Работа'),
       ('Дом'),
       ('Учеба'),
       ('Важное'),
       ('Срочно')