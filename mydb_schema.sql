--
-- PostgreSQL database dump
--

-- Dumped from database version 16.4 (Debian 16.4-1.pgdg120+1)
-- Dumped by pg_dump version 16.4 (Debian 16.4-1.pgdg120+1)

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
-- Name: pgcrypto; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS pgcrypto WITH SCHEMA public;


--
-- Name: EXTENSION pgcrypto; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION pgcrypto IS 'cryptographic functions';


SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: message_status; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.message_status (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    message_id uuid NOT NULL,
    user_id uuid NOT NULL,
    seen boolean DEFAULT false,
    seen_at timestamp without time zone
);


ALTER TABLE public.message_status OWNER TO postgres;

--
-- Name: messages; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.messages (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    room_id uuid NOT NULL,
    sender_id uuid NOT NULL,
    content text NOT NULL,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE public.messages OWNER TO postgres;

--
-- Name: private_rooms; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.private_rooms (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE public.private_rooms OWNER TO postgres;

--
-- Name: private_users; Type: TABLE; Schema: public; Owner: ugurabbasov
--

CREATE TABLE public.private_users (
    user_id uuid NOT NULL,
    private_room_id uuid NOT NULL
);


ALTER TABLE public.private_users OWNER TO ugurabbasov;

--
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    username character varying NOT NULL,
    email character varying NOT NULL,
    password character varying,
    avatar character varying,
    refreshtoken character varying,
    createdat timestamp without time zone DEFAULT now(),
    nickname character varying NOT NULL
);


ALTER TABLE public.users OWNER TO postgres;

--
-- Name: message_status message_status_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.message_status
    ADD CONSTRAINT message_status_pkey PRIMARY KEY (id);


--
-- Name: messages messages_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.messages
    ADD CONSTRAINT messages_pkey PRIMARY KEY (id);


--
-- Name: private_users pk_private_users; Type: CONSTRAINT; Schema: public; Owner: ugurabbasov
--

ALTER TABLE ONLY public.private_users
    ADD CONSTRAINT pk_private_users PRIMARY KEY (user_id, private_room_id);


--
-- Name: private_rooms private_rooms_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.private_rooms
    ADD CONSTRAINT private_rooms_pkey PRIMARY KEY (id);


--
-- Name: message_status unique_message_user; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.message_status
    ADD CONSTRAINT unique_message_user UNIQUE (message_id, user_id);


--
-- Name: users users_email_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_email_key UNIQUE (email);


--
-- Name: users users_nickname_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_nickname_key UNIQUE (nickname);


--
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- Name: idx_message_status; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_message_status ON public.message_status USING btree (message_id, user_id);


--
-- Name: idx_messages_room; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_messages_room ON public.messages USING btree (room_id);


--
-- Name: idx_private_room_id; Type: INDEX; Schema: public; Owner: ugurabbasov
--

CREATE INDEX idx_private_room_id ON public.private_users USING btree (private_room_id);


--
-- Name: message_status fk_message; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.message_status
    ADD CONSTRAINT fk_message FOREIGN KEY (message_id) REFERENCES public.messages(id);


--
-- Name: private_users fk_private_room; Type: FK CONSTRAINT; Schema: public; Owner: ugurabbasov
--

ALTER TABLE ONLY public.private_users
    ADD CONSTRAINT fk_private_room FOREIGN KEY (private_room_id) REFERENCES public.private_rooms(id);


--
-- Name: messages fk_room; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.messages
    ADD CONSTRAINT fk_room FOREIGN KEY (room_id) REFERENCES public.private_rooms(id);


--
-- Name: messages fk_sender; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.messages
    ADD CONSTRAINT fk_sender FOREIGN KEY (sender_id) REFERENCES public.users(id);


--
-- Name: message_status fk_user; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.message_status
    ADD CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- Name: private_users fk_user; Type: FK CONSTRAINT; Schema: public; Owner: ugurabbasov
--

ALTER TABLE ONLY public.private_users
    ADD CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- PostgreSQL database dump complete
--

