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
Scrutinio della votazione con codice: ${Votazione.codice}
</h3>

<p>
Si riporta di seguito lo scrutinio della votazione.
</p>

<h4>Elettori:</h4>
<#if elettori??>
<#list elettori as elettore>
<p>${elettore.cognomeNome} </p>
<#else>
Nessun elettore presente.
</#list>
<#else>
Nessun elettore presente.
</#if>

<h4>Numero Elettori:</h4>
<#if elettori??>
<p>${elettori?size} </p>
<#else>
0
</#if>

<h4>Votanti:</h4>
<#if votanti??>
<#list votanti as votante>
<p>${votante.dtDataVotazione?datetime?string('dd MMMMM yyyy HH:mm')} ${votante.cognomeNome} </p>
<#else>
Nessuna voto espresso.
</#list>
<#else>
Nessuna voto espresso.
</#if>

<h4>Numero Votanti:</h4>
<#if votanti??>
<#assign numeroVotiAnticipati = 0>
<#assign numeroVotiDopoScandenza = 0>
<p>${votanti?size}</p>
<#list votanti as votante>
<#if votante.dtDataVotazione < Votazione.dtInizioValidita>
<#assign numeroVotiAnticipati = numeroVotiAnticipati + 1>
<p> Voti anticipati : ${numeroVotiAnticipati}</p>
</#if>
<#if Votazione.dtFineValidita < votante.dtDataVotazione>
<#assign numeroVotiDopoScandenza = numeroVotiDopoScandenza + 1> 
<p>Voti dopo la chiusura: ${numeroVotiDopoScandenza}</p>
</#if>	
</#list>
<#else>
Nessuna voto espresso.
</#if>


<h4>Preferenze valide:</h4>
<#if PreferenzeValide??>
<#list PreferenzeValide as preferenzaValida>
<#if preferenzaValida.preferenza?trim?has_content>
<p>Preferenza "${preferenzaValida.preferenza}", voti: ${preferenzaValida.conteggio} </p>
</#if>
<#else>
Nessuna preferenza valida espressa.
</#list>
<#else>
Nessuna preferenza valida espressa.
</#if>

<h4>Schede bianche:</h4>
<#if SchedeBianche??>
<#list SchedeBianche as schedaBianca>
<p>Voti: ${schedaBianca.conteggio} </p>
<#else>
<p>Voti: 0 </p>
</#list>
<#else>
<p>Voti: 0 </p>
</#if>

<h4>Preferenze non valide (non ammesse o espresse in ritardo rispetto ai termini):</h4>
<#if PreferenzeNonValide??>
<#list PreferenzeNonValide as preferenzaNonValida>
<#if preferenzaNonValida.preferenza=="">
<p>Preferenza scheda bianca, voti: ${preferenzaNonValida.conteggio} </p>
<#else>
<p>Preferenza "${preferenzaNonValida.preferenza}", voti: ${preferenzaNonValida.conteggio} </p>
</#if>
<#else>
Nessuna preferenza espressa.
</#list>
<#else>
Nessuna preferenza espressa.
</#if>


</body>
