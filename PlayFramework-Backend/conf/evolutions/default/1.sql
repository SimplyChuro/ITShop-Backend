# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table categories (
  id                            bigserial not null,
  name                          varchar(255),
  parent_id                     bigint,
  constraint pk_categories primary key (id)
);

create table pictures (
  id                            bigserial not null,
  url                           varchar(255),
  product_id                    bigint,
  constraint pk_pictures primary key (id)
);

create table products (
  id                            bigserial not null,
  name                          varchar(255),
  brand                         varchar(255),
  price                         float,
  description                   varchar(2048),
  constraint pk_products primary key (id)
);

create table product_category (
  id                            bigserial not null,
  category_id                   bigint,
  product_id                    bigint,
  constraint pk_product_category primary key (id)
);

create table users (
  id                            bigserial not null,
  auth_token                    varchar(255),
  name                          varchar(256) not null,
  surname                       varchar(256) not null,
  email                         varchar(256) not null,
  password                      varchar(255),
  admin                         boolean,
  constraint uq_users_email unique (email),
  constraint pk_users primary key (id)
);

create index ix_pictures_product_id on pictures (product_id);
alter table pictures add constraint fk_pictures_product_id foreign key (product_id) references products (id) on delete restrict on update restrict;

create index ix_product_category_category_id on product_category (category_id);
alter table product_category add constraint fk_product_category_category_id foreign key (category_id) references categories (id) on delete restrict on update restrict;

create index ix_product_category_product_id on product_category (product_id);
alter table product_category add constraint fk_product_category_product_id foreign key (product_id) references products (id) on delete restrict on update restrict;


# --- !Downs

alter table if exists pictures drop constraint if exists fk_pictures_product_id;
drop index if exists ix_pictures_product_id;

alter table if exists product_category drop constraint if exists fk_product_category_category_id;
drop index if exists ix_product_category_category_id;

alter table if exists product_category drop constraint if exists fk_product_category_product_id;
drop index if exists ix_product_category_product_id;

drop table if exists categories cascade;

drop table if exists pictures cascade;

drop table if exists products cascade;

drop table if exists product_category cascade;

drop table if exists users cascade;

