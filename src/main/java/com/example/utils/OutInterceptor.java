package com.example.utils;

import java.io.*;

public class OutInterceptor extends PrintStream {
    public OutInterceptor(OutputStream out) {
        super(out);
    }

    @Override
    public void print(String log)
    {
        super.print("Intercepted: " + log);
    }

    @Override
    public void println(String log)
    {
        print(log + "\n");
    }
}
