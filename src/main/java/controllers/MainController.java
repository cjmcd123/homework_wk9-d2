package controllers;

import db.Seeds;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;

import java.util.HashMap;

import static spark.Spark.get;
import static spark.SparkBase.staticFileLocation;

public class MainController {

    public static void main(String[] args) {
        Seeds.seedData();
        staticFileLocation("/public");

        ManagersController managersController = new ManagersController();
        EmployeesController employeesController = new EmployeesController();
        EngineersController engineersController = new EngineersController();
        DepartmentController departmentController = new DepartmentController();

        get("/", (req, res) ->{
            HashMap<String, Object> model = new HashMap<>();
            model.put("template", "templates/home.vtl");
            return new ModelAndView(model, "templates/layout.vtl");
        }, new VelocityTemplateEngine());

    }

}
