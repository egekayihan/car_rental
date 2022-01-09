import java.sql.*;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class MyCon {
    static Connection connection = createConnection();
    static Statement stmnt = createStatement();
    static ResultSet resultSet = null;
    static Scanner sc = new Scanner(System.in);
    static PreparedStatement preparedStatement;
    static final int portAddress = 3306;

    public static void main(String[] args) throws SQLException {

        int input = 0;

        while (true) {

            System.out.println();
            System.out.println("1. Insert");
            System.out.println("2. View");
            System.out.print("Which function would you like to run (0. Exit): ");
            input = sc.nextInt();

            if (input == 1) {
                getInserts();
            } else if (input == 2) {
                getTables();
            } else if (input == 0) {
                System.out.println("Have a nice Day");
                break;
            }
        }

    }

    public static void getInserts() throws SQLException {
        int input = 0;
        while (true) {
            System.out.println();
            System.out.println("1. Client");
            System.out.println("2. Invoices");
            System.out.print("Which table would you like to insert (0. Exit): ");
            input = sc.nextInt();

            if (input == 1) {
                insertClient();
                System.out.println();
            } else if (input == 2) {
                insertInvoice();
                System.out.println();
            } else if (input == 0) {
                return;
            }
        }
    }

    public static void insertClient() throws SQLException {
        System.out.print("Personal Number: ");
        int personalNumber = sc.nextInt();

        System.out.print("First Name: ");
        String firstName = sc.next();

        System.out.print("Last Name: ");
        String lastName = sc.next();

        System.out.print("Address: ");
        String address = sc.next();

        String query = "INSERT INTO client (personal_number, firstname, lastname, address) VALUES (?, ?, ?, ?)";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, personalNumber);
        preparedStatement.setString(2, firstName);
        preparedStatement.setString(3, lastName);
        preparedStatement.setString(4, address);

        preparedStatement.execute();

        getClients();
    }

    public static void insertInvoice() throws SQLException {
        System.out.print("Starting Date (YYYY-MM-DD): ");
        String startDateS = sc.next();
        Date startDateD = Date.valueOf(startDateS);

        System.out.print("Ending Date (YYYY-MM-DD): ");
        String endDateS = sc.next();
        Date endDateD = Date.valueOf(endDateS);

        while (endDateD.compareTo(startDateD) < 0){
            System.out.println("Ending Date can't be earlier than Starting Date");
            System.out.print("Ending Date (YYYY-MM-DD): ");
            endDateS = sc.next();
            endDateD = Date.valueOf(endDateS);
        }

        System.out.print("Client Personal Number: ");
        int clPersNum = sc.nextInt();

        System.out.print("Employee ID: ");
        int emId = sc.nextInt();

        System.out.print("Car ID: ");
        int caId = sc.nextInt();

        int cost = getCarFee(caId) * calculateRentingPeriod(startDateD, endDateD);

        String query = "INSERT INTO invoice (starting_date, ending_date, cost, cl_personal_number, emp_id, car_id) VALUES (?, ?, ?, ?, ?, ?)";

        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setDate(1, startDateD);
        preparedStatement.setDate(2, endDateD);
        preparedStatement.setInt(3, cost);
        preparedStatement.setInt(4, clPersNum);
        preparedStatement.setInt(5, emId);
        preparedStatement.setInt(6, caId);

        preparedStatement.execute();

        getInvoices();
    }

    public static int calculateRentingPeriod(Date start, Date end){
        long delta = end.getTime() - start.getTime();
        TimeUnit time = TimeUnit.DAYS;

        return (int) (time.convert(delta, TimeUnit.MILLISECONDS) + 1);
    }

    public static int getCarFee(int carID){
        String queryOfCarFee = "SELECT fee FROM car WHERE id=?";
        int cost = 0;

        try {
            preparedStatement = connection.prepareStatement(queryOfCarFee);
            preparedStatement.setInt(1, carID);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next())
                cost = resultSet.getInt(1);
        }catch (SQLException e){
            e.printStackTrace();
        }

        return cost;
    }

    public static void getTables() throws SQLException {
        int input = 0;
        while (true) {
            System.out.println("1. Shops");
            System.out.println("2. Cars");
            System.out.println("3. Clients");
            System.out.println("4. Employees");
            System.out.println("5. Invoices");
            System.out.print("Which table would you like to view (0. Exit): ");
            input = sc.nextInt();

            if (input == 1) {
                getShops();
                System.out.println();
            } else if (input == 2) {
                getCars();
                System.out.println();
            } else if (input == 3) {
                getClients();
                System.out.println();
            } else if (input == 4) {
                getEmployees();
                System.out.println();
            } else if (input == 5) {
                getInvoices();
                System.out.println();
            } else if (input == 0) {
                return;
            }
        }
    }

    public static Connection createConnection(){
        Connection con = null;
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:" + portAddress + "/car_rental";
            con = DriverManager.getConnection(url, "root", "root");

        } catch(Exception e) {
            e.printStackTrace();
        }
            return con;
    }

    public static Statement createStatement(){
        try{
            stmnt = connection.createStatement();
        } catch(Exception ex) {
            System.out.println("Exception");
        }
        return stmnt;
    }

    public static void getShops() throws SQLException {
        resultSet = stmnt.executeQuery("select * from shop");
        System.out.println();
        System.out.println("SHOPS");

        while (resultSet.next()){
            System.out.println("ID: " + resultSet.getInt(1) + "    City: " +
                    resultSet.getString(2) + "    Address: " +
                    resultSet.getString(3));
        }
    }

    public static void getCars() throws SQLException {
        resultSet = stmnt.executeQuery("select * from car");
        System.out.println();
        System.out.println("CARS");

        while (resultSet.next()){
            System.out.println("ID: " + resultSet.getInt(1) + "    Brand: " +
                    resultSet.getString(2) + "    Model: " +
                    resultSet.getString(3) + "    Production year: " +
                    resultSet.getString(4) + "    Fee: " +
                    resultSet.getString(5) + "    Shop ID: " +
                    resultSet.getString(6));
        }
    }

    public static void getInvoices() throws SQLException {
        resultSet = stmnt.executeQuery("select * from invoice");
        System.out.println();
        System.out.println("INVOICES");

        while (resultSet.next()){
            System.out.println("ID: " + resultSet.getInt(1) + "    Starting Date:  " +
                    resultSet.getString(2) + "    Ending Date: " +
                    resultSet.getString(3) + "    Cost: " +
                    resultSet.getString(4) + "    Client Personal Number: " +
                    resultSet.getString(5) + "    Employee ID: " +
                    resultSet.getString(6) + "    Car ID: " +
                    resultSet.getString(7));
        }
    }

    public static void getClients() throws SQLException {
        resultSet = stmnt.executeQuery("select * from client");
        System.out.println();
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
        System.out.println();
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
