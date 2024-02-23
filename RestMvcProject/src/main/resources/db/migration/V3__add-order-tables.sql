drop
  table if exists beer_order;
drop
  table if exists beer_order_line;
create table beer_order (
  version BIGINT default null,
  created_date datetime(6) default null,
  customer_ref varchar(255) default null,
  last_modified_date datetime(6) default null,
  id varchar(36) not null,
  customer_id varchar(36) default null,
  primary key (id),
  constraint foreign key (customer_id) references customer(id)
)engine = InnoDB;
create table beer_order_line (
  version BIGINT default null,
  beer_id varchar(36) default null,
  created_date datetime(6) default null,
  last_modified_date datetime(6) default null,
  id varchar(36) not null,
  order_quantity integer default null,
  quantity_allocated integer default null,
  beer_order_id varchar(36) default null,
  primary key (id),
  constraint foreign key (beer_order_id) references beer_order(id),
  constraint foreign key (beer_id) references beer(id)
)engine = InnoDB;