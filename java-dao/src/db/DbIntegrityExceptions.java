package db;

public class DbIntegrityExceptions extends RuntimeException {

    public DbIntegrityExceptions(String msg){
        super(msg);
    }
}
