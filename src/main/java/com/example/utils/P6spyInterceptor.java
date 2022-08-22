package com.example.utils;

import com.p6spy.engine.logging.Category;
import com.p6spy.engine.spy.appender.StdoutLogger;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;

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
        {
            System.out.println(sql);

            try {
                final Statement statement = CCJSqlParserUtil.parse(sql);
                statement.accept(new StatementVisitorImpl());
            } catch (final JSQLParserException e)
            {
                throw new RuntimeException(e.getMessage(), e);
            }
        }
    }

}
