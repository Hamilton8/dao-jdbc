package model.dao.implementation;

import db.DB;
import db.DbException;
import model.dao.SellerDAO;
import model.entities.Department;
import model.entities.Seller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class SellerDAOJDBC implements SellerDAO {

    private Connection connection;
    private List<Seller> sellers = new ArrayList<>();

    public SellerDAOJDBC(Connection connection){
        this.connection = connection;
    }

    @Override
    public void insert(Seller seller) {

    }

    @Override
    public void update(Seller seller) {

    }

    @Override
    public void deleteById(int id) {

    }

    @Override
    public Seller findById(int id) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement("SELECT seller.*, department.Name " +
                    "AS NameDep " +
                    "FROM seller INNER JOIN department " +
                    "ON seller.DepartmentId = department.ID " +
                    "WHERE seller.Id = ?");
            preparedStatement.setInt(1,id);
            resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                Department department = instatiateDepartment(resultSet);
                Seller seller = instatiateSeller(resultSet,department);
                return seller;
            }
            return null;
        }catch(SQLException e){
            throw new DbException(e.getMessage());
        }
    }

    private Seller instatiateSeller(ResultSet resultSet, Department department) throws SQLException{
        Seller seller = new Seller();
        seller.setId(resultSet.getInt(1));
        seller.setName(resultSet.getString("Name"));
        seller.setEmail(resultSet.getString("Email"));
        seller.setBirthDate(resultSet.getDate("BirthDate"));
        seller.setBaseSalary(resultSet.getDouble("BaseSalary"));
        seller.setDepartment(department);
        return seller;
    }

    private Department instatiateDepartment(ResultSet resultSet) throws SQLException{
        Department department = new Department();
        department.setId(resultSet.getInt("ID"));
        department.setName(resultSet.getString("Name"));
        return department;
    }

    @Override
    public List<Seller> findAll() {
        return List.of();
    }

    @Override
    public List<Seller> findByDepartment(Department department) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try{
            preparedStatement = connection.prepareStatement("SELECT seller.*, department.Name " +
                    "AS NameDep " +
                    "FROM seller INNER JOIN department " +
                    "ON seller.DepartmentId = department.ID " +
                    "WHERE DepartmentId = ? ORDER BY Name");
            preparedStatement.setInt(1, department.getId());
            resultSet = preparedStatement.executeQuery();
            Map<Integer, Department> departmentMap = new HashMap<>();

            while (resultSet.next()){
                Department dpt = departmentMap.get(resultSet.getInt("DepartmentId"));
                if (dpt==null) {
                    dpt = instatiateDepartment(resultSet);
                    departmentMap.put(resultSet.getInt("DepartmentId"),dpt);
                }
                Seller seller = instatiateSeller(resultSet,dpt);
                sellers.add(seller);
            }
            return sellers;
        }catch (SQLException e){
            throw new DbException(e.getMessage());
        }finally {
            DB.closeResultSet(resultSet);
            DB.closeStatement(preparedStatement);
        }
    }

}
