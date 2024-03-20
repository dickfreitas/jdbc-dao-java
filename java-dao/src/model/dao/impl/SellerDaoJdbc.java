package model.dao.impl;

import db.DB;
import db.DbException;
import model.dao.SellerDAO;
import model.entities.Department;
import model.entities.Seller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class SellerDaoJdbc implements SellerDAO {

    private Connection connection;
    public SellerDaoJdbc(Connection conn){
        this.connection = conn;
    }
    @Override
    public void insert(Seller department) {

    }

    @Override
    public void update(Seller department) {

    }

    @Override
    public void deleteById(Integer id) {

    }

    @Override
    public Seller findById(Integer id) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement(
                    "SELECT seller.* , department.Name as DepName " +
                            "FROM seller " +
                            "INNER JOIN department " +
                            "ON seller.DepartmentId = department.id " +
                            "WHERE seller.Id = ?"
            );
            preparedStatement.setInt(1,id);
            resultSet = preparedStatement.executeQuery();
            //VENDO SE HA UMA TABELA COMO RESULTADO
            if (resultSet.next()){
                //Instanciando o departamento
                Department dept = new Department();
                //Pegando a informação do id do departamento e jogando para a classe para instaciala
                dept.setId(resultSet.getInt("DepartmentId"));
                //Tem a mesma ideia do de cima, pegando o nome do departamneto na tabela para instaciar
                dept.setName(resultSet.getString("DepName"));

                Seller seller = new Seller();
                seller.setId(resultSet.getInt("Id"));
                seller.setName(resultSet.getString("Name"));
                seller.setEmail(resultSet.getString("Email"));
                seller.setBaseSalary(resultSet.getDouble("BaseSalary"));
                seller.setBirthDate(resultSet.getDate("BirthDate"));
                seller.setDepartment(dept);

                return seller;

            }
            return  null;
        }catch (SQLException e){
            throw new  DbException(e.getMessage());
        }finally {
            DB.closeStatement(preparedStatement);
            DB.closeResultSet(resultSet);
        }

    }

    @Override
    public List<Seller> findAll() {
        return null;
    }
}
