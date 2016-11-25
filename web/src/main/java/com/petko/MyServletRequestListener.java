package com.petko;

import com.petko.utils.HibernateUtilLibrary;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;

public class MyServletRequestListener implements ServletRequestListener {
    private HibernateUtilLibrary util;
    private Session session;
    private Transaction transaction;

    @Override
    public void requestInitialized(ServletRequestEvent servletRequestEvent) {
        util = HibernateUtilLibrary.getHibernateUtil();
        session = util.getSession();
        transaction = session.beginTransaction();
    }

    @Override
    public void requestDestroyed(ServletRequestEvent servletRequestEvent) {
        if (transaction != null && !transaction.wasRolledBack()) transaction.commit();
        util.releaseSession(session);
    }
}
