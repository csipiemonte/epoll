-- SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
--
-- SPDX-License-Identifier: EUPL-1.2

CREATE ALIAS PGP_SYM_DECRYPT AS $$
String pgpSymDecrypt(String value, String chiave) {
return value;
}
$$;;