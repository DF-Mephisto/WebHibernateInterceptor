package com.example.utils;

import com.example.users.User;
import com.p6spy.engine.spy.P6DataSource;
import net.ttddyy.dsproxy.QueryInfo;
import net.ttddyy.dsproxy.proxy.ParameterSetOperation;
import net.ttddyy.dsproxy.support.ProxyDataSource;
import net.ttddyy.dsproxy.support.ProxyDataSourceBuilder;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.util.Properties;

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
                        configuration.getProperties().put(Environment.DATASOURCE, new P6DataSource(getPostgresDataSource(configuration)));
                        //System.setOut(new OutInterceptor(System.out));
                        //configuration.getProperties().put(Environment.DATASOURCE, getDataSource(configuration));
                        final StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()
                                .applySettings(configuration.getProperties());
                        sessionFactory = configuration
                                //.setInterceptor(new HibernateInterceptor())
                                .buildSessionFactory(builder.build());

                    } catch (final Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return sessionFactory;
    }

    private static DataSource getDataSource(final Configuration configuration) {
        final ProxyDataSource dataSource = ProxyDataSourceBuilder.create(getPostgresDataSource(configuration))
                //.logQueryByCommons(CommonsLogLevel.INFO)
                //.logQueryToSysOut()
                .countQuery()
                .multiline()
                .afterQuery((ei, qi) -> {
                    for (QueryInfo i : qi)
                    {
                        System.out.println(i.getQuery());
                        i.getParametersList().forEach(l -> {
                            for (ParameterSetOperation p : l)
                            {
                                for (Object o : p.getArgs())
                                    System.out.println(o);
                            }
                        });
                    }
                })
                /*.listener(new QueryExecutionListener() {
                    @Override
                    public void beforeQuery(ExecutionInfo info, List<QueryInfo> queryInfos) {
                        System.out.println("Before Query Execution");
                    }
                    @Override
                    public void afterQuery(ExecutionInfo info, List<QueryInfo> queryInfos) {
                        System.out.println("\nAfter Query Execution");
                    }
                })*/.build();

        return dataSource;
    }

    private static DataSource getPostgresDataSource(final Configuration configuration) {
        final PGSimpleDataSource ds = new PGSimpleDataSource() ;
        final Properties prop = configuration.getProperties();
        ds.setURL(prop.getProperty(Environment.URL));
        ds.setUser(Environment.USER);
        ds.setPassword(Environment.PASS);
        return ds;
    }
}
