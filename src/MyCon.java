import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class MyCon {
    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/car_rental", "root", "root");

            Statement stmnt = con.createStatement();
            ResultSet rs = stmnt.executeQuery("select * from shop");


            while (rs.next()){
                System.out.println(rs.getInt(1) + " " +
                        rs.getString(2) + " " +
                        rs.getString(3) /*+ " " +*/
                       /* rs.getDate(4)*/);
            }
        } catch(Exception ex) {
            System.out.println("Exception");
        }
    }
}
