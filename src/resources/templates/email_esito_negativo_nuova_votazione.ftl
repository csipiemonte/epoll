<!--
SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte

SPDX-License-Identifier: EUPL-1.2
-->
<#ftl encoding="utf-8">

<html>

<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
</head>

<body>

<h3>Attenzione!</h3>

<p>Non &egrave; stato possibile creare la votazione richiesta per la seguente motivazione:${Motivazione}</p>

<#if Votazione??>
<#if Votazione.oggetto??>
<h3>Oggetto della votazione:</h3>
<p>${Votazione.oggetto}</p>
<br>
</#if>
</#if>
</body>
</html>

