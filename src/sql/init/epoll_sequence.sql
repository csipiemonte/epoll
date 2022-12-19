-- SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
--
-- SPDX-License-Identifier: EUPL-1.2

DROP SEQUENCE IF EXISTS epoll_t_elettore_id_seq;

CREATE SEQUENCE epoll_t_elettore_id_seq
       INCREMENT BY 1
       MINVALUE 1
       CACHE 1
       NO CYCLE
       OWNED BY epoll_t_elettore.id;
       
DROP SEQUENCE IF EXISTS epoll_t_ente_id_seq;

CREATE SEQUENCE epoll_t_ente_id_seq
       INCREMENT BY 1
       MINVALUE 1
       CACHE 1
       NO CYCLE
       OWNED BY epoll_t_ente.id;
       
DROP SEQUENCE IF EXISTS epoll_t_pec_cfg_id_seq;

CREATE SEQUENCE epoll_t_pec_cfg_id_seq
       INCREMENT BY 1
       MINVALUE 1
       CACHE 1
       NO CYCLE
       OWNED BY epoll_t_pec_cfg.id;
       
DROP SEQUENCE IF EXISTS epoll_t_poll_id_seq;

CREATE SEQUENCE epoll_t_poll_id_seq
       INCREMENT BY 1
       MINVALUE 1
       CACHE 1
       NO CYCLE
       OWNED BY epoll_t_poll.id;

DROP SEQUENCE IF EXISTS epoll_t_preferenza_id_seq;

CREATE SEQUENCE epoll_t_preferenza_id_seq
       INCREMENT BY 1
       MINVALUE 1
       CACHE 1
       NO CYCLE
       OWNED BY epoll_t_preferenza.id;       
       
DROP SEQUENCE IF EXISTS epoll_t_presidente_id_seq;

CREATE SEQUENCE epoll_t_presidente_id_seq
       INCREMENT BY 1
       MINVALUE 1
       CACHE 1
       NO CYCLE
       OWNED BY epoll_t_presidente.id;