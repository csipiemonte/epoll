/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.epoll.epollsrv.util.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import it.csi.epoll.epollsrv.util.logger.EpollsrvLogger;
import it.csi.epoll.epollsrv.util.logger.LogCategory;
import it.csi.epoll.epollsrv.util.logger.LoggerConstants;
import it.csi.util.performance.StopWatch;


/**
 * @author abora
 * 
 *         Aspect che implementa le funzionalità di StopWatch e i log di begin ed end su tutti i metodi specificati
 */
@Component
@Aspect
public class LoggingAspect {

    private static final EpollsrvLogger logScheduler = new EpollsrvLogger ( LogCategory.EPOLLSRV_ROOT_LOG_CATEGORY_SCHEDULER_LOG_CATEGORY.getCategory () );

    private static final EpollsrvLogger logBusiness = new EpollsrvLogger ( LogCategory.EPOLLSRV_ROOT_LOG_CATEGORY_BUSINESS_LOG_CATEGORY.getCategory () );

    private static final StopWatch watcher = new StopWatch ( LoggerConstants.EPOLLSRV_ROOT_LOG_CATEGORY.getValue () );

    /**
     * Aspetto su tutte le implementazioni dei service.
     * 
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around ( value = "execution(* it.csi.epoll.epollsrv.business.impl..*.*(..))", argNames = "joinPoint" )
    public Object aroundBusinessImplCall ( ProceedingJoinPoint joinPoint ) throws Throwable {
        try {
            // avvia il timer prima dell'operazione da monitorare
            watcher.start ();
            logBusiness.begin ( joinPoint.getTarget ().getClass ().getSimpleName (), joinPoint.getSignature ().getName () );
            return joinPoint.proceed ();
        } finally {
            logBusiness.end ( joinPoint.getTarget ().getClass ().getSimpleName (), joinPoint.getSignature ().getName () );
            // arresta il timer
            watcher.stop ();
            // logga il tempo trascorso
            watcher.dumpElapsed ( joinPoint.getTarget ().getClass ().getSimpleName (), joinPoint.getSignature ().getName (), "business", "" );
        }
    }

    /**
     * Aspetto su tutte le implementazioni dei scheduler.
     * 
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around ( value = "execution(* it.csi.epoll.epollsrv.schedule.impl..*.*(..))", argNames = "joinPoint" )
    public Object aroundScheduleImplCall ( ProceedingJoinPoint joinPoint ) throws Throwable {
        try {
            // avvia il timer prima dell'operazione da monitorare
            watcher.start ();
            logScheduler.begin ( joinPoint.getTarget ().getClass ().getSimpleName (), joinPoint.getSignature ().getName () );
            return joinPoint.proceed ();
        } finally {
            logScheduler.end ( joinPoint.getTarget ().getClass ().getSimpleName (), joinPoint.getSignature ().getName () );
            // arresta il timer
            watcher.stop ();
            // logga il tempo trascorso
            watcher.dumpElapsed ( joinPoint.getTarget ().getClass ().getSimpleName (), joinPoint.getSignature ().getName (), "scheduler", "" );
        }
    }
}
