insert into users(password, email, creation_time, is_active)
values ( '123', 'z@z.z', '2019-01-01 00:00:00', true),
       ( '123', 'a@a.a', '2019-01-01 00:00:00', true),
       ( '123', 'b@b.b', '2019-01-01 00:00:00', true),
       ( '123', 'c@c.c', '2019-01-01 00:00:00', true)
;
insert into members(creation_time, is_active, nick, user_id)
values ('2019-01-01 00:00:00', true, 'a', 1),
       ('2019-01-01 00:00:00', true, 'b', 2),
       ('2019-01-01 00:00:00', true, 'c', 3)
;
insert into tgroup(creation_time, description, group_name, is_active, members_limit)
values ('2019-01-01 00:00:00', 'java junior developers', 'devmeet', true, 4),
       ('2019-01-01 00:00:00', 'python junior developers', 'snakes', true, 3)
;
insert into tgroup_members(groups_id, members_id)
values (1, 1),
       (1, 2),
       (2, 1),
       (2, 3)
;

INSERT INTO places(creation_time, description, is_active, location, place_name, website, member_id)
VALUES ('2019-01-01 00:00:00', 'openspace koło Metra Politechniki', true, 'Rektorska 4, 00-614 Warszawa',
        'Centrum Zarządzania Innowacjami i Transferem Technologii Politechniki Warszawskiej', 'cziitt.pw.edu.pl', 1),
       ('2019-01-01 00:00:00', 'Google Campus Warsaw', true, 'Plac Konesera 03-736 Warszawa (+48) 22 128 44 38',
        'Google for Startups – Koneser', 'https://www.campus.co/warsaw', 2);
