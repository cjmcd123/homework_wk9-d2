package controllers;

import db.DBHelper;
import models.*;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;

import java.util.HashMap;
import java.util.List;

import static spark.Spark.get;
import static spark.Spark.post;

public class DepartmentController {

    public DepartmentController(){
        this.setUpEndPoints();
    }

    private void setUpEndPoints(){

        get("/departments", (req, res) -> {
            HashMap<String, Object> model = new HashMap<>();
            List<Department> departments = DBHelper.getAll(Department.class);
            model.put("template", "templates/departments/index.vtl");
            model.put("departments", departments);
            return new ModelAndView(model, "templates/layout.vtl");
        }, new VelocityTemplateEngine());

        get("/departments/new", (req, res) -> {
            HashMap<String, Object> model = new HashMap<>();
            model.put("template", "templates/departments/create.vtl");
            return new ModelAndView(model, "templates/layout.vtl");
        }, new VelocityTemplateEngine());

        get("/departments/:id", (req, res) -> {
            HashMap<String, Object> model = new HashMap<>();
            int id = Integer.parseInt(req.params(":id"));
            Department department = DBHelper.find(id, Department.class);
            Manager manager = DBHelper.findManagerForDept(department);
            List<Engineer> engineers = DBHelper.findEngineersForDept(department);
            model.put("template", "templates/departments/show.vtl");
            model.put("department", department);
            model.put("manager", manager);
            model.put("engineers", engineers);
            return new ModelAndView(model, "templates/layout.vtl");
        }, new VelocityTemplateEngine());

        get("/departments/:id/edit", (req, res) -> {
            HashMap<String, Object> model = new HashMap<>();
            int id = Integer.parseInt(req.params(":id"));
            Department department = DBHelper.find(id, Department.class);
            model.put("template", "templates/departments/edit.vtl");
            model.put("department", department);
            return new ModelAndView(model, "templates/layout.vtl");
        }, new VelocityTemplateEngine());

        post("/departments", (req, res) -> {
            String title = req.queryParams("title");
            Department department = new Department(title);
            DBHelper.save(department);
            res.redirect("/departments");
            return null;
        }, new VelocityTemplateEngine());

        post("/departments/:id", (req, res) -> {
            int id = Integer.parseInt(req.params(":id"));
            Department department = DBHelper.find(id, Department.class);
            department.setTitle(req.queryParams("title"));
            DBHelper.save(department);
            res.redirect("/departments");
            return null;
        }, new VelocityTemplateEngine());

        get("/departments/:id/delete", (req, res) -> {
            int id = Integer.parseInt(req.params(":id"));
            Department department = DBHelper.find(id, Department.class);
            DBHelper.delete(department);
            res.redirect("/departments");
            return null;
        }, new VelocityTemplateEngine());

    }
}
