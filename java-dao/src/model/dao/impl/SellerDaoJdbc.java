package model.dao.impl;

import db.DB;
import db.DbException;
import model.dao.SellerDAO;
import model.entities.Department;
import model.entities.Seller;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SellerDaoJdbc implements SellerDAO {

    private Connection connection;
    public SellerDaoJdbc(Connection conn){
        this.connection = conn;
    }
    @Override
    public void insert(Seller obj) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyy");
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement(
                    "INSERT INTO seller" +
                            "(Name , Email , BirthDate , BaseSalary , DepartmentId )" +
                            "VALUES" +
                            "(? , ? , ? , ? , ?)" ,  +
                            Statement.RETURN_GENERATED_KEYS
            );
            preparedStatement.setString(1 , obj.getName());
            preparedStatement.setString(2 , obj.getEmail());
            preparedStatement.setDate(3 , new java.sql.Date(obj.getBirthDate().getTime()));
            preparedStatement.setDouble(4 , obj.getBaseSalary());
            preparedStatement.setInt(5 , obj.getDepartment().getId());

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0){
                resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet.next()){
                    int id = resultSet.getInt(1);
                    obj.setId(id);
                }
            }else {
                throw  new DbException("Erro inesperado, nenhuma linha afetada");
            }


        }catch (SQLException e){
            throw new  DbException(e.getMessage());
        }finally {
            DB.closeStatement(preparedStatement);
            DB.closeResultSet(resultSet);
        }

    }

    @Override
    public void update(Seller obj) {


        PreparedStatement preparedStatement = null;

        try{
            preparedStatement = connection.prepareStatement(
                    "UPDATE seller " +
                            "SET Name = ? , Email = ? , BirthDate = ? , BaseSalary = ? , DepartmentId = ? " +
                            "WHERE Id = ? "
            );

            preparedStatement.setString(1 , obj.getName());
            preparedStatement.setString(2 , obj.getEmail());
            preparedStatement.setDate(3 , new java.sql.Date(obj.getBirthDate().getTime()));
            preparedStatement.setDouble(4 , obj.getBaseSalary());
            preparedStatement.setInt(5 , obj.getDepartment().getId());
            preparedStatement.setInt(6 , obj.getId());

            preparedStatement.executeUpdate();

        }catch(SQLException e){
            throw new DbException(e.getMessage());

        }


    }

    @Override
    public void deleteById(Integer id) {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(
                    "Delete " +
                            "FROM seller " +
                            "WHERE id = ?" ,+
                            Statement.RETURN_GENERATED_KEYS
            );

            preparedStatement.setInt(1 , id);
            preparedStatement.executeUpdate();
        }catch (SQLException e){
            throw new DbException(e.getMessage());
        }

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
                Department dept = instantiateDepartment(resultSet);

                Seller seller = instantiateSeller(resultSet,dept);

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

    private Seller instantiateSeller(ResultSet resultSet, Department dept) throws SQLException {
        Seller seller = new Seller();
        seller.setId(resultSet.getInt("Id"));
        seller.setName(resultSet.getString("Name"));
        seller.setEmail(resultSet.getString("Email"));
        seller.setBaseSalary(resultSet.getDouble("BaseSalary"));
        seller.setBirthDate(resultSet.getDate("BirthDate"));
        seller.setDepartment(dept);
        return seller;
    }

    //FUNÇÃO AUXILIAR PRA NAO DEIXAR TAO VERBOSO OS METODOS
    private Department instantiateDepartment(ResultSet resultSet) throws SQLException {
        Department dept = new Department();
        //Pegando a informação do id do departamento e jogando para a classe para instaciala
        dept.setId(resultSet.getInt("DepartmentId"));
        //Tem a mesma ideia do de cima, pegando o nome do departamneto na tabela para instaciar
        dept.setName(resultSet.getString("DepName"));
        return dept;
    }

    @Override
    public List<Seller> findAll() {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try{
            preparedStatement = connection.prepareStatement(
                    "SELECT seller.*,department.Name as DepName "
                            + "FROM seller INNER JOIN department "
                            + "ON seller.DepartmentId = department.Id "
                            + "ORDER BY Name"
            );
            resultSet = preparedStatement.executeQuery();

            List<Seller> list = new ArrayList<>();
            Map<Integer ,Department> map = new HashMap<>();
            while (resultSet.next()){
                Department dep = map.get(resultSet.getInt("DepartmentId"));

                if (dep == null){
                    dep = instantiateDepartment(resultSet);
                    map.put(resultSet.getInt("DepartmentId") , dep);
                }

                Seller obj = instantiateSeller(resultSet , dep);
                list.add(obj);
            }

            return  list;

        }catch (SQLException e){
            throw  new DbException(e.getMessage());
        }finally {
            DB.closeStatement(preparedStatement);
            DB.closeResultSet(resultSet);
        }
    }

    @Override
    public List<Seller> findByDepartment(Department department) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = connection.prepareStatement(
                    "SELECT seller.*,department.Name as DepName "
                            + "FROM seller INNER JOIN department "
                            + "ON seller.DepartmentId = department.Id "
                            + "WHERE DepartmentId = ? "
                            + "ORDER BY Name");

            st.setInt(1, department.getId());

            rs = st.executeQuery();

            List<Seller> list = new ArrayList<>();
            Map<Integer, Department> map = new HashMap<>();

            while (rs.next()) {

                Department dep = map.get(rs.getInt("DepartmentId"));

                if (dep == null) {
                    dep = instantiateDepartment(rs);
                    map.put(rs.getInt("DepartmentId"), dep);
                }

                Seller obj = instantiateSeller(rs, dep);
                list.add(obj);
            }
            return list;
        }
        catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }
}
