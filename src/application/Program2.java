package application;

import model.dao.DAOFactory;
import model.dao.DepartmentDAO;
import model.dao.implementation.DepartmentDAOJDBC;
import model.entities.Department;

import java.util.ArrayList;
import java.util.List;

public class Program2 {
    public static void main(String[] args) {
        DepartmentDAO departmentDAO = DAOFactory.createDepartmentDAO();
        List<Department> departments = new ArrayList<>();

        departmentDAO.deleteById(8);
        departmentDAO.insert(new Department(5,"IT"));
        departmentDAO.deleteById(9);
        departmentDAO.update(new Department(6,"Mechanic"));

        departments = departmentDAO.findAll();
        departments.forEach(System.out::println);

        System.out.println();
        System.out.println(departmentDAO.findById(2));


    }
}
