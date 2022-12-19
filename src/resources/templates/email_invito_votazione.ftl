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
Per procedere alla votazione &egrave; necessario rispondere alla presente email digitando esclusivamente una delle preferenze sottostanti.
</p>

<p>
Si prega di porre la massima attenzione nella digitazione della preferenza, nel caso in cui la preferenza non venisse espressa correttamente il voto verr&agrave; annullato.
</p>

<#if Votazione.preferenze??>
<h3>Preferenze:</h3>
<#list Votazione.preferenze as preferenza>
<p>${preferenza.codPreferenza}  ${preferenza.preferenza}</p>
</#list>
<br>
</#if>

<p>
La preferenza deve essere espressa all'interno del periodo di votazione che inizia il giorno 
${Votazione.dtInizioValidita?datetime?string('dd MMMMM yyyy')} 
alle ore ${Votazione.dtInizioValidita?datetime?string('HH:mm')}
 e si conclude il giorno 
${Votazione.dtFineValidita?datetime?string('dd MMMMM yyyy')}
 alle ore ${Votazione.dtFineValidita?datetime?string('HH:mm')}.
</p>

Grazie.<br><br>

Il presidente.
 
</body>
