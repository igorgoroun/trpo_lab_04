drop table if exists discipline;
create table discipline
(
    id   int          not null AUTO_INCREMENT,
    name varchar(128) not null,
    primary key (id)
) engine=INNODB;

insert into discipline (name)
values ('Технології розподілених систем та паралельних обчислень'),
       ('Web-технології та Web-дизайн'),
       ('Крос-платформне програмування'),
       ('Інтелектуальний аналіз даних'),
       ('Основи психології'),
       ('Охорона праці'),
       ('Математичні методи дослідження операцій'),
       ('Бази даних');

create table timing
(
    id         int         not null AUTO_INCREMENT,
    name       varchar(32) not null,
    time_start TIME        not null,
    time_end   TIME        not null,
    primary key (id)
) engine=INNODB;
insert into timing (name, time_start, time_end)
values ('I пара', '09:00', '10:20'),
       ('II пара', '10:30', '11:50'),
       ('III пара', '12:20', '13:40'),
       ('IV пара', '13:50', '15:10'),
       ('V пара', '15:20', '16:40'),
       ('VI пара', '16:50', '18:10'),
       ('VII пара', '18:20', '19:40');


create table room
(
    id   int         not null AUTO_INCREMENT,
    name varchar(16) not null,
    primary key (id)
) engine=INNODB;
insert into room (name)
values ('D1'),
       ('D2'),
       ('D3'),
       ('D4'),
       ('1-134'),
       ('2-14');


create table lecturer
(
    id   int          not null AUTO_INCREMENT,
    name varchar(128) not null,
    primary key (id)
) engine=INNODB;
insert into lecturer (name)
values ('Павленко О.Ю.'),
       ('Гайда А.Ю.'),
       ('Беркунський Є.Ю.'),
       ('Гайдаєнко О.В.'),
       ('Філатова О.С.'),
       ('Гурець Н.В.'),
       ('Приходько К.С.'),
       ('Партас В.К.'),
       ('Книрік Н.Р.'),
       ('Латанська Л.О.'),
       ('Бажаткіна В.А.');

create table lecturer_discipline
(
    lecturer_id   int not null,
    discipline_id int not null,
    index         idx_lecturer(lecturer_id),
    index         idx_discipline(discipline_id),
    CONSTRAINT UNIQUE (lecturer_id, discipline_id),
    FOREIGN KEY (lecturer_id) REFERENCES lecturer (id) on delete cascade,
    FOREIGN KEY (discipline_id) REFERENCES discipline (id) on delete cascade
) engine=INNODB;
insert into lecturer_discipline (lecturer_id, discipline_id)
values (1, 1),
       (1, 2),
       (2, 3),
       (3, 1),
       (3, 2),
       (4, 4),
       (5, 5),
       (6, 6),
       (10, 7),
       (11, 7);

drop FUNCTION if EXISTS is_lecturer_discipline;
create FUNCTION is_lecturer_discipline(Discipline int, Lecturer int)
    returns BIT
    DETERMINISTIC
BEGIN
return exists (select *
               from lecturer_discipline as ld
               where ld.lecturer_id = Lecturer
                 and ld.discipline_id = Discipline);
END;
select is_lecturer_discipline(4, 4);

drop function if exists convert_weekday;
create function convert_weekday(wd int)
    returns VARCHAR(16)
    DETERMINISTIC
begin
return case wd
           when 1 then 'Понеділок'
           when 2 then 'Вівторок'
           when 3 then 'Середа'
           when 4 then 'Четвер'
           when 5 then 'П\'ятниця'
    end;
end;
select convert_weekday(5);

drop table if exists agenda;
create table agenda (
    id int not null auto_increment,
    discipline_id int not null,
    lecturer_id int,
    weekday SMALLINT(1) not null check (weekday >= 1 and weekday <= 7),
    timing_id int not null,
    room_id int not null,
    primary key(id),
    index idx_discipline(discipline_id),
    index idx_weekday(weekday),
    index idx_weekday_timing(weekday, timing_id),
    FOREIGN KEY (discipline_id) REFERENCES discipline(id) on delete cascade,
    FOREIGN KEY (lecturer_id) REFERENCES lecturer(id) on delete set null,
    FOREIGN KEY (timing_id) REFERENCES timing(id) on delete cascade,
    FOREIGN KEY (room_id) REFERENCES room(id) on delete cascade
) engine=INNODB;

insert into agenda (discipline_id, lecturer_id, weekday, timing_id, room_id) values 
    (2, 3, 1, 1, 5),
    (2, 1, 1, 2, 5),
    (3, 2, 1, 3, 2),
    (6, 6, 1, 4, 6),
    (6, 6, 1, 5, 1),
    (2, 3, 2, 2, 3),
    (1, 1, 2, 3, 2),
    (8, 9, 2, 4, 2),
    (8, 9, 2, 5, 2),
    (5, 5, 3, 2, 2),
    (5, 5, 3, 3, 2),
    (8, 9, 3, 4, 5),
    (7, 10, 4, 1, 1),
    (7, 10, 4, 2, 1),
    (7, 11, 4, 2, 1),
    (1, 1, 5, 3, 5),
    (3, 2, 5, 4, 4),
    (4, 4, 5, 5, 1);

-- full weekly agenda
select 
    convert_weekday(ag.weekday) as weekday,
    tim.name as lesson_position,
    CONCAT(date_format(tim.time_start, '%H:%i'), '-', date_format(tim.time_end, '%H:%i')) as lesson_time,
    di.name as discipline,
    lec.name as lecturer,
    rm.name as room
from agenda ag
left join timing tim on tim.id=ag.timing_id
left join discipline di on di.id=ag.discipline_id
left join lecturer lec on lec.id=ag.lecturer_id
left join room rm on rm.id=ag.room_id
order by ag.weekday asc, tim.time_start asc;

-- find lecturers working on defined day and room
select 
    convert_weekday(ag.weekday) as weekday,
    lec.name as lecturer,
    rm.name as room
from agenda ag
left join lecturer lec on lec.id=ag.lecturer_id
left join room rm on rm.id=ag.room_id
where ag.weekday=2 and rm.name='D2';

-- find lecturers not working on defined day
select lec.name as lecturer
from lecturer lec
left join agenda ag on ag.lecturer_id=lec.id and ag.weekday=2
where ag.weekday is null;

-- find days having defined number of lessons
select 
    convert_weekday(ag.weekday) as dayname
from agenda ag
group by ag.weekday
having count(*)=3;

-- find days having defined number of rooms occupied
select 
    convert_weekday(ag.weekday) as weekday,
    room.name,
    count(*) as room_used
from agenda ag
left join room on room.id=ag.room_id
group by ag.weekday, ag.room_id
having count(*)=2;


-- move first lessons to the last position
with positions as (
    select weekday, min(timing_id) as first_position, max(timing_id)+1 as last_position from agenda group by weekday
), 
movements as (
    select ag.id, p.last_position as move_to from positions p
    LEFT join agenda ag on ag.weekday=p.weekday and ag.timing_id=p.first_position
)
update agenda, (select * from movements) as mov
set agenda.timing_id=mov.move_to
where agenda.id=mov.id and agenda.weekday=1;

