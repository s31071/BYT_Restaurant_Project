package classes;

import java.io.*;

public class ExtentToolBox {

    //tworzymy dowolny plik gdzie zapisywac się będą nasze info
    private static final String allExtentFile = "extents.bin";

    public static void saveAllExtents() throws IOException {

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(allExtentFile))) {

            // zapisujemy wszystkie extenty do bazy danych, która nie jest bazą danych
            Address.writeExtent(out);
            Cook.writeExtent(out);
            Customer.writeExtent(out);
            DeliveryDriver.writeExtent(out);
            Dish.writeExtent(out);
            FullTime.writeExtent(out);
            Invoice.writeExtent(out);
            Menu.writeExtent(out);
            Order.writeExtent(out);
            PartTime.writeExtent(out);
            Product.writeExtent(out);
            ProductOrder.writeExtent(out);
            Receipt.writeExtent(out);
            Reservation.writeExtent(out);
            Shift.writeExtent(out);
            Supplier.writeExtent(out);
            SupplyHistory.writeExtent(out);
            Table.writeExtent(out);
            Waiter.writeExtent(out);

            out.flush();
        }
    }


    public static void loadAllExtents() throws IOException, ClassNotFoundException {

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(allExtentFile))) {

//czytamy wszystkie extenty
            Address.readExtent(in);
            Cook.readExtent(in);
            Customer.readExtent(in);
            DeliveryDriver.readExtent(in);
            Dish.readExtent(in);
            FullTime.readExtent(in);
            Invoice.readExtent(in);
            Menu.readExtent(in);
            Order.readExtent(in);
            PartTime.readExtent(in);
            Product.readExtent(in);
            ProductOrder.readExtent(in);
            Receipt.readExtent(in);
            Reservation.readExtent(in);
            Shift.readExtent(in);
            Supplier.readExtent(in);
            SupplyHistory.readExtent(in);
            Table.readExtent(in);
            Waiter.readExtent(in);
        }
    }
}