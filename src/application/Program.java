package application;

import model.dao.DAOFactory;
import model.dao.SellerDAO;
import model.entities.Department;
import model.entities.Seller;

import java.util.Date;

public class Program {
    public static void main(String[] args) {
        Department department = new Department(1, "Hamil");
        System.out.println(department);
        Seller seller = new Seller(21,"Hamilton","g@gmail.com",new Date(),
                3300.0, department);

        System.out.println(seller);

        SellerDAO sellerDAO = DAOFactory.createSellerDAO();
    }
}
