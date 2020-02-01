select * from guest;
insert into guest (invitation_id, name) values (1, 'test 3 name');

CREATE TABLE color (
    color_id INT GENERATED ALWAYS AS IDENTITY,
    color_name VARCHAR NOT NULL
);

INSERT INTO color (color_name)
VALUES
    ('Red');
	
select * from color;

-- old
CREATE TABLE public.guest
(
    id bigint NOT NULL,
    invitation_id bigint,
    name text COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT guest_pkey PRIMARY KEY (id)
)

DROP TABLE public.guest;

-- new
CREATE TABLE public.guest
(
    id BIGINT GENERATED ALWAYS AS IDENTITY,
    invitation_id bigint,
    name text COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT guest_pkey PRIMARY KEY (id)
);