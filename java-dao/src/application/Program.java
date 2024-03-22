package application;

import model.dao.DaoFactory;
import model.dao.SellerDAO;
import model.entities.Department;
import model.entities.Seller;

import java.util.Date;
import java.util.List;

public class Program {
    public static void main(String[] args) {
        SellerDAO sellerDAO = DaoFactory.createSellerDao();

        System.out.println("----FindById------");
        Seller seller = sellerDAO.findById(5);

        System.out.println(seller);

        System.out.println("\n----FindByDepartment------");
        Department department = new Department(2, null);
        List<Seller> list = sellerDAO.findByDepartment(department);
        for (Seller obj : list) {
            System.out.println(obj);
        }

        System.out.println("\n----FindAll-----");
        List<Seller> list2 = sellerDAO.findAll();
        for (Seller obj : list2){
            System.out.println(obj);
        }

         System.out.println("\n----Insert-----");
        Seller newSeller = new Seller(null , "Greg" , "greg@gmail.com" , new Date() , 4000.0 , department);
        sellerDAO.insert(newSeller);
        System.out.println("Inserted! New id = " + newSeller.getId() );



        System.out.println("\n----Update-----");
        seller = sellerDAO.findById(8);
        seller.setName("Seu jorge");
        sellerDAO.update(seller);

        System.out.println("Update complete");

        System.out.println("\n----Delete-----");
        sellerDAO.deleteById(8);
        System.out.println("Delete complete");
    }
}
