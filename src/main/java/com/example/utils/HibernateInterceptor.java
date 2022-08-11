package com.example.utils;

import com.example.users.User;
import org.hibernate.EmptyInterceptor;
import org.hibernate.Transaction;
import org.hibernate.type.Type;

import java.io.Serializable;


public class HibernateInterceptor extends EmptyInterceptor {
    @Override
    public boolean onSave(Object entity, Serializable id,
                          Object[] state, String[] propertyNames, Type[] types) {

        if (entity instanceof User) {
            System.out.println(entity);
        }

        return super.onSave(entity, id, state, propertyNames, types);
    }

    @Override
    public boolean onLoad(Object entity, Serializable id,
                          Object[] state, String[] propertyNames, Type[] types) {

        if (entity instanceof User) {
            for (Object o : state)
                System.out.println(o);
            for (String str : propertyNames)
                System.out.println(str);
            for (Type t : types)
                System.out.println(t);
        }

        return super.onLoad(entity, id, state, propertyNames, types);
    }

    @Override
    public String onPrepareStatement(String sql) {
        System.out.println(sql);
        return super.onPrepareStatement(sql);
    }

    @Override
    public void afterTransactionBegin(Transaction tx)
    {
        System.out.println(tx);
        super.afterTransactionBegin(tx);
    }

    @Override
    public void afterTransactionCompletion(Transaction tx)
    {
        System.out.println(tx);
        super.afterTransactionCompletion(tx);
    }
}