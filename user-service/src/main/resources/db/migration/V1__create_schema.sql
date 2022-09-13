--
-- PostgreSQL database dump
--

-- Dumped from database version 13.4
-- Dumped by pg_dump version 13.4

-- Started on 2022-09-13 16:59:40

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 6 (class 2615 OID 33956)
-- Name: main; Type: SCHEMA; Schema: -; Owner: walk_trainer
--

CREATE SCHEMA main;


ALTER SCHEMA main OWNER TO walk_trainer;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 201 (class 1259 OID 33957)
-- Name: flyway_schema_history; Type: TABLE; Schema: main; Owner: walk_trainer
--

CREATE TABLE main.flyway_schema_history (
    installed_rank integer NOT NULL,
    version character varying(50),
    description character varying(200) NOT NULL,
    type character varying(20) NOT NULL,
    script character varying(1000) NOT NULL,
    checksum integer,
    installed_by character varying(100) NOT NULL,
    installed_on timestamp without time zone DEFAULT now() NOT NULL,
    execution_time integer NOT NULL,
    success boolean NOT NULL
);


ALTER TABLE main.flyway_schema_history OWNER TO walk_trainer;

--
-- TOC entry 207 (class 1259 OID 34001)
-- Name: hibernate_sequence; Type: SEQUENCE; Schema: main; Owner: walk_trainer
--

CREATE SEQUENCE main.hibernate_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE main.hibernate_sequence OWNER TO walk_trainer;

--
-- TOC entry 202 (class 1259 OID 33967)
-- Name: medical_card; Type: TABLE; Schema: main; Owner: walk_trainer
--

CREATE TABLE main.medical_card (
    id bigint NOT NULL,
    text character varying(255),
    owner bigint NOT NULL
);


ALTER TABLE main.medical_card OWNER TO walk_trainer;

--
-- TOC entry 203 (class 1259 OID 33972)
-- Name: role; Type: TABLE; Schema: main; Owner: walk_trainer
--

CREATE TABLE main.role (
    id bigint NOT NULL,
    name character varying(255)
);


ALTER TABLE main.role OWNER TO walk_trainer;

--
-- TOC entry 204 (class 1259 OID 33977)
-- Name: token; Type: TABLE; Schema: main; Owner: walk_trainer
--

CREATE TABLE main.token (
    id bigint NOT NULL,
    expiration_time timestamp without time zone,
    value character varying(255),
    owner bigint
);


ALTER TABLE main.token OWNER TO walk_trainer;

--
-- TOC entry 205 (class 1259 OID 33982)
-- Name: user; Type: TABLE; Schema: main; Owner: walk_trainer
--

CREATE TABLE main."user" (
    id bigint NOT NULL,
    email character varying(255) NOT NULL,
    first_name character varying(255),
    last_name character varying(255),
    password character varying(255),
    status character varying(255),
    username character varying(255) NOT NULL,
    doctor bigint NOT NULL
);


ALTER TABLE main."user" OWNER TO walk_trainer;

--
-- TOC entry 206 (class 1259 OID 33990)
-- Name: user_role; Type: TABLE; Schema: main; Owner: walk_trainer
--

CREATE TABLE main.user_role (
    user_id bigint NOT NULL,
    role_id bigint NOT NULL
);


ALTER TABLE main.user_role OWNER TO walk_trainer;

--
-- TOC entry 2875 (class 2606 OID 33965)
-- Name: flyway_schema_history flyway_schema_history_pk; Type: CONSTRAINT; Schema: main; Owner: walk_trainer
--

ALTER TABLE ONLY main.flyway_schema_history
    ADD CONSTRAINT flyway_schema_history_pk PRIMARY KEY (installed_rank);


--
-- TOC entry 2878 (class 2606 OID 33971)
-- Name: medical_card medical_card_pkey; Type: CONSTRAINT; Schema: main; Owner: walk_trainer
--

ALTER TABLE ONLY main.medical_card
    ADD CONSTRAINT medical_card_pkey PRIMARY KEY (id);


--
-- TOC entry 2880 (class 2606 OID 33976)
-- Name: role role_pkey; Type: CONSTRAINT; Schema: main; Owner: walk_trainer
--

ALTER TABLE ONLY main.role
    ADD CONSTRAINT role_pkey PRIMARY KEY (id);


--
-- TOC entry 2884 (class 2606 OID 33981)
-- Name: token token_pkey; Type: CONSTRAINT; Schema: main; Owner: walk_trainer
--

