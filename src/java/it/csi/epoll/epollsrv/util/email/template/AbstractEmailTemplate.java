/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.epoll.epollsrv.util.email.template;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Map;

import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StringUtils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import it.csi.epoll.epollsrv.util.Constants;
import it.csi.epoll.epollsrv.util.exception.ManagedException;
import it.csi.epoll.epollsrv.util.logger.EpollsrvLogger;
import it.csi.epoll.epollsrv.util.logger.LogCategory;


/**
 *
 */
public abstract class AbstractEmailTemplate {

    private static final EpollsrvLogger logger = new EpollsrvLogger ( LogCategory.EPOLLSRV_ROOT_LOG_CATEGORY_UTIL_LOG_CATEGORY,
        "AbstractEmailTemplate" );

    private Configuration configuration;

    protected String templateName;

    public AbstractEmailTemplate (String templateName) {
        
        if ( !StringUtils.hasText ( templateName ) ) {
            logger.error ( "EmailTemplate Constructor", "Il nome del template non e' configurato" );
            throw new ManagedException ( "Il nome del template non e' configurato" );
        }

        this.templateName = templateName;
        try {
            configuration = new Configuration ( Configuration.VERSION_2_3_28 );
            configuration.setDirectoryForTemplateLoading ( new ClassPathResource ( Constants.TEMPLATE_PATH ).getFile () );
            configuration.setTemplateExceptionHandler ( TemplateExceptionHandler.RETHROW_HANDLER );
            configuration.setLogTemplateExceptions ( false );
            configuration.setWrapUncheckedExceptions ( true );
            configuration.setLocale ( Locale.getDefault () );
            configuration.setDefaultEncoding ( Constants.UTF_8 );
        } catch ( IOException e ) {
            logger.error ( "EmailTemplate Constructor", "Errore nella creazione del contenuto dell'email", e );
            throw new ManagedException ( e.getMessage () );
        }
    }

    public abstract Map<String, Object> creaModello ();

    public String creaContenutoEmail () {
        String methodName = "creaContenutoEmail";

        logger.begin ( "AbstractEmailTemplate", methodName );

        Map<String, Object> model = creaModello ();
        if ( model == null ) {
            throw new ManagedException ( "Modello non creato" );
        }

        try ( ByteArrayOutputStream baos = new ByteArrayOutputStream ();
            Writer res = new BufferedWriter ( new OutputStreamWriter ( baos, StandardCharsets.UTF_8 ) ) ) {

            Template template = configuration.getTemplate ( templateName );
            template.process ( model, res );

            return baos.toString ( Constants.UTF_8 );

        } catch ( IOException | TemplateException e ) {
            logger.error ( methodName, "Errore in fase di creazione del template", e );
            throw new ManagedException ( e.getMessage () );
        } finally {
            logger.end ( "AbstractEmailTemplate", methodName );
        }
    }

    public String getTemplateName () {
        return templateName;
    }

}
