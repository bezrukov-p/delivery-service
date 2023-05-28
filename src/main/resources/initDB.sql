CREATE TABLE IF NOT EXISTS public.couriers
(
    id bigserial not null,
    type text COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT couriers_pkey PRIMARY KEY (id)
)

    TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.couriers
    OWNER to postgres;

----------------------------------------------------

CREATE TABLE IF NOT EXISTS public.regions
(
    type bigint NOT NULL,
    CONSTRAINT regions_pkey PRIMARY KEY (type)
)

    TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.regions
    OWNER to postgres;

--------------------------------------------------



CREATE TABLE IF NOT EXISTS public.orders
(
    id bigserial not null,
    weight double precision NOT NULL,
    region integer NOT NULL,
    cost integer NOT NULL,
    completed_time timestamp without time zone,
    courier_completed_id bigint,
    CONSTRAINT orders_pkey PRIMARY KEY (id),
    CONSTRAINT courier_completed_id_fk FOREIGN KEY (courier_completed_id)
        REFERENCES public.couriers (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID,
    CONSTRAINT region_fk FOREIGN KEY (region)
        REFERENCES public.regions (type) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID
)

    TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.orders
    OWNER to postgres;


--------------------------------------
CREATE TABLE IF NOT EXISTS public.hours
(
    hours text COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT hours_pkey PRIMARY KEY (hours)
)

    TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.hours
    OWNER to postgres;

---------------------------------------


CREATE TABLE IF NOT EXISTS public.couriers_regions
(
    courier_id bigint NOT NULL,
    region_id bigint NOT NULL,
    CONSTRAINT couriers_fk FOREIGN KEY (courier_id)
        REFERENCES public.couriers (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID,
    CONSTRAINT regions_fk FOREIGN KEY (region_id)
        REFERENCES public.regions (type) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID
)

    TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.couriers_regions
    OWNER to postgres;


---------------------------------------------------------



CREATE TABLE IF NOT EXISTS public.orders_hours
(
    order_id bigint NOT NULL,
    hours text COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT hours_fk FOREIGN KEY (hours)
        REFERENCES public.hours (hours) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT orders_fk FOREIGN KEY (order_id)
        REFERENCES public.orders (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

    TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.orders_hours
    OWNER to postgres;

-------------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS public.couriers_hours
(
    courier_id bigint,
    hours text COLLATE pg_catalog."default",
    CONSTRAINT couriers_fk FOREIGN KEY (courier_id)
        REFERENCES public.couriers (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID,
    CONSTRAINT hours_fk FOREIGN KEY (hours)
        REFERENCES public.hours (hours) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID
)

    TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.couriers_hours
    OWNER to postgres;