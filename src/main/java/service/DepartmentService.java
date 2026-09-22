package com.indiapost.brsrplatform.service;

import com.indiapost.brsrplatform.entity.Department;
import com.indiapost.brsrplatform.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    public List<Department> getAllActiveDepartments() {
        return departmentRepository.findByIsActiveTrue();
    }

    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    public Department getDepartmentById(Long id) {
        return departmentRepository.findById(id).orElse(null);
    }

    public void saveDepartment(Department department) {
        departmentRepository.save(department);
    }

    public void deleteDepartment(Long id) {
        Department department = getDepartmentById(id);
        if (department != null) {
            department.setIsActive(false);
            departmentRepository.save(department);
        }
    }

    public boolean codeExists(String code) {
        return departmentRepository.existsByCode(code);
    }
}