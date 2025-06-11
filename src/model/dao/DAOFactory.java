package model.dao;

import db.DB;
import model.dao.implementation.DepartmentDAOJDBC;
import model.dao.implementation.SellerDAOJDBC;

public class DAOFactory {

    public static SellerDAO createSellerDAO(){
        return new SellerDAOJDBC(DB.getConnection());
    }

    public static DepartmentDAO createDepartmentDAO(){
        return new DepartmentDAOJDBC(DB.getConnection());
    }
}
