import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class MyCon {
    static Connection connection = createConnection();
    static Statement stmnt = createStatement();
    static ResultSet resultSet = null;
    static Scanner sc = new Scanner(System.in);
    static PreparedStatement preparedStatement;
    static final int portAddress = 3307;
    static int employeeShopID = 0;
    static ArrayList<Integer> availableCarsArr = new ArrayList<Integer>();

    public static void main(String[] args) throws SQLException {
        int input = 0;

        employeeLogin();

        while (true) {
            System.out.println("------------------------------------------------------");
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

    public static void getCarsInACity() throws SQLException {
        int i = 1;
        String city = "";
        int input = 0;
        ArrayList<String> arr = new ArrayList<String>();
        resultSet = stmnt.executeQuery("select city from shop");

        while (resultSet.next()){
            System.out.println(i + ". "+ resultSet.getString(1));
            arr.add(resultSet.getString(1));
            i++;
        }

        while(true) {
            System.out.print("Choose a city: ");
            input = sc.nextInt();

            if (input > 0 && input <= arr.size()) {
                city = arr.get(input - 1);
                break;
            }
            else
                System.out.println("Invalid Input");
        }

        String query = "SELECT car.brand, car.model, COUNT(invoice.car_id) " +
                "FROM car " +
                "JOIN invoice ON invoice.car_id = car.id " +
                "JOIN shop ON shop.id = shop_id " +
                "WHERE shop.city = ? " +
                "GROUP BY invoice.car_id " +
                "ORDER BY COUNT(invoice.car_id) DESC";

        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, city);

        resultSet = preparedStatement.executeQuery();
        System.out.println();
        System.out.format("%17s%18s%26s", "Car Brand", "Car Model", "Number of Rents");
        System.out.println();
        System.out.println("------------------------------------------------------------------");

        while (resultSet.next()){
            System.out.format("%15s%19s%21s", resultSet.getString(1), resultSet.getString(2),
                    resultSet.getString(3));
            System.out.println();
//            System.out.println("Car Brand: " + resultSet.getString(1)
//                    + "    Car Model: " + resultSet.getString(2)
//                    + "    Number of Rents: " + resultSet.getInt(3));
        }
        System.out.println("------------------------------------------------------------------");
    }

    public static void getEmployeesPerformance() throws SQLException {
        System.out.print("Shop ID: ");
        int shopID = sc.nextInt();

        String query = "SELECT employee.firstname, employee.lastname, COUNT(invoice.emp_id), SUM(invoice.cost) " +
                "FROM employee " +
                "JOIN invoice ON invoice.emp_id = employee.id " +
                "WHERE employee.shop_id = ? " +
                "GROUP BY invoice.emp_id " +
                "ORDER BY SUM(invoice.cost) DESC";

        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, shopID);

        resultSet = preparedStatement.executeQuery();
        System.out.println();
        System.out.format("%15s%16s%30s%21s",  "First Name", "Last Name","Number of Cars Rented", "Total Earning");
        System.out.println();
        System.out.println("--------------------------------------------------------------------------------------------");

        while (resultSet.next()){
            System.out.format("%12s%20s%18s%27s", resultSet.getString(1), resultSet.getString(2),
                    resultSet.getString(3), resultSet.getString(4));
            System.out.println();
//            System.out.println("First Name: " + resultSet.getString(1)
//                    + "    Last Name: " + resultSet.getString(2)
//                    + "    Number of Cars Rented: " + resultSet.getString(3)
//                    + "    Total Earning: " + resultSet.getString(4));
        }
        System.out.println("--------------------------------------------------------------------------------------------");
    }

    public static void employeeLogin() throws SQLException {
        System.out.print("Employee ID: ");
        int id = sc.nextInt();

        String query = "select shop_id from employee where employee.id = ?";
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, id);

        resultSet = preparedStatement.executeQuery();

        if(resultSet.next())
            employeeShopID = resultSet.getInt(1);
    }

    public static void getAvailableCars() throws SQLException {
        System.out.print("Starting Date (YYYY-MM-DD): ");
        String startDateS = sc.next();
        Date startDateD = Date.valueOf(startDateS);

        System.out.print("Ending Date (YYYY-MM-DD): ");
        String endDateS = sc.next();
        Date endDateD = Date.valueOf(endDateS);

        String query = "SELECT invoice.car_id FROM invoice " +
                "WHERE NOT(ending_date < ? OR starting_date > ?) ";

        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setDate(1, startDateD);
        preparedStatement.setDate(2, endDateD);

        resultSet = preparedStatement.executeQuery();
        System.out.println();
        System.out.println("   Available Car IDs");
        System.out.println("--------------------------");

        while (resultSet.next()){
            System.out.println("             " + resultSet.getInt(1));
        }
        System.out.println("--------------------------");
    }

    public static void getCarsInShop() throws SQLException {
        availableCarsArr = new ArrayList<Integer>();
        System.out.print("Starting Date (YYYY-MM-DD): ");
        String startDateS = sc.next();
        Date startDateD = Date.valueOf(startDateS);

        System.out.print("Ending Date (YYYY-MM-DD): ");
        String endDateS = sc.next();
        Date endDateD = Date.valueOf(endDateS);

        String query = "SELECT invoice.car_id FROM invoice " +
                "WHERE NOT(ending_date < ? OR starting_date > ?) ";

        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setDate(1, startDateD);
        preparedStatement.setDate(2, endDateD);

        resultSet = preparedStatement.executeQuery();

        while (resultSet.next())
            availableCarsArr.add(resultSet.getInt(1));


        String query2 = "SELECT brand, model, production_year, fee FROM car " +
                "WHERE shop_id = ?";

        for(int i = 0; i < availableCarsArr.size(); i++)
            query2 = query2 + " AND id != ?";

        preparedStatement = connection.prepareStatement(query2);
        preparedStatement.setInt(1, employeeShopID);

        for(int i = 0; i < availableCarsArr.size(); i++)
            preparedStatement.setInt(i + 2, availableCarsArr.get(i));

        resultSet = preparedStatement.executeQuery();
        System.out.println();
        System.out.format("%15s%18s%21s%17s", "Car Brand", "Car Model", "Production Year", "Fee");
        System.out.println();
        System.out.println("-----------------------------------------------------------------------------");

        while (resultSet.next()){
            System.out.format("%16s%16s%20s%19s", resultSet.getString(1), resultSet.getString(2),
                    resultSet.getString(3), resultSet.getString(4));
            System.out.println();
//            System.out.println("Car Brand: " + resultSet.getString(1) + "    Car Model: " +
//                    resultSet.getString(2) + "    Production Year: " +
//                    resultSet.getString(3) + "    Fee: " +
//                    resultSet.getString(4));
        }
        System.out.println("-----------------------------------------------------------------------------");
    }

    public static void getPrevInvoices() throws SQLException {
        System.out.print("Personal Number: ");
        int personalNumber = sc.nextInt();

        String query = "SELECT firstname, lastname, car.brand, car.model, starting_date, ending_date " +
                "FROM client " +
                "JOIN invoice ON invoice.cl_personal_number = client.personal_number " +
                "JOIN car ON car.id = invoice.car_id " +
                "WHERE client.personal_number = ?;";

        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, personalNumber);

        resultSet = preparedStatement.executeQuery();
        System.out.println();
        System.out.format("%15s%16s%20s%20s%20s%22s", "First Name", "Last Name", "Car Brand", "Car Model", "Starting Date", "Ending Date");
        System.out.println();
        System.out.println("---------------------------------------------------------------------------------------------------------------------");

        while (resultSet.next()){
            System.out.format("%14s%17s%18s%21s%20s%23s", resultSet.getString(1), resultSet.getString(2),
                    resultSet.getString(3), resultSet.getString(4), resultSet.getDate(5),
                    resultSet.getDate(6));
            System.out.println();
//            System.out.println("First Name: " + resultSet.getString(1) + "    Last Name: " +
//                    resultSet.getString(2) + "    Car Brand: " +
//                    resultSet.getString(3) + "    Car Model: " +
//                    resultSet.getString(4) + "    Starting Date: " +
//                    resultSet.getDate(5) + "    Ending Date: " +
//                    resultSet.getDate(6));
        }
        System.out.println("---------------------------------------------------------------------------------------------------------------------");
    }

    public static void getInserts() throws SQLException {
        int input = 0;
        while (true) {
            System.out.println("------------------------------------------------------");
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
            System.out.println();
            System.out.println("1. Shops");
            System.out.println("2. Cars");
            System.out.println("3. Clients");
            System.out.println("4. Employees");
            System.out.println("5. Invoices");
            System.out.println();
            System.out.println("6. Get Previous Invoices of a Person");
            System.out.println("7. Get Available Cars in a Certain Period");
            System.out.println("8. Get Available Cars in a Certain Shop");
            System.out.println("9. Get Employee Performances in a Shop");
            System.out.println("10. Get Cars in a City");
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
            } else if (input == 6) {
                getPrevInvoices();
                System.out.println();
            }else if (input == 7) {
                getAvailableCars();
                System.out.println();
            }else if (input == 8) {
                getCarsInShop();
                System.out.println();
            }else if (input == 9) {
                getEmployeesPerformance();
                System.out.println();
            }else if (input == 10) {
                getCarsInACity();
                System.out.println();
            }else if (input == 0) {
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
        System.out.println();
        System.out.format("%15s%18s%24s", "ID", "City", "Address");
        System.out.println();
        System.out.println("------------------------------------------------------------------");

        while (resultSet.next()){
            System.out.format("%15s%18s%25s", resultSet.getInt(1), resultSet.getString(2),
                    resultSet.getString(3));
            System.out.println();
            /*System.out.println("ID: " + resultSet.getInt(1) + "    City: " +
                    resultSet.getString(2) + "    Address: " +
                    resultSet.getString(3));*/
        }
        System.out.println("------------------------------------------------------------------");
    }

    public static void getCars() throws SQLException {
        resultSet = stmnt.executeQuery("select * from car");
        System.out.println();
        System.out.println("CARS");
        System.out.println();
        System.out.format("%15s%16s%20s%22s%18s%21s", "ID", "Brand", "Model", "Production Year", "Fee", "Shop ID");
        System.out.println();
        System.out.println("------------------------------------------------------------------------------------------------------------------");

        while (resultSet.next()){
            System.out.format("%15s%18s%18s%20s%20s%19s", resultSet.getInt(1), resultSet.getString(2),
                    resultSet.getString(3), resultSet.getString(4), resultSet.getString(5),
                    resultSet.getString(6));
            System.out.println();
            /*System.out.println("ID: " + resultSet.getInt(1) + "    Brand: " +
                    resultSet.getString(2) + "    Model: " +
                    resultSet.getString(3) + "    Production year: " +
                    resultSet.getString(4) + "    Fee: " +
                    resultSet.getString(5) + "    Shop ID: " +
                    resultSet.getString(6));*/
        }
        System.out.println("------------------------------------------------------------------------------------------------------------------");
    }

    public static void getInvoices() throws SQLException {
        resultSet = stmnt.executeQuery("select * from invoice");
        System.out.println();
        System.out.println("INVOICES");
        System.out.println();

        System.out.format("%15s%16s%18s%10s%28s%16s%16s", "ID", "Starting Date", "Ending Date", "Cost", "Client Personal Number",
                "Employee ID", "Car ID");
        System.out.println();
        System.out.println("---------------------------------------------------------------------------------------------------------------" +
                "--------------------");

        while (resultSet.next()){
            System.out.format("%15s%16s%18s%10s%20s%19s%18s", resultSet.getInt(1), resultSet.getString(2),
                    resultSet.getString(3), resultSet.getString(4), resultSet.getString(5),
                    resultSet.getString(6), resultSet.getString(7));
            System.out.println();
            /*System.out.println("ID: " + resultSet.getInt(1) + "    Starting Date:  " +
                    resultSet.getString(2) + "    Ending Date: " +
                    resultSet.getString(3) + "    Cost: " +
                    resultSet.getString(4) + "    Client Personal Number: " +
                    resultSet.getString(5) + "    Employee ID: " +
                    resultSet.getString(6) + "    Car ID: " +
                    resultSet.getString(7));*/
        }
        System.out.println("---------------------------------------------------------------------------------------------------------------" +
                "--------------------");
    }

    public static void getClients() throws SQLException {
        resultSet = stmnt.executeQuery("select * from client");
        System.out.println();
        System.out.println("CLIENTS");
        System.out.println();

        System.out.format("%20s%16s%18s%21s", "Personal Number", "First Name", "Last Name", "Address");
        System.out.println();
        System.out.println("--------------------------------------------------------------------------------------------");

        while (resultSet.next()){
            System.out.format("%16s%16s%20s%25s", resultSet.getInt(1), resultSet.getString(2),
                    resultSet.getString(3), resultSet.getString(4));
            System.out.println();

            /*System.out.println("Personal Number: " + resultSet.getInt(1) + "    First Name: " +
                    resultSet.getString(2) + "    Last Name: " +
                    resultSet.getString(3) + "    Address: " +
                    resultSet.getString(4));*/
        }
        System.out.println("--------------------------------------------------------------------------------------------");
    }

    public static void getEmployees() throws SQLException {
        resultSet = stmnt.executeQuery("select * from employee");
        System.out.println();
        System.out.println("EMPLOYEES");
        System.out.println();

        System.out.format("%16s%21s%16s%18s%18s", "ID", "First Name", "Last Name", "Title", "Shop ID");
        System.out.println();
        System.out.println("--------------------------------------------------------------------------------------------");

        while (resultSet.next()){
            System.out.format("%16s%19s%19s%18s%14s", resultSet.getInt(1), resultSet.getString(2),
                    resultSet.getString(3), resultSet.getString(4), resultSet.getString(5));
            System.out.println();
            /*System.out.println("ID: " + resultSet.getInt(1) + "    First Name: " +
                    resultSet.getString(2) + "    Last Name: " +
                    resultSet.getString(3) + "    Title: " +
                    resultSet.getString(4) + "    Shop ID: " +
                    resultSet.getString(5));*/
        }
        System.out.println("--------------------------------------------------------------------------------------------");
    }
}
