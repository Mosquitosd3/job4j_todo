package controller;

import model.Users;
import store.HBRStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class RegServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        Users user = HBRStore.instOf().findByEmail(email);
        if (user == null) {
            HttpSession sc = req.getSession();
            sc.setAttribute("user", HBRStore.instOf().save(new Users(name, email, password)));
            resp.sendRedirect(req.getContextPath() + "/index.html");
        } else {
            req.setAttribute("error", "Такой пользователь уже зарегистрирован.");
            req.getRequestDispatcher("login.jsp").forward(req, resp);
        }
    }
}
