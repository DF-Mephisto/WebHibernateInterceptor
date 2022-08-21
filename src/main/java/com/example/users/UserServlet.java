package com.example.users;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.stream.Collectors;

public class UserServlet extends HttpServlet {

    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;

    public UserServlet()
    {
        objectMapper = new ObjectMapper();
        userRepository = new UserRepository() ;
    }

    @Override
    protected void doGet(final HttpServletRequest req,
                         final HttpServletResponse resp) throws IOException {

        final String pathInfo = req.getRequestURI();
        final String[] pathParts = pathInfo.split("/");
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            if (pathParts.length == 2)
            {
                resp.getWriter().write(objectMapper.writeValueAsString(userRepository.loadAllUsers()));
            } else if (pathParts.length == 3)
            {
                resp.getWriter().write(objectMapper.writeValueAsString(userRepository.loadUserById(Long.valueOf(pathParts[2]))));
            } else
            {
                resp.getWriter().write("Incorrect path");
            }
        } catch (final Throwable e)
        {
            e.printStackTrace();
            resp.getWriter().write(e.getMessage());
        }
    }

    @Override
    protected void doPost(final HttpServletRequest req,
                          final HttpServletResponse resp) throws IOException {
        final String jsonUser = req.getReader()
                .lines()
                .collect(Collectors
                        .joining(System.lineSeparator()));

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            userRepository.createUser(objectMapper.readValue(jsonUser, User.class));
            resp.getWriter().write(jsonUser);
        } catch (final RuntimeException e)
        {
            resp.getWriter().write(e.getMessage());
        }
    }

    @Override
    protected void doDelete(final HttpServletRequest req,
                            final HttpServletResponse resp) throws IOException {
        final String pathInfo = req.getRequestURI();
        final String[] pathParts = pathInfo.split("/");

        resp.setCharacterEncoding("UTF-8");

        try {
            if (pathParts.length == 3)
            {
                userRepository.deleteUserById(Long.valueOf(pathParts[2]));
                resp.setStatus(204);
            } else
            {
                resp.getWriter().write("Incorrect path");
            }
        } catch (final RuntimeException e)
        {
            resp.getWriter().write(e.getMessage());
        }
    }

}
