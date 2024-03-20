package application;

import model.dao.DaoFactory;
import model.dao.SellerDAO;
import model.entities.Department;
import model.entities.Seller;

import java.util.List;

public class Program {
    public static void main(String[] args) {
        SellerDAO sellerDAO = DaoFactory.createSellerDao();

        System.out.println("----FindById------");
        Seller seller = sellerDAO.findById(3);

        System.out.println(seller);

        System.out.println("\n----FindByDepartment------");
        Department department = new Department(2, null);
        List<Seller> list = sellerDAO.findByDepartment(department);
        for (Seller obj : list) {
            System.out.println(obj);
        }
    }
}
