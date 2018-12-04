drop table t_demo ;
create table t_demo (
  id          INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
  name        VARCHAR(128),
  version     INT,
  create_time TIMESTAMP,
  update_time TIMESTAMP
);
