package model.dao.implementation;

import db.DB;
import db.DbException;
import model.dao.DepartmentDAO;
import model.entities.Department;
import model.entities.Seller;

import javax.swing.table.TableRowSorter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDAOJDBC implements DepartmentDAO {

    private Connection connection = null;
    int rowsAffected;
    private List<Department> departments = new ArrayList<>();

    public DepartmentDAOJDBC(Connection connection){
        this.connection = connection;
    }

    @Override
    public void insert(Department dpt) {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(
                    "INSERT INTO department (name) VALUES (?) ", Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1,dpt.getName());
            rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected>0){
                ResultSet resultSet = preparedStatement.getGeneratedKeys();
                while (resultSet.next()){
                    int id = resultSet.getInt(1);
                    dpt.setId(id);
                }
                System.out.println("DEPARTMENT INSERTED SUCCESSFULLY!");
            }else{
                throw new DbException("DEPARTMENT NOT INSERTED, SOMETHING IS WRONG!");
            }
        }catch (SQLException e){
            throw new DbException(e.getMessage());
        }finally {
            DB.closeStatement(preparedStatement);
        }
    }

    @Override
    public void update(Department dpt) {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(
                    "UPDATE department SET Name=? WHERE ID=?");
            preparedStatement.setString(1,dpt.getName());
            preparedStatement.setInt(2,dpt.getId());
            rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected>0){
                System.out.println("DEPARTMENT UPDATED SUCCESSFULLY!");
            }else{
                throw new DbException("DEPARTMENT NOT UPDATED! SOMETHING IS WRONG...");
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }finally {
            DB.closeStatement(preparedStatement);
        }
    }

    @Override
    public void deleteById(int id) {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(
                    "DELETE FROM department WHERE ID=?");
            preparedStatement.setInt(1,id);
            rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected>0){
                System.out.println("DEPARTMENT DELETED SUCCESSFULLY!");
            }else{
                throw new DbException("DEPARTMENT NOT DELETED!SOMETHING WENT WRONG...");
            }
        }catch (SQLException e){
            throw new DbException(e.getMessage());
        }finally {
            DB.closeStatement(preparedStatement);
        }
    }

    @Override
    public Department findById(int id) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Department department = null;
        try{
            preparedStatement = connection.prepareStatement(
                    "SELECT * FROM department WHERE ID=?");
            preparedStatement.setInt(1,id);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                id = resultSet.getInt(1);
                String name = resultSet.getString(2);
                department= new Department(id,name);
            }
        }catch (SQLException e){
            throw new DbException(e.getMessage());
        }finally {
            DB.closeResultSet(resultSet);
            DB.closeStatement(preparedStatement);
        }
        return department;
    }

    @Override
    public List<Department> findAll() {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement(
                    "SELECT * FROM department");
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                Department department = new Department();
                department.setId(resultSet.getInt(1));
                department.setName(resultSet.getString(2));
                departments.add(department);
            }
        }catch (SQLException e){
            throw new DbException(e.getMessage());
        }finally {
            DB.closeResultSet(resultSet);
            DB.closeStatement(preparedStatement);
        }
        return departments;
    }
}
