package model.dao;

import model.entities.Department;

import java.util.List;

public interface DepartmentDAO {

    void insert(Department dpt);
    void update(Department dpt);
    void deleteById(int id);
    Department findById(int id);
    List<Department> findAll();
}
