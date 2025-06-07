package model.dao.implementation;

import db.DbException;
import model.dao.DepartmentDAO;
import model.entities.Department;
import model.entities.Seller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class DepartmentDAOJDBC implements DepartmentDAO {

    private Connection connection;

    public DepartmentDAOJDBC(Connection connection){
        this.connection = connection;
    }

    @Override
    public void insert(Department dpt) {

    }

    @Override
    public void update(Department dpt) {

    }

    @Override
    public void deleteById(int id) {

    }

    @Override
    public Department findById(int id) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try{
            preparedStatement = connection.prepareStatement("SELECT seller.*, department.name " +
                    "AS NameDep " +
                    "FROM seller INNER JOIN department " +
                    "ON seller.DepartmentId = department.ID " +
                    "WHERE DepartmentId =? ORDER BY Name");
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()){

            }
        }catch (SQLException e){
            throw new DbException(e.getMessage());
        }
        return null;
    }

    @Override
    public List<Department> findAll() {
        return List.of();
    }
}
