package com.company;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.Calendar;
import java.util.Map;
import java.util.Scanner;


public class Main {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        try {

            /*String table = input.next();

            String query = "Select * from CUSTOMER";
            Statement pstmt = con.prepareStatement(query);
            //((PreparedStatement) pstmt).setString(1, table);

            ResultSet rs = ((PreparedStatement) pstmt).executeQuery();

            while (rs.next()){
                System.out.println(rs.getString("FIRST_NAME"));
            }
            */

            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@//192.168.18.55:1521/pdb1.comsoft.local", "ivailo", "password");

            selectionQueries queries = new selectionQueries();
            int actionID = 0;


            do{
                menu();
                actionID = input.nextInt();

                switch (actionID) {
                    case 1:
                        queries.createorder(con);
                        break;
                    case 2:
                        queries.removeOrder(con);
                        break;
                    case 3:
                        queries.updateOrder(con);
                        break;
                    case 4:
                        queries.addProductToOrder(con);
                        break;
                    case 5:
                        queries.removeOrder(con);
                        break;
                    case 6:
                        queries.showOrders(con);
                        break;
                    case 7:
                        queries.showUsers(con);
                        break;

                }


            }while (actionID != 0);




        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public static void menu() {
        System.out.println("Enter the ID of the action you want to do or enter 0 to exit: ");
        System.out.println("1. Create new order");
        System.out.println("2. Delete Order");
        System.out.println("3. Update order");
        System.out.println("4. Add products to order");
        System.out.println("5. Remove products from order");
        System.out.println("6. show orders");
        System.out.println("7. show users");

    }

}
