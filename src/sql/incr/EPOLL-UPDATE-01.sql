-- SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
--
-- SPDX-License-Identifier: EUPL-1.2

ALTER TABLE epoll_t_poll ADD frequenza_aggiornamento int8 NULL DEFAULT 0;

ALTER TABLE epoll_t_poll ADD dt_ultima_notifica timestamp NULL;

ALTER TABLE epoll_t_body_preferenza RENAME COLUMN ID TO uuid;

ALTER TABLE epoll_t_body_preferenza  ALTER COLUMN uuid SET DATA type VARCHAR(100)  USING (uuid_generate_v4());




