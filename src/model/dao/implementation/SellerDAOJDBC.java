package model.dao.implementation;

import db.DB;
import db.DbException;
import model.dao.SellerDAO;
import model.entities.Department;
import model.entities.Seller;

import java.sql.*;
import java.sql.Date;
import java.time.Instant;
import java.util.*;

public class SellerDAOJDBC implements SellerDAO {

    private Connection connection;
    private List<Seller> sellers = new ArrayList<>();
    Scanner input = new Scanner(System.in);

    public SellerDAOJDBC(Connection connection){
        this.connection = connection;
    }

    @Override
    public void insert(Seller seller) {
        PreparedStatement preparedStatement=null;
        try{
             preparedStatement = connection.prepareStatement(
                     "INSERT INTO seller "
                             + "(Name,Email,birthDate,baseSalary,DepartmentId) "
                             + "VALUES (?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
             preparedStatement.setString(1,seller.getName());
             preparedStatement.setString(2, seller.getEmail());
             preparedStatement.setDate(3, new Date(seller.getBirthDate().getTime()));
             preparedStatement.setDouble(4,seller.getBaseSalary());
             preparedStatement.setInt(5,seller.getDepartment().getId());

             int rowsAffected = preparedStatement.executeUpdate();
             if(rowsAffected>0){
                 ResultSet resultSet = preparedStatement.getGeneratedKeys();
                 if(resultSet.next()){
                     int id = resultSet.getInt(1);
                     seller.setId(id);
                 }
                 DB.closeResultSet(resultSet);
             }else{
                 throw new DbException("Somethin' is wrong! NO ROWS AFFECTED...");
             }

        }catch (SQLException e){
            throw new DbException(e.getMessage());
        }

    }

    @Override
    public void update(Seller seller) {
        try{
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE seller " +
                    "SET Name=?, Email=?, birthDate=?, baseSalary=?, DepartmentId=? WHERE ID=?",Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, seller.getName());
            preparedStatement.setString(2, seller.getEmail());
            preparedStatement.setDate(3,new Date(seller.getBirthDate().getTime()));
            preparedStatement.setDouble(4,seller.getBaseSalary());
            preparedStatement.setInt(5,seller.getDepartment().getId());
            preparedStatement.setInt(6,seller.getId());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected>0){
                System.out.println("SELLER UPDATED SUCCESSFULLY!");
            }else{
                throw new DbException("Somethin' is Wrong! SELLER NOT UPDATED...");
            }
        }catch (SQLException e){
            throw new DbException(e.getMessage());
        }
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
                    "AS DepName " +
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
        department.setName(resultSet.getString("DepName"));
        return department;
    }

    @Override
    public List<Seller> findAll() {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try{
            Map<Integer, Department> map = new HashMap<>();
            preparedStatement = connection.prepareStatement("SELECT seller.*,department.Name " +
                    "AS DepName " +
                    "FROM seller INNER JOIN department " +
                    "ON seller.DepartmentId =department.ID " +
                    "ORDER BY Name");
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                Department department = map.get(resultSet.getInt("DepartmentId"));
                if(department == null){
                    department = instatiateDepartment(resultSet);
                    map.put(department.getId(),department);
                }
                Seller seller = instatiateSeller(resultSet,department);
                sellers.add(seller);
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        return sellers;
    }

    @Override
    public List<Seller> findByDepartment(Department department) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try{
            preparedStatement = connection.prepareStatement("SELECT seller.*, department.Name " +
                    "AS DepName " +
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
