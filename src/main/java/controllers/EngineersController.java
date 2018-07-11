package controllers;

import db.DBHelper;
import models.Department;
import models.Engineer;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;

import java.util.HashMap;
import java.util.List;

import static spark.Spark.get;
import static spark.Spark.post;

public class EngineersController {

    public EngineersController(){
        this.setUpEndPoints();
    }

    private void setUpEndPoints(){

        get("/engineers", (req, res) -> {
            HashMap<String, Object> model = new HashMap<>();
            List<Engineer> engineers = DBHelper.getAll(Engineer.class);
            model.put("template", "templates/engineers/index.vtl");
            model.put("engineers", engineers);
            return new ModelAndView(model, "templates/layout.vtl");
        }, new VelocityTemplateEngine());

        get("/engineers/new", (req, res) -> {
            HashMap<String, Object> model = new HashMap<>();
            List<Department> departments = DBHelper.getAll(Department.class);
            model.put("departments", departments);
            model.put("template", "templates/engineers/create.vtl");
            return new ModelAndView(model, "templates/layout.vtl");
        }, new VelocityTemplateEngine());

        get("/engineers/:id", (req, res) -> {
            HashMap<String, Object> model = new HashMap<>();
            int id = Integer.parseInt(req.params(":id"));
            Engineer engineer = DBHelper.find(id, Engineer.class);
            model.put("template", "templates/engineers/show.vtl");
            model.put("engineer", engineer);
            return new ModelAndView(model, "templates/layout.vtl");
        }, new VelocityTemplateEngine());

        get("/engineers/:id/edit", (req, res) -> {
            HashMap<String, Object> model = new HashMap<>();
            int id = Integer.parseInt(req.params(":id"));
            Engineer engineer = DBHelper.find(id, Engineer.class);
            List<Department> departments = DBHelper.getAll(Department.class);
            model.put("template", "templates/engineers/edit.vtl");
            model.put("engineer", engineer);
            model.put("departments", departments);
            return new ModelAndView(model, "templates/layout.vtl");
        }, new VelocityTemplateEngine());

        post("/engineers", (req, res) -> {
            String firstName = req.queryParams("firstName");
            String lastName = req.queryParams("lastName");
            int salary = Integer.parseInt(req.queryParams("salary"));
            int departmentId = Integer.parseInt(req.queryParams("department"));
            Department department = DBHelper.find(departmentId, Department.class);
            Engineer engineer = new Engineer(firstName, lastName, salary, department);
            DBHelper.save(engineer);
            res.redirect("/engineers");
            return null;
        }, new VelocityTemplateEngine());

        post("/engineers/:id", (req, res) -> {
            int id = Integer.parseInt(req.params(":id"));
            Engineer engineer = DBHelper.find(id, Engineer.class);
            engineer.setFirstName(req.queryParams("firstName"));
            engineer.setLastName(req.queryParams("lastName"));
            int salary = Integer.parseInt(req.queryParams("salary"));
            engineer.setSalary(salary);
            int departmentId = Integer.parseInt(req.queryParams("department"));
            Department department = DBHelper.find(departmentId, Department.class);
            engineer.setDepartment(department);
            DBHelper.save(engineer);
            res.redirect("/engineers");
            return null;
        }, new VelocityTemplateEngine());

        get("/engineers/:id/delete", (req, res) -> {
            int id = Integer.parseInt(req.params(":id"));
            Engineer engineer = DBHelper.find(id, Engineer.class);
            DBHelper.delete(engineer);
            res.redirect("/engineers");
            return null;
        }, new VelocityTemplateEngine());

    }
}
