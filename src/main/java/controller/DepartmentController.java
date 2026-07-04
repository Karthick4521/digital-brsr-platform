package com.indiapost.brsrplatform.controller;

import com.indiapost.brsrplatform.entity.Department;
import com.indiapost.brsrplatform.service.DepartmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/departments")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    // View all departments
    @GetMapping
    public String listDepartments(Model model) {
        model.addAttribute("departments",
                departmentService.getAllDepartments());
        model.addAttribute("activePage", "departments");
        return "department/list";
    }

    // Show add form
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("department", new Department());
        model.addAttribute("activePage", "departments");
        return "department/form";
    }

    // Save new department
    @PostMapping("/save")
    public String saveDepartment(@Valid @ModelAttribute("department") Department department,
                                 BindingResult result,
                                 RedirectAttributes redirectAttributes,
                                 Model model) {
        if (result.hasErrors()) {
            return "department/form";
        }
        if (department.getId() == null && departmentService.codeExists(department.getCode())) {
            model.addAttribute("codeError", "Department code already exists!");
            return "department/form";
        }
        departmentService.saveDepartment(department);
        redirectAttributes.addFlashAttribute("successMsg",
                "Department saved successfully!");
        return "redirect:/departments";
    }

    // Show edit form
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Department department = departmentService.getDepartmentById(id);
        if (department == null) {
            return "redirect:/departments";
        }
        model.addAttribute("department", department);
        model.addAttribute("activePage", "departments");
        return "department/form";
    }

    // Delete department
    @GetMapping("/delete/{id}")
    public String deleteDepartment(@PathVariable Long id,
                                   RedirectAttributes redirectAttributes) {
        departmentService.deleteDepartment(id);
        redirectAttributes.addFlashAttribute("successMsg",
                "Department deleted successfully!");
        return "redirect:/departments";
    }
}