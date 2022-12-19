/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.test.epoll.epollsrv.business.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class RegExTest {

    private Pattern regExpPattern;
    
    @Before
    public void init() {
        regExpPattern = Pattern.compile("\\s*([0-9a-zA-Z]+)[\\,\\-\\;\\s]*");
    }
    
    private List<String> estraiPreferenze (String line) {
        Matcher matcher = regExpPattern.matcher(line);
        
        List<String> preferenze = new ArrayList<> ();
        
        while (matcher.find ()) {
            
            preferenze.add ( matcher.group (1) );
        }        
        
        return preferenze;
    }
    
    @Test
    public void test() {
        
        List<String> prefs = estraiPreferenze ( "Blu;Giallo,Pippo-Viola Rosso Lilla" );
        
        Assert.assertEquals ( 6, prefs.size () );

        Assert.assertEquals ( "Blu", prefs.get ( 0 ) );
        Assert.assertEquals ( "Giallo", prefs.get ( 1 ) );
        Assert.assertEquals ( "Pippo", prefs.get ( 2 ) );
        Assert.assertEquals ( "Viola", prefs.get ( 3 ) );
        Assert.assertEquals ( "Rosso", prefs.get ( 4 ) );
        Assert.assertEquals ( "Lilla", prefs.get ( 5 ) );

    }
    
}
