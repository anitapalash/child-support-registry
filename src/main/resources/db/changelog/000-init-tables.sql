create table if not exists debtors (
    id                      bigserial primary key,
    surname                 text,
    name                    text,
    patronymic              text,
    date_of_birth           timestamp,
    passport                text,
    inn                     text,
    registration_address    text,
    actual_address          text,
    deleted                 boolean not null default false
);

create table if not exists guardians (
    id                      bigserial primary key,
    surname                 text,
    name                    text,
    patronymic              text,
    date_of_birth           timestamp,
    passport                text,
    inn                     text,
    registration_address    text,
    actual_address          text,
    deleted                 boolean not null default false
);

create table if not exists children (
    id                      bigserial primary key,
    surname                 text,
    name                    text,
    patronymic              text,
    date_of_birth           timestamp,
    debt                    bigint,
    debtor_id               bigint references debtors(id),
    guardian_id             bigint references guardians(id),
    deleted                 boolean not null default false
);


