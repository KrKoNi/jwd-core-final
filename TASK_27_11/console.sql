drop database if exists NASA;
create database NASA;
use NASA;

drop table if exists abstract_base_entity;
create table abstract_base_entity(
                                     id tinyint primary key auto_increment,
                                     name varchar(20) not null unique
);

drop table if exists rank_;
drop table if exists role_;
drop table if exists mission_result;

create table rank_ like abstract_base_entity;
create table role_ like abstract_base_entity;

insert into rank_ (id, name)
values (1, 'TRAINEE'), (2, 'SECOND_OFFICER'), (3, 'FIRST_OFFICER'), (4, 'CAPTAIN');

insert into role_ (id, name)
values (1, 'MISSION_SPECIALIST'), (2, 'FLIGHT_ENGINEER'), (3, 'PILOT'), (4, 'COMMANDER');

create table mission_result like abstract_base_entity;

insert into mission_result (id, name)
values (1, 'CANCELLED'), (2, 'FAILED'), (3, 'PLANNED'), (4, 'IN_PROGRESS'), (5, 'COMPLETED');

drop table if exists crewmember;
drop table if exists flight_mission;
drop table if exists spaceship;

create table crewmember like abstract_base_entity;
create table flight_mission like abstract_base_entity;
create table spaceship like abstract_base_entity;

alter table crewmember
    add column is_ready_for_next_missions boolean default true not null,
    add column role_id tinyint,
    add column rank_id tinyint,
    add column mission_id tinyint,

    add constraint foreign key (mission_id) references flight_mission(id),
    add constraint foreign key (rank_id) references rank_(id),
    add constraint foreign key (role_id) references role_(id);

drop table if exists team_pattern;

create table team_pattern(
                             id tinyint,
                             role_id tinyint,
                             role_num tinyint default 0,

                             spaceship_id tinyint,

                             foreign key (spaceship_id) references spaceship(id),
                             foreign key (role_id) references role_(id)
);

alter table spaceship
    add column is_ready_for_next_missions boolean default (true) not null,
    add column distance int,
    add team_id tinyint,
    add mission_id tinyint,

    add constraint foreign key (mission_id) references flight_mission(id);

alter table flight_mission
    add column start_date date null,
    add column end_date date null,
    add column distance mediumint unsigned null,

    add column mission_result_id tinyint,

    add constraint foreign key(mission_result_id) references mission_result(id);
