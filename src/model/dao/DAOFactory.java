package model.dao;

import model.dao.implementation.SellerDAOJDBC;

public class DAOFactory {

    public static SellerDAO createSellerDAO(){
        return new SellerDAOJDBC();
    }
}
