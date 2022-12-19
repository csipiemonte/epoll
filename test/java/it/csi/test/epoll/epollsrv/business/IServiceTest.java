/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.test.epoll.epollsrv.business;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.Serializable;
import java.util.List;

import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import it.csi.epoll.epollsrv.business.service.IService;
import it.csi.epoll.epollsrv.integration.domain.AbstractEntity;
import it.csi.epoll.epollsrv.integration.repository.IRepository;


/**
 * @param <S> Service
 * @param <R> Repository
 * @param <E> Entity
 * @param <ID> Primary key
 *
 */
@Transactional
public abstract class IServiceTest<S extends IService<R, E, ID>, R extends IRepository<E, ID>, E extends AbstractEntity<ID>,
                ID extends Serializable> {

    /**
     * Metodo astratto per ottenere il service specifico sotto test da riferfinire nei vari testServiceImpl.
     * 
     * @return Service principale del testcase.
     */
    protected abstract S getService ();

    /**
     * Metodo astratto per ottenere la entity specifica da riferfinire nei vari testServiceImpl.
     * 
     * @return Service principale del testcase.
     */
    protected abstract E createEntity ();

    @Test
    public void testStatus () {
        Boolean result = getService ().isActive ();
        assertNotNull ( "Il servizio testStatus deve sempre ritornare un oggetto valorizzato", result );
        assertTrue ( "Il risultato del test deve essere sempre true", result );
    }

    @Test
    public void testFindAll () {
        List<E> result = getService ().findAll ();
        assertNotNull ( "Il servizio deve sempre ritornare un oggetto valorizzato", result );
    }

    @Test
    public void testSave () {
        E result = getService ().save ( createEntity () );
        assertNotNull ( "Il servizio testSave deve sempre ritornare un oggetto valorizzato", result );
        assertNotNull ( "Il risultato del test deve restituire un ID valorizzato ", result.getId () );
    }

    @Test
    public void testFindOne () {
        E saved = getService ().save ( createEntity () );
        E result = getService ().findOne ( saved.getId () );
        assertNotNull ( "Il servizio testSave deve sempre ritornare un oggetto valorizzato", result );
        assertNotNull ( "Il risultato del test deve restituire un ID valorizzato ", result.getId () );
    }

    @Test
    public void testDelete () {
        E saved = getService ().save ( createEntity () );
        getService ().delete ( saved.getId () );
        E result = getService ().findOne ( saved.getId () );
        assertNull ( "Il servizio testDelete deve sempre ritornare un oggetto NON valorizzato in quanto precedentemente cancellato", result );
    }

    @SuppressWarnings ( "unlikely-arg-type" )
    @Test
    public void testEquals () {
        E toCompare = createEntity ();
        E saved = getService ().save ( createEntity () );
        E result = getService ().findOne ( saved.getId () );
        assertTrue ( "Le due entità devono essere uguali", saved.equals ( result ) );
        assertFalse ( "L'equals deve ritornare sempre false se confronto un oggetto nullo", saved.equals ( null ) );
        assertFalse ( "L'equals deve ritornare sempre false se confrontato con un oggetto di diverso tipo", saved.equals ( this ) );
        if ( null == toCompare.getId () ) {
            assertTrue ( "Le due entità non devono essere uguali", !saved.equals ( toCompare ) && !toCompare.equals ( saved ) );
        } else {
            assertTrue ( "Le due entità devono essere uguali", saved.equals ( toCompare ) );
        }
    }

    @Test
    public void testHash () {
        E toCompare = createEntity ();
        E saved = getService ().save ( createEntity () );
        assertTrue ( "hashCode deve essere maggiore di 31", ( toCompare.hashCode () >= 31 ) && ( saved.hashCode () >= 31 ) );
    }

}
