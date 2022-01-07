import java.sql.*;

public class MyCon {
    static Statement stmnt = makeConnection();
    static ResultSet resultSet = null;

    public static void main(String[] args) throws SQLException {
        getShops();
        System.out.println();

        getCars();
        System.out.println();

        getClients();
        System.out.println();

        getEmployees();
        System.out.println();

        getInvoices();
        System.out.println();

    }

    public static Statement makeConnection(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3307/car_rental", "root", "root");

            stmnt = con.createStatement();
        } catch(Exception ex) {
            System.out.println("Exception");
        }
        return stmnt;
    }

    public static void getShops() throws SQLException {
        resultSet = stmnt.executeQuery("select * from shop");
        if (resultSet.next())
            System.out.println("SHOPS");

        while (resultSet.next()){
            System.out.println("ID: " + resultSet.getInt(1) + "    City: " +
                    resultSet.getString(2) + "    Address: " +
                    resultSet.getString(3));
        }
    }

    public static void getCars() throws SQLException {
        resultSet = stmnt.executeQuery("select * from car");
        if (resultSet.next())
            System.out.println("CARS");

        while (resultSet.next()){
            System.out.println("ID: " + resultSet.getInt(1) + "    Brand: " +
                    resultSet.getString(2) + "    Model: " +
                    resultSet.getString(3) + "    Production year: " +
                    resultSet.getString(4) + "    Fee: " +
                    resultSet.getString(5) + "    Shop ID: " );
        }
    }

    public static void getInvoices() throws SQLException {
        resultSet = stmnt.executeQuery("select * from invoice");
        if (resultSet.next())
            System.out.println("INVOICES");

        while (resultSet.next()){
            System.out.println("ID: " + resultSet.getInt(1) + "    Starting Date:  " +
                    resultSet.getString(2) + "    Ending Date: " +
                    resultSet.getString(3) + "    Cost: " +
                    resultSet.getString(3) + "    Client Personal Number: " +
                    resultSet.getString(3) + "    Employee ID: " +
                    resultSet.getString(3) + "    Car ID: " +
                    resultSet.getString(3));
        }
    }

    public static void getClients() throws SQLException {
        resultSet = stmnt.executeQuery("select * from client");
        if (resultSet.next())
            System.out.println("CLIENTS");

        while (resultSet.next()){
            System.out.println("Personal Number: " + resultSet.getInt(1) + "    First Name: " +
                    resultSet.getString(2) + "    Last Name: " +
                    resultSet.getString(3) + "    Address: " +
                    resultSet.getString(4));
        }
    }

    public static void getEmployees() throws SQLException {
        resultSet = stmnt.executeQuery("select * from employee");
        if (resultSet.next())
            System.out.println("EMPLOYEES");

        while (resultSet.next()){
            System.out.println("ID: " + resultSet.getInt(1) + "    First Name: " +
                    resultSet.getString(2) + "    Last Name: " +
                    resultSet.getString(3) + "    Title: " +
                    resultSet.getString(4) + "    Shop ID: " +
                    resultSet.getString(5));
        }
    }
}
