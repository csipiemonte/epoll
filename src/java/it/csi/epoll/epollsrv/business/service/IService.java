/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.epoll.epollsrv.business.service;

import java.io.Serializable;
import java.util.List;

import it.csi.epoll.epollsrv.integration.domain.AbstractEntity;
import it.csi.epoll.epollsrv.integration.repository.IRepository;


/**
 * Interfaccia contenente i metodi CRUD per comodita'. Aggiungere solo metodi potenzialmente utili a tutti services
 * 
 * @param <R> repository
 * @param <E> entity
 * @param <ID> primary key type
 * 
 * @author Lorenzo Fantini
 *
 */
public interface IService<R extends IRepository<E, ID>, E extends AbstractEntity<ID>, ID extends Serializable> extends MonitoringService {

    /**
     * Metodo astratto da riferfinire nei vari serviceImpl. Serve a centralizzare le operazioni CRUD e il monitoraggio
     * 
     * @return Repository principale del service.
     */
    R getRepository ();

    /**
     * Saves a given entity. Use the returned instance for further operations as the save operation might have changed the entity instance completely.
     * 
     * @param entity
     * @return the saved entity
     */
    default <S extends E> S save ( S entity ) {
        return getRepository ().save ( entity );
    }

    /**
     * Retrives an entity by its id.
     * 
     * @param id must not be {@literal null}.
     * @return the entity with the given id or {@literal null} if none found
     * @throws IllegalArgumentException if {@code id} is {@literal null}
     */
    default E findOne ( ID id ) {
        return getRepository ().findOne ( id );
    }

    /**
     * Returns the number of entities available.
     * 
     * @return the number of entities
     */
    default long count () {
        return getRepository ().count ();
    }

    /**
     * Deletes the entity with the given id.
     * 
     * @param id must not be {@literal null}.
     * @throws IllegalArgumentException in case the given {@code id} is {@literal null}
     */
    default void delete ( ID id ) {
        getRepository ().delete ( id );
    }

    /**
     * Returns all instances of the type.
     * 
     * @return all entities
     */
    default List<E> findAll () {
        return getRepository ().findAll ();
    }

    /*
     * (non-Javadoc)
     * @see MonitoringService#isActive()
     */
    @Override
    default Boolean isActive () {
        count ();
        return Boolean.TRUE;
    }
}
