package com.company;

import javax.swing.plaf.nimbus.State;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;


public class selectionQueries {
    Scanner input = new Scanner(System.in);
    Scanner numericalInpup = new Scanner(System.in);

    public void showUsers(Connection con){
        try {
            String selectAllUsersQ = "select * from CUSTOMER";
            Statement selectAllUsersST = con.prepareStatement(selectAllUsersQ);
            ResultSet selectAllUsersRS = ((PreparedStatement) selectAllUsersST).executeQuery();


            while (selectAllUsersRS.next()){
                System.out.println("UserID: " + selectAllUsersRS.getLong("ID") + "  Name: " + selectAllUsersRS.getString("FIRST_NAME"));
            }

        }catch (Exception e){
            e.printStackTrace();
        }


    }

    public void showOrders(Connection con) {
        try {
            System.out.println("type the first name of the user to search the orders of");
            String f_name = input.nextLine();
            String query = "select c.FIRST_NAME, o.*, p.NAME from CUSTOMER c inner join \"ORDER\" o on c.ID = o.CUSTOMER_ID inner join PRODUCT_ORDER PO on o.ID = PO.ORDER_ID inner join PRODUCT P on PO.PRODUCT_ID = P.ID where FIRST_NAME = ? order by o.ID";
            Statement statement = con.prepareStatement(query);
            ((PreparedStatement) statement).setString(1, f_name);
            ResultSet rs = ((PreparedStatement) statement).executeQuery();

            while (rs.next()) {
                System.out.print("Order ID: " + rs.getString("ID") + "  ");
                System.out.print(rs.getString("FIRST_NAME") + "   ");
                System.out.print(rs.getString("DESTINATION_COUNTRY") + "   ");
                System.out.print(rs.getString("DISTINATION_CITY") + "   ");
                System.out.print(rs.getString("DESTINATION_ADRESS") + "   ");
                System.out.println(rs.getString("NAME"));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createorder(Connection con) {
        try {
            System.out.println("type the first name of the user about to make an order");
            long customerID = getUserID(con);

            System.out.println("what type of payment are you going to use?");
            System.out.println("1.Cash");
            System.out.println("2.Debit");
            System.out.println("3.Credit");
            System.out.println("Type a number");
            int paymentTypeID = numericalInpup.nextInt();
            String paymentTypeCommonCode = " ";

            switch (paymentTypeID) {
                case 1:
                    paymentTypeCommonCode = "CASH";
                    break;
                case 2:
                    paymentTypeCommonCode = "DEBIT";
                    break;
                case 3:
                    paymentTypeCommonCode = "CREDIT";
                    break;
            }

            String selectPaymentTypeIDquery = "select pt.ID from PAYMENT_TYPE pt where COMMON_CODE = ?";
            Statement selectPaymentTypeStatement = con.prepareStatement(selectPaymentTypeIDquery);
            ((PreparedStatement) selectPaymentTypeStatement).setString(1, paymentTypeCommonCode);
            ResultSet paymentTypeIDRS = ((PreparedStatement) selectPaymentTypeStatement).executeQuery();
            paymentTypeIDRS.next();
            paymentTypeID = paymentTypeIDRS.getInt("ID");

            System.out.println("What type of delivery would you like?");
            System.out.println("1.Standard");
            System.out.println("2.Fast");
            System.out.println("3.Express");
            System.out.println("Type a number");
            int deliveryTypeID = numericalInpup.nextInt();
            String deliveryTypeCommonCode = " ";

            switch (deliveryTypeID) {
                case 1:
                    deliveryTypeCommonCode = "STANDARD";
                    break;
                case 2:
                    deliveryTypeCommonCode = "FAST";
                    break;
                case 3:
                    deliveryTypeCommonCode = "EXPRESS";
                    break;
            }

            String selectDeliveryTypeIDquery = "select dt.ID from DELIVERY_TYPE dt where COMMON_CODE = ?";
            Statement selectDeliveryTypeStatement = con.prepareStatement(selectDeliveryTypeIDquery);
            ((PreparedStatement) selectDeliveryTypeStatement).setString(1, deliveryTypeCommonCode);
            ResultSet deliveryTypeIDRS = ((PreparedStatement) selectDeliveryTypeStatement).executeQuery();
            deliveryTypeIDRS.next();
            deliveryTypeID = deliveryTypeIDRS.getInt("ID");

            System.out.println("Insert destination country");
            String destinationCountry = input.nextLine();
            System.out.println("Insert destination city");
            String destinationCity = input.nextLine();
            System.out.println("Insert destination adress");
            String destinationAdress = input.nextLine();

            String insertOrder = "insert into \"ORDER\" (DESTINATION_COUNTRY, DISTINATION_CITY, DESTINATION_ADRESS, CUSTOMER_ID, PAYMENT_TYPE_ID, DELIVERY_TYPE_ID) values (?,?,?,?,?,?)";
            Statement insertOrderStatement = con.prepareStatement(insertOrder);
            ((PreparedStatement) insertOrderStatement).setString(1, destinationCountry);
            ((PreparedStatement) insertOrderStatement).setString(2, destinationCity);
            ((PreparedStatement) insertOrderStatement).setString(3, destinationAdress);
            ((PreparedStatement) insertOrderStatement).setLong(4, customerID);
            ((PreparedStatement) insertOrderStatement).setLong(5, paymentTypeID);
            ((PreparedStatement) insertOrderStatement).setLong(6, deliveryTypeID);
            int result = ((PreparedStatement) insertOrderStatement).executeUpdate();


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void addProductToOrder(Connection con) {
        try {
            System.out.println("Type the first name of the user about to add products to order");
            long customerID = getUserID(con);


            String selectAllOrdersFromUserQuery = "select o.ID, o.DESTINATION_COUNTRY, o.DISTINATION_CITY, o.DESTINATION_ADRESS from CUSTOMER c inner join \"ORDER\" o on c.ID = o.CUSTOMER_ID where CUSTOMER_ID = ?";
            Statement selectAllOrdersStatement = con.prepareStatement(selectAllOrdersFromUserQuery);
            ((PreparedStatement) selectAllOrdersStatement).setLong(1, customerID);
            ResultSet allCustomerOrdersRS = ((PreparedStatement) selectAllOrdersStatement).executeQuery();
            System.out.println("Please select the ID of your order");
            while (allCustomerOrdersRS.next()) {
                System.out.print("ID: " + allCustomerOrdersRS.getLong("ID"));
                System.out.print("  " + allCustomerOrdersRS.getString("DESTINATION_COUNTRY"));
                System.out.println(" " + allCustomerOrdersRS.getString("DISTINATION_CITY"));
                System.out.println(" " + allCustomerOrdersRS.getString("DESTINATION_ADRESS"));
            }
            long orderID = numericalInpup.nextLong();

            String getAllProductsQ = "select * from PRODUCT";
            Statement selectAllProductsS = con.prepareStatement(getAllProductsQ);
            ResultSet allProductsRS = ((PreparedStatement) selectAllProductsS).executeQuery();
            System.out.println("Enter the ID's of products you want to add to your order one by one");
            System.out.println("Enter 0 to exit");
            while (allProductsRS.next()) {
                System.out.print("ID: " + allProductsRS.getLong("ID"));
                System.out.print(" " + allProductsRS.getString("Name") + "    Price: ");
                System.out.println(allProductsRS.getString("Price"));
            }
            int productToOrderID = 0;

            do {
                try {
                    productToOrderID = numericalInpup.nextInt();
                    String insertProductIntoOrder = "insert into PRODUCT_ORDER (PRODUCT_ID, ORDER_ID) values (?, ?)";
                    Statement insertProductsIntoOrderST = con.prepareStatement(insertProductIntoOrder);
                    ((PreparedStatement) insertProductsIntoOrderST).setLong(1, productToOrderID);
                    ((PreparedStatement) insertProductsIntoOrderST).setLong(2, orderID);
                    int rows = ((PreparedStatement) insertProductsIntoOrderST).executeUpdate();

                } catch (Exception e) {
                    System.out.println("not a valid ID for a product");
                }
            } while (productToOrderID != 0);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeProductFromOrder(Connection con) {
        try {
            System.out.println("type the first name of the user about to remove products from order");
            long customerID = getUserID(con);

            String selectAllOrdersFromUserQuery = "select o.ID, o.DESTINATION_COUNTRY, o.DISTINATION_CITY, o.DESTINATION_ADRESS from CUSTOMER c inner join \"ORDER\" o on c.ID = o.CUSTOMER_ID where CUSTOMER_ID = ?";
            Statement selectAllOrdersStatement = con.prepareStatement(selectAllOrdersFromUserQuery);
            ((PreparedStatement) selectAllOrdersStatement).setLong(1, customerID);
            ResultSet allCustomerOrdersRS = ((PreparedStatement) selectAllOrdersStatement).executeQuery();
            System.out.println("Please select the ID of your order");
            while (allCustomerOrdersRS.next()) {
                System.out.print("ID: " + allCustomerOrdersRS.getLong("ID"));
                System.out.print("  " + allCustomerOrdersRS.getString("DESTINATION_COUNTRY"));
                System.out.println(" " + allCustomerOrdersRS.getString("DISTINATION_CITY"));
                System.out.println(" " + allCustomerOrdersRS.getString("DESTINATION_ADRESS"));
            }
            long orderID = numericalInpup.nextLong();


            long productToRemove = 0;


            do {
                String getProductsInOrderQ = "select po.ID, P.NAME, P.PRICE from PRODUCT_ORDER po inner join PRODUCT P on po.PRODUCT_ID = P.ID where ORDER_ID = ? order by po.ID";
                Statement selectProductsInOrderST = con.prepareStatement(getProductsInOrderQ);
                ((PreparedStatement) selectProductsInOrderST).setLong(1, orderID);
                ResultSet productsInOrderRS = ((PreparedStatement) selectProductsInOrderST).executeQuery();
                System.out.println("Enter the ID's of products you want to remove from your order");
                System.out.println("Enter 0 to exit");

                while (productsInOrderRS.next()) {
                    System.out.print("ID: " + productsInOrderRS.getLong("ID"));
                    System.out.print(" " + productsInOrderRS.getString("Name") + "    Price: ");
                    System.out.println(productsInOrderRS.getString("Price"));
                }

                productToRemove = numericalInpup.nextLong();

                try {
                    System.out.println("rder ID = " + orderID);
                    System.out.println("product to remove = " + productToRemove);

                    String removeProductFromOrderQ = "delete from PRODUCT_ORDER where ORDER_ID = ? and ID = ?";
                    Statement removeProductsFrmOrderST = con.prepareStatement(removeProductFromOrderQ);
                    ((PreparedStatement) removeProductsFrmOrderST).setLong(1, orderID);
                    ((PreparedStatement) removeProductsFrmOrderST).setLong(2, productToRemove);
                    int rows = ((PreparedStatement) removeProductsFrmOrderST).executeUpdate();
                    System.out.println("rows deleted = " + rows);

                } catch (Exception e) {
                    e.printStackTrace();
                }


            } while (productToRemove != 0);


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void removeOrder(Connection con) {
        try {
            System.out.println("Type the first name of the user about to remove an order");
            long customerID = getUserID(con);

            System.out.println("Enter the ID of the order you want to remove");

            String selectOrdersFromCustomerQ = "select ID, DESTINATION_COUNTRY, DISTINATION_CITY, DESTINATION_ADRESS from \"ORDER\" where CUSTOMER_ID = ?";
            Statement selectOrdersFromCustomerST = con.prepareStatement(selectOrdersFromCustomerQ);
            ((PreparedStatement) selectOrdersFromCustomerST).setLong(1, customerID);
            ResultSet ordersFromCustomerRS = ((PreparedStatement) selectOrdersFromCustomerST).executeQuery();
            System.out.println("select ID of the order you want to remove: ");

            while (ordersFromCustomerRS.next()) {
                System.out.println("ID: " + ordersFromCustomerRS.getLong("ID") + "   Country: " + ordersFromCustomerRS.getString("DESTINATION_COUNTRY") + " \n " + ordersFromCustomerRS.getString("DISTINATION_CITY") + " " + ordersFromCustomerRS.getString("DESTINATION_ADRESS"));
            }

            long orderID = numericalInpup.nextLong();

            String removeOrderByIDQ = "delete from \"ORDER\" where ID = ?";
            Statement deleteOrderByIDST = con.prepareStatement(removeOrderByIDQ);
            ((PreparedStatement) deleteOrderByIDST).setLong(1, orderID);
            int rows = ((PreparedStatement) deleteOrderByIDST).executeUpdate();
            System.out.println(rows);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void updateOrder(Connection con){

        try {
            System.out.println("Type the name of the customer who wants to update order");
            long customerID = getUserID(con);
            System.out.println("Enter the ID of the order you want to remove");

            String selectOrdersFromCustomerQ = "select ID, DESTINATION_COUNTRY, DISTINATION_CITY, DESTINATION_ADRESS from \"ORDER\" where CUSTOMER_ID = ?";
            Statement selectOrdersFromCustomerST = con.prepareStatement(selectOrdersFromCustomerQ);
            ((PreparedStatement) selectOrdersFromCustomerST).setLong(1, customerID);
            ResultSet ordersFromCustomerRS = ((PreparedStatement) selectOrdersFromCustomerST).executeQuery();

            System.out.println("select ID of the order you want to update: ");

            while (ordersFromCustomerRS.next()) {
                System.out.println("ID: " + ordersFromCustomerRS.getLong("ID") + "   Country: " + ordersFromCustomerRS.getString("DESTINATION_COUNTRY") + " \n " + ordersFromCustomerRS.getString("DISTINATION_CITY") + " " + ordersFromCustomerRS.getString("DESTINATION_ADRESS"));
            }

            long orderID = numericalInpup.nextLong();

            System.out.println("enter the new destination country");
            String newDestinationCountry = input.nextLine();
            System.out.println("Enter new destination city");
            String newDestinationCity = input.nextLine();
            System.out.println("enter new destination adress");
            String newDestinationAdress = input.nextLine();


            System.out.println("what type of payment are you going to use?");
            System.out.println("1.Cash");
            System.out.println("2.Debit");
            System.out.println("3.Credit");
            System.out.println("Type a number");
            int paymentTypeID = numericalInpup.nextInt();
            String paymentTypeCommonCode = " ";

            switch (paymentTypeID) {
                case 1:
                    paymentTypeCommonCode = "CASH";
                    break;
                case 2:
                    paymentTypeCommonCode = "DEBIT";
                    break;
                case 3:
                    paymentTypeCommonCode = "CREDIT";
                    break;
            }

            String selectPaymentTypeIDquery = "select pt.ID from PAYMENT_TYPE pt where COMMON_CODE = ?";
            Statement selectPaymentTypeStatement = con.prepareStatement(selectPaymentTypeIDquery);
            ((PreparedStatement) selectPaymentTypeStatement).setString(1, paymentTypeCommonCode);
            ResultSet paymentTypeIDRS = ((PreparedStatement) selectPaymentTypeStatement).executeQuery();
            paymentTypeIDRS.next();
            paymentTypeID = paymentTypeIDRS.getInt("ID");

            System.out.println("What type of delivery would you like to change to?");
            System.out.println("1.Standard");
            System.out.println("2.Fast");
            System.out.println("3.Express");
            System.out.println("Type a number");
            int deliveryTypeID = numericalInpup.nextInt();
            String deliveryTypeCommonCode = " ";

            switch (deliveryTypeID) {
                case 1:
                    deliveryTypeCommonCode = "STANDARD";
                    break;
                case 2:
                    deliveryTypeCommonCode = "FAST";
                    break;
                case 3:
                    deliveryTypeCommonCode = "EXPRESS";
                    break;
            }

            String selectDeliveryTypeIDquery = "select dt.ID from DELIVERY_TYPE dt where COMMON_CODE = ?";
            Statement selectDeliveryTypeStatement = con.prepareStatement(selectDeliveryTypeIDquery);
            ((PreparedStatement) selectDeliveryTypeStatement).setString(1, deliveryTypeCommonCode);
            ResultSet deliveryTypeIDRS = ((PreparedStatement) selectDeliveryTypeStatement).executeQuery();
            deliveryTypeIDRS.next();
            deliveryTypeID = deliveryTypeIDRS.getInt("ID");

            String updateOrderQ = "update \"ORDER\" set DESTINATION_COUNTRY = ?, DISTINATION_CITY = ?, DESTINATION_ADRESS = ?, PAYMENT_TYPE_ID = ?, DELIVERY_TYPE_ID = ? where ID = ?";
            Statement updateOrderST = con.prepareStatement(updateOrderQ);
            ((PreparedStatement) updateOrderST).setString(1, newDestinationCountry);
            ((PreparedStatement) updateOrderST).setString(2, newDestinationCity);
            ((PreparedStatement) updateOrderST).setString(3, newDestinationAdress);
            ((PreparedStatement) updateOrderST).setLong(4, paymentTypeID);
            ((PreparedStatement) updateOrderST).setLong(5, deliveryTypeID);
            ((PreparedStatement) updateOrderST).setLong(6, orderID);
            int rows = ((PreparedStatement) updateOrderST).executeUpdate();
            System.out.println(rows);


        }catch (Exception e ){
            e.printStackTrace();
        }



    }




    private long getUserID(Connection con) {
        long returnLong = 0;
        try {
            String customerFName = input.nextLine();
            String selectCustomerIDquery = "select c.ID from CUSTOMER c where c.FIRST_NAME = ?";
            Statement statement = con.prepareStatement(selectCustomerIDquery);
            ((PreparedStatement) statement).setString(1, customerFName);
            ResultSet rs = ((PreparedStatement) statement).executeQuery();
            rs.next();
            returnLong = rs.getInt("ID");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnLong;


    }
}
