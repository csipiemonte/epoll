/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */
package it.csi.test.epoll.epollsrv.testbed.config;

import java.sql.SQLException;
import java.util.Properties;

import javax.annotation.Resource;
import javax.persistence.EntityManagerFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import it.csi.epoll.epollsrv.business.dto.ConfigDTO;


@Configuration
@PropertySource ( "classpath:database.properties" )
@ComponentScan ( basePackages = {
    "it.csi.epoll.epollsrv.business.impl",
    "it.csi.epoll.epollsrv.schedule.impl",
    "it.csi.epoll.epollsrv.util.aspects",
    "it.csi.epoll.epollsrv.config" },
                excludeFilters = {
                    @Filter ( type = FilterType.ANNOTATION, value = Configuration.class ) } )
@EnableJpaRepositories ( basePackages = { "it.csi.epoll.epollsrv.integration.repository", "it.csi.epoll.epollsrv.test.repository" } )
@EnableTransactionManagement
@EnableAspectJAutoProxy
@EnableScheduling
@TestExecutionListeners ( { DirtiesContextTestExecutionListener.class } )
public class EPollUnitTestInMemory {

    @Resource
    private Environment env;

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory () throws SQLException {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean ();
        em.setDataSource ( dataSource () );
        em.setPackagesToScan ( "it.csi.epoll.epollsrv.integration.domain" );

        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter ();
        em.setJpaVendorAdapter ( vendorAdapter );
        em.setJpaProperties ( additionalProperties () );

        return em;
    }

    @Bean
    public EmbeddedDatabase dataSource () throws SQLException {
    	EmbeddedDatabase dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
    			.addScript("sql/init_schema.sql")
    			.addScript("sql/init_data.sql").build();

    			ResourceDatabasePopulator rdp = new ResourceDatabasePopulator();
    			rdp.addScript(new ClassPathResource("sql/init_functions.sql"));
    			rdp.setSeparator(";;");
    			rdp.populate(dataSource.getConnection());

    			return dataSource;
        
        
    }

    @Bean
    public PlatformTransactionManager transactionManager ( EntityManagerFactory emf ) {
        JpaTransactionManager transactionManager = new JpaTransactionManager ();
        transactionManager.setEntityManagerFactory ( emf );

        return transactionManager;
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation () {
        return new PersistenceExceptionTranslationPostProcessor ();
    }
    
    @Bean
    public ConfigDTO configDTO () {
            return ConfigDTO.builder ()
                .withConnectionTimeout ( "5000" )
                .withDebug ( "true" )
                .withEmailTimeout ( "5000" )
                .withMaxNumEmailCfgCached ( 100 )
                .build ();
    }
    
    Properties additionalProperties () {
        Properties properties = new Properties ();

        properties.setProperty ( "hibernate.dialect", "org.hibernate.dialect.H2Dialect" );
        properties.setProperty ( "hibernate.temp.use_jdbc_metadata_defaults", "false" );
        properties.setProperty ( "hibernate.jdbc.lob.non_contextual_creation", "true" );
        properties.setProperty ( "hibernate.default_schema", "EPOLL" );
        properties.setProperty ( "hibernate.show_sql", "true" );
        properties.setProperty ( "hibernate.format_sql", "true" );
        properties.setProperty ( "hibernate.hbm2ddl.auto", "none" );
        properties.setProperty ( "hibernate.generate_statistics", "true" );
        properties.setProperty ( "spring.jpa.properties.hibernate.generate_statistics", "true" );
        properties.setProperty ( "spring.h2.console.enabled", "true" );
        properties.setProperty ( "spring.h2.console.path", "/h2-console" );
        return properties;
    }

}
