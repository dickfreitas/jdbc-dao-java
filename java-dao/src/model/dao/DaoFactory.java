package model.dao;

import model.dao.impl.SellerDaoJdbc;

/*
UMA INJEÇÃO DE DEPENDENCIAS MANUAL, SERVE PARA QUE O PROGRAMA PRINCIPAL
 NÃO TENHA CONTATO COM A CLASSE QUE ESTARA IMPLEMENTANDO AS COISAS COM O BANCO DE DADOS
GERANDO ASSIM UMA SEGURANÇA MAIOR.
 */

public class DaoFactory {
    public static SellerDAO createSellerDao(){
        return new SellerDaoJdbc();
    }
}
