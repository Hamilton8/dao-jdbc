package application;

import model.dao.DAOFactory;
import model.dao.SellerDAO;
import model.dao.implementation.SellerDAOJDBC;
import model.entities.Seller;

import java.util.Date;

public class Program {
    public static void main(String[] args) {

        SellerDAO sellerDAO = DAOFactory.createSellerDAO();

        Seller seller = sellerDAO.findById(2);
        System.out.println(seller);
    }
}
