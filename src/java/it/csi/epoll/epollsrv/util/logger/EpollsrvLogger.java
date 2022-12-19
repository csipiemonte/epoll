/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.epoll.epollsrv.util.logger;

import org.apache.log4j.Logger;


/**
 * Classe di centralizzazione dei log.
 */
public class EpollsrvLogger {

    private Logger log;
	private String className;

    /**
     * Costrutture da usare solo negli aspect.
     * 
     * @param logCategory
     */
    public EpollsrvLogger ( String logCategory ) {
        this.log = Logger.getLogger ( logCategory );
    }

	public EpollsrvLogger(LogCategory logCategory, String className) {
        this.log = Logger.getLogger ( logCategory.getCategory () );
		this.className = className;
	}

    public void begin ( String className, String method ) {
        debug ( className, method, LoggerConstants.BEGIN.getValue () );
	}

    public void end ( String className, String method ) {
        debug ( className, method, LoggerConstants.END.getValue () );
	}

    public void info ( String method, String message ) {
        log.info ( String.format ( LoggerConstants.DEFAULT_LOG.getValue (), className, method, message ) );
    }

    public void debug ( String className, String method, String message ) {
        if ( log.isDebugEnabled () ) {
            log.debug ( String.format ( LoggerConstants.DEFAULT_LOG.getValue (), className, method, message ) );
        }
    }
	
    public void warn ( String method, String message ) {
        log.warn ( String.format ( LoggerConstants.DEFAULT_LOG.getValue (), className, method, message ) );
	}

    public void warn ( String method, String message, Exception ex ) {
        log.warn ( String.format ( LoggerConstants.DEFAULT_LOG.getValue (), className, method, message ), ex );
    }

	public void error(String method, String message) {
        log.error ( String.format ( LoggerConstants.DEFAULT_LOG.getValue (), className, method, message ) );
	}

	public void error(String method, String message, Exception ex) {
        log.error ( String.format ( LoggerConstants.DEFAULT_LOG.getValue (), className, method, message ), ex );
	}

    public void error ( String className, String method, String message, Exception ex ) {
        log.error ( String.format ( LoggerConstants.DEFAULT_LOG.getValue (), className, method, message ), ex );
    }

}
