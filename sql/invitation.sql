select * from invitation where 12 = ANY (guests);
select * from invitation;
insert into invitation (id, rsvped, guests) values(3, false, ARRAY [3]);

CREATE TABLE public.invitation
(
    id bigint NOT NULL,
    rsvped boolean NOT NULL,
    guests bigint[] NOT NULL,
    CONSTRAINT invitation_pkey PRIMARY KEY (id)
)
