package controllers;

import db.DBHelper;
import db.Seeds;
import models.Employee;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;

import java.util.HashMap;
import java.util.List;

import static spark.Spark.get;

public class EmployeesController {

    public EmployeesController(){
        this.setUpEndPoints();
    }

    public void setUpEndPoints(){


        get("/employees", (req, res) -> {
            HashMap<String, Object> model = new HashMap<>();
            List<Employee> employees = DBHelper.getAll(Employee.class);
            model.put("template", "templates/employees/index.vtl");
            model.put("employees", employees);
            return new ModelAndView(model, "templates/layout.vtl");
        }, new VelocityTemplateEngine());

    }

}
