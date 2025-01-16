create table "user"
(
    id serial primary key
);

comment on table "user" is 'coding.challenge.Application user';

comment on column "user".id is 'User identifier';

create table phrase
(
    id serial primary key
);

comment on table phrase is 'Practice phrase';

comment on column phrase.id is 'Phrase identifier';

create table user_phrase
(
    user_id          int     not null,
    phrase_id        int     not null,
    phrase_file_path varchar not null,
    foreign key (user_id) references "user" (id),
    foreign key (phrase_id) references phrase (id)
);

comment on table user_phrase is 'Phrase saved by a user';

comment on column user_phrase.user_id is 'User identifier';
comment on column user_phrase.phrase_id is 'Phrase identifier';
comment on column user_phrase.phrase_file_path is 'Path to the audio file (phrase) that user saved';