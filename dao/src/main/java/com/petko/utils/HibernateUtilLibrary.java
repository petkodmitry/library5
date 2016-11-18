package com.petko.utils;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class HibernateUtilLibrary {
    private static HibernateUtilLibrary util = null;
    private static Logger log = Logger.getLogger(HibernateUtilLibrary.class);
    private static SessionFactory sessionFactory = null;
    private static ThreadLocal<Session> sessions = new ThreadLocal<>();

    public static synchronized HibernateUtilLibrary getHibernateUtil() {
        if (util == null){
            util = new HibernateUtilLibrary();
        }
        return util;
    }

    private HibernateUtilLibrary() {
        try {
            Configuration configuration = new Configuration().configure();
            StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties());
            ServiceRegistry registry = builder.build();
            sessionFactory = configuration.buildSessionFactory(registry);
        } catch (Throwable ex) {
            log.error("Initial SessionFactory creation failed. " + ex);
            System.exit(0);
        }
    }

    public Session getSession() {
        Session session = sessions.get();
        if (session == null) {
            session = sessionFactory.openSession();
            sessions.set(session);
        }
        return session;
    }

    public void releaseSession(Session session) {
        if (session != null) {
            if (session.isOpen()) session.close();
            sessions.remove();
        }
    }
}