ALTER TABLE ONLY main.token
    ADD CONSTRAINT token_pkey PRIMARY KEY (id);


--
-- TOC entry 2886 (class 2606 OID 33996)
-- Name: token uk_1ijv0rgb0nkfb1suvfxcewa0y; Type: CONSTRAINT; Schema: main; Owner: walk_trainer
--

ALTER TABLE ONLY main.token
    ADD CONSTRAINT uk_1ijv0rgb0nkfb1suvfxcewa0y UNIQUE (value);


--
-- TOC entry 2882 (class 2606 OID 33994)
-- Name: role uk_8sewwnpamngi6b1dwaa88askk; Type: CONSTRAINT; Schema: main; Owner: walk_trainer
--

ALTER TABLE ONLY main.role
    ADD CONSTRAINT uk_8sewwnpamngi6b1dwaa88askk UNIQUE (name);


--
-- TOC entry 2888 (class 2606 OID 33998)
-- Name: user uk_ob8kqyqqgmefl0aco34akdtpe; Type: CONSTRAINT; Schema: main; Owner: walk_trainer
--

ALTER TABLE ONLY main."user"
    ADD CONSTRAINT uk_ob8kqyqqgmefl0aco34akdtpe UNIQUE (email);


--
-- TOC entry 2890 (class 2606 OID 34000)
-- Name: user uk_sb8bbouer5wak8vyiiy4pf2bx; Type: CONSTRAINT; Schema: main; Owner: walk_trainer
--

ALTER TABLE ONLY main."user"
    ADD CONSTRAINT uk_sb8bbouer5wak8vyiiy4pf2bx UNIQUE (username);


--
-- TOC entry 2892 (class 2606 OID 33989)
-- Name: user user_pkey; Type: CONSTRAINT; Schema: main; Owner: walk_trainer
--

ALTER TABLE ONLY main."user"
    ADD CONSTRAINT user_pkey PRIMARY KEY (id);


--
-- TOC entry 2876 (class 1259 OID 33966)
-- Name: flyway_schema_history_s_idx; Type: INDEX; Schema: main; Owner: walk_trainer
--

CREATE INDEX flyway_schema_history_s_idx ON main.flyway_schema_history USING btree (success);


--
-- TOC entry 2893 (class 2606 OID 34003)
-- Name: medical_card fk5o1c4snu8esgbvj07xrflbsg4; Type: FK CONSTRAINT; Schema: main; Owner: walk_trainer
--

ALTER TABLE ONLY main.medical_card
    ADD CONSTRAINT fk5o1c4snu8esgbvj07xrflbsg4 FOREIGN KEY (owner) REFERENCES main."user"(id);


--
-- TOC entry 2896 (class 2606 OID 34018)
-- Name: user_role fka68196081fvovjhkek5m97n3y; Type: FK CONSTRAINT; Schema: main; Owner: walk_trainer
--

ALTER TABLE ONLY main.user_role
    ADD CONSTRAINT fka68196081fvovjhkek5m97n3y FOREIGN KEY (role_id) REFERENCES main.role(id);


--
-- TOC entry 2895 (class 2606 OID 34013)
-- Name: user fkc2a7wc4p0vuajyrx1n925hsc8; Type: FK CONSTRAINT; Schema: main; Owner: walk_trainer
--

ALTER TABLE ONLY main."user"
    ADD CONSTRAINT fkc2a7wc4p0vuajyrx1n925hsc8 FOREIGN KEY (doctor) REFERENCES main."user"(id);


--
-- TOC entry 2897 (class 2606 OID 34023)
-- Name: user_role fkfgsgxvihks805qcq8sq26ab7c; Type: FK CONSTRAINT; Schema: main; Owner: walk_trainer
--

ALTER TABLE ONLY main.user_role
    ADD CONSTRAINT fkfgsgxvihks805qcq8sq26ab7c FOREIGN KEY (user_id) REFERENCES main."user"(id);


--
-- TOC entry 2894 (class 2606 OID 34008)
-- Name: token fkiqyl6yyijy2pd0y9h4beyfpju; Type: FK CONSTRAINT; Schema: main; Owner: walk_trainer
--

ALTER TABLE ONLY main.token
    ADD CONSTRAINT fkiqyl6yyijy2pd0y9h4beyfpju FOREIGN KEY (owner) REFERENCES main."user"(id);


-- Completed on 2022-09-13 16:59:40

--
-- PostgreSQL database dump complete
--

