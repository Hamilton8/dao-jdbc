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

        Seller newSeller = new Seller(null, "Cristiano Ronaldo", "ronaldo@gmail.com",new Date(),5000.00,new Department(2));
        sellerDAO.insert(newSeller);
        System.out.println(newSeller.getId());
    }
}
