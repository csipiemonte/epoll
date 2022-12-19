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

<p>
${Votazione.testo}
</p>

<p>
Per procedere alla votazione &egrave; necessario rispondere alla presente email digitando i codici delle preferenze in quantit&agrave; minore o uguale al numero indicato.
</p>

<p>
Si prega di porre la massima attenzione nella digitazione delle preferenze, nel caso in cui le preferenze non venissero espresse correttamente i voti non validi non saranno calcolati.
</p>

<h3>Votazione multi-preferenza</h3>
<#if Votazione.multiPreferenza == true>
<p>SI</p>
<h3>Numero massimo di preferenze da esprimere</h3>
<p>${Votazione.preferenzeDaEsprimere}</p>
<#else>
<p>NO</p>
</#if>

<#if Votazione.preferenze??>
<h3>Preferenze:</h3>
<#list Votazione.preferenze as preferenza>
<p>${preferenza.codPreferenza}  ${preferenza.preferenza}</p>
</#list>
<br>
</#if>

<p>
Le preferenze devono essere espresse all'interno del periodo di votazione che inizia il giorno 
${Votazione.dtInizioValidita?datetime?string('dd MMMMM yyyy')} 
alle ore ${Votazione.dtInizioValidita?datetime?string('HH:mm')}
 e si conclude il giorno 
${Votazione.dtFineValidita?datetime?string('dd MMMMM yyyy')}
 alle ore ${Votazione.dtFineValidita?datetime?string('HH:mm')}.
</p>

Grazie.<br><br>

Il presidente.
 
</body>