create table item (
    id serial primary key,
    description text not null,
    created timestamp,
    done boolean
);