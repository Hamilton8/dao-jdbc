package application;

import model.dao.DAOFactory;
import model.dao.SellerDAO;
import model.dao.implementation.SellerDAOJDBC;
import model.entities.Department;
import model.entities.Seller;

import java.util.Date;
import java.util.List;

public class Program {
    public static void main(String[] args) {

        SellerDAO sellerDAO = DAOFactory.createSellerDAO();

        Seller seller = sellerDAO.findById(2);
        List<Seller> sellers = sellerDAO.findByDepartment(new Department(4));
        for(Seller sell: sellers){
            System.out.println(sell);
        }
    }
}
