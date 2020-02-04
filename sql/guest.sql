select * from guest;
-- new
CREATE TABLE public.guest
(
    id BIGINT GENERATED ALWAYS AS IDENTITY,
    invitation_id bigint,
    name text COLLATE pg_catalog."default" NOT NULL,
    attending boolean,
    CONSTRAINT guest_pkey PRIMARY KEY (id)
);