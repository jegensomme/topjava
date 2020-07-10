package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.MealRepository;
import ru.javawebinar.topjava.dao.InMemoryMealRepository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {

    private static final Logger log = getLogger(UserServlet.class);

    private static String INSERT_OR_EDIT = "/mealEdit.jsp";
    private static String MEALS = "/meals.jsp";

    private MealRepository repository;

    public MealServlet() {
        super();
        repository = new InMemoryMealRepository();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.info("getAll");
        String forward="";
        String action = request.getParameter("action");
        Integer mealId = null;
        Meal meal = null;
        switch (action) {
            case "delete":
                mealId = Integer.valueOf(request.getParameter("mealId"));
                repository.delete(mealId);
                forward = MEALS;
                request.setAttribute("meals", MealsUtil.getWithExceed(repository.getAll(), Meal.CALORIES_LIMIT));
                log.info("Delete {}", mealId);
                break;
            case "edit":
                forward = INSERT_OR_EDIT;
                mealId = Integer.valueOf(request.getParameter("mealId"));
                meal = (Meal) repository.get(getId(request));
                request.setAttribute("meal", meal);
                log.info("Edit {}", mealId);
                break;
            case "list":
                forward = MEALS;
                request.setAttribute("meals", MealsUtil.getWithExceed(repository.getAll(), Meal.CALORIES_LIMIT));
                log.info("Get list {}");
                break;
            case "create":
            default:
                meal = new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 0);
                request.setAttribute("meal", meal);
                forward = INSERT_OR_EDIT;
                log.info("Create {}");
        }

        RequestDispatcher view = request.getRequestDispatcher(forward);
        view.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.info("getAll");
        request.setCharacterEncoding("UTF-8");

        String mealId = request.getParameter("mealId");
        Meal meal = new Meal(mealId == null ? null : Integer.valueOf(mealId),
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.valueOf(request.getParameter("calories")));

        repository.save(meal);
        request.setAttribute("meals", MealsUtil.getWithExceed(repository.getAll(), Meal.CALORIES_LIMIT));
        request.getRequestDispatcher(MEALS).forward(request, response);
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("mealId"));
        return Integer.parseInt(paramId);
    }
}
