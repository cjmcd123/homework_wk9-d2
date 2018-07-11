package controllers;

import db.DBHelper;
import models.Department;
import models.Employee;
import models.Engineer;
import models.Manager;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;

import java.util.HashMap;
import java.util.List;

import static spark.Spark.get;
import static spark.Spark.post;

public class ManagersController {

    public ManagersController(){
        this.setUpEndPoints();
    }

    private void setUpEndPoints(){

        get("/managers", (req, res) -> {
            HashMap<String, Object> model = new HashMap<>();
            List<Manager> managers = DBHelper.getAll(Manager.class);
            model.put("template", "templates/managers/index.vtl");
            model.put("managers", managers);
            return new ModelAndView(model, "templates/layout.vtl");
        }, new VelocityTemplateEngine());

        get("/managers/new", (req, res) -> {
            HashMap<String, Object> model = new HashMap<>();
            List<Department> departments = DBHelper.getAll(Department.class);
            model.put("template", "templates/managers/create.vtl");
            model.put("departments", departments);
            return new ModelAndView(model, "templates/layout.vtl");
        }, new VelocityTemplateEngine());

        get("/managers/:id", (req, res) -> {
            HashMap<String, Object> model = new HashMap<>();
            int id = Integer.parseInt(req.params(":id"));
            Manager manager = DBHelper.find(id, Manager.class);
            List<Engineer> engineers = DBHelper.findEngineersForManager(manager);
            model.put("template", "templates/managers/show.vtl");
            model.put("manager", manager);
            model.put("engineers", engineers);
            return new ModelAndView(model, "templates/layout.vtl");
        }, new VelocityTemplateEngine());

        get("/managers/:id/edit", (req, res) -> {
            HashMap<String, Object> model = new HashMap<>();
            int id = Integer.parseInt(req.params(":id"));
            Manager manager = DBHelper.find(id, Manager.class);
            List<Department> departments = DBHelper.getAll(Department.class);
            model.put("template", "templates/managers/edit.vtl");
            model.put("manager", manager);
            model.put("departments", departments);
            return new ModelAndView(model, "templates/layout.vtl");
        }, new VelocityTemplateEngine());

        post("/managers", (req, res) -> {
            String firstName = req.queryParams("firstName");
            String lastName = req.queryParams("lastName");
            int salary = Integer.parseInt(req.queryParams("salary"));
            int departmentId = Integer.parseInt(req.queryParams("department"));
            Department department = DBHelper.find(departmentId, Department.class);
            double budget = Double.parseDouble(req.queryParams("budget"));
            Manager manager = new Manager(firstName, lastName, salary, department, budget);
            DBHelper.save(manager);
            res.redirect("/managers");
            return null;
        }, new VelocityTemplateEngine());

        post("/managers/:id", (req, res) -> {
            int id = Integer.parseInt(req.params(":id"));
            Manager manager = DBHelper.find(id, Manager.class);
            manager.setFirstName(req.queryParams("firstName"));
            manager.setLastName(req.queryParams("lastName"));
            int salary = Integer.parseInt(req.queryParams("salary"));
            manager.setSalary(salary);
            int departmentId = Integer.parseInt(req.queryParams("department"));
            Department department = DBHelper.find(departmentId, Department.class);
            manager.setDepartment(department);
            double budget = Double.parseDouble(req.queryParams("budget"));
            manager.setBudget(budget);
            DBHelper.save(manager);
            res.redirect("/managers");
            return null;
        }, new VelocityTemplateEngine());

        get("/managers/:id/delete", (req, res) -> {
            int id = Integer.parseInt(req.params(":id"));
            Manager manager = DBHelper.find(id, Manager.class);
            DBHelper.delete(manager);
            res.redirect("/managers");
            return null;
        }, new VelocityTemplateEngine());

    }

}
