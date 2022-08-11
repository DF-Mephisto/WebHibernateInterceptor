package com.example.utils;

import com.example.users.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class HibernateSessionFactoryUtil {
    private static volatile SessionFactory sessionFactory;

    private HibernateSessionFactoryUtil() {}

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            synchronized (HibernateSessionFactoryUtil.class)
            {
                if (sessionFactory == null)
                {
                    try {
                        final Configuration configuration = new Configuration().configure();
                        configuration.addAnnotatedClass(User.class);
                        final StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()
                                .applySettings(configuration.getProperties());
                        sessionFactory = configuration
                                .setInterceptor(new HibernateInterceptor())
                                .buildSessionFactory(builder.build());

                    } catch (final Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return sessionFactory;
    }
}
