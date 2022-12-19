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

<h3>
Notifica di voto della votazione con codice: ${Votazione.codice}
</h3>

<p>
Si riporta la segeunte notifica
</p>

<h4>Numero Elettori:</h4>
<#if elettori??>
<p>${elettori?size} </p>
<#else>
0
</#if>

<h4>Votanti:</h4>
<#if votanti??>
<p>${votanti?size} </p>
<#else>
Nessuna voto espresso.
</#if>
</body>
