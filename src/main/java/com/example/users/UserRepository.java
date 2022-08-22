package com.example.users;

import com.example.utils.HibernateSessionFactoryUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class UserRepository {
    private static final String hqlLoadlAll = "SELECT u FROM User u";
    private static final String hqlDeleteById = "DELETE FROM User WHERE id = :id";

    UserRepository()
    {

    }

    User loadUserById(final long id) {
        final Session session = getSession();
        final User user = session.get(User.class, id);
        session.close();
        return user;
    }

    List<User> loadAllUsers() {
        final Session session = getSession();
        final List<User> users = session.createQuery(hqlLoadlAll, User.class)
                .getResultList();
        session.close();
        return users;
    }

    void createUser(final User user) {
        final Session session = getSession();
        final Transaction tx1 = session.beginTransaction();
        session.persist(user);
        tx1.commit();
        session.close();
    }

    void putUser(final User user) {
        final Session session = getSession();
        final Transaction tx1 = session.beginTransaction();
        session.update(user);
        tx1.commit();
        session.close();
    }

    void deleteUserById(final long id) {
        final Session session = getSession();
        final Transaction tx1 = session.beginTransaction();
        session.createQuery(hqlDeleteById)
                .setParameter("id", id)
                .executeUpdate();
        tx1.commit();
        session.close();
    }

    private Session getSession()
    {
        return HibernateSessionFactoryUtil
                .getSessionFactory()
                .openSession();
    }
}
