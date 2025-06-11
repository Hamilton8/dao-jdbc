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
        System.out.println(seller);
        System.out.println();

        List<Seller> sellers = sellerDAO.findAll();
        for(Seller sell: sellers){
            System.out.println(sell);
        }

        Seller newSeller = new Seller(8, "Patty", "pat@gmail.com",new Date(),4000.00,new Department(4));
        sellerDAO.update(newSeller);
        System.out.println(newSeller.getId());

        sellerDAO.deleteById(8);
    }
}
