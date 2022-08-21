package com.example.utils;

import com.p6spy.engine.logging.Category;
import com.p6spy.engine.spy.appender.StdoutLogger;

public class P6spyInterceptor extends StdoutLogger {

    @Override
    public void logSQL(final int connectionId,
                       final String now,
                       final long elapsed,
                       final Category category,
                       final String prepared,
                       final String sql,
                       final String s) {
        if (category.getName().equals("statement"))
            System.out.println(sql);
    }

}
