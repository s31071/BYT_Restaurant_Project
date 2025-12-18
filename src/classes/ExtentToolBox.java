package classes;

import java.io.*;
import java.beans.XMLEncoder;
import java.beans.XMLDecoder;
import java.time.LocalDate;

public class ExtentToolBox {

    private static final String allExtentFile = "extents.xml";

    public static void saveAllExtents() throws IOException {
        try (XMLEncoder encoder = new XMLEncoder(
                new BufferedOutputStream(new FileOutputStream(allExtentFile)))) {

            encoder.setPersistenceDelegate(
                    LocalDate.class,
                    new java.beans.PersistenceDelegate() {
                        @Override
                        protected java.beans.Expression instantiate(Object obj, java.beans.Encoder enc) {
                            LocalDate date = (LocalDate) obj;
                            return new java.beans.Expression(
                                    date,
                                    LocalDate.class,
                                    "of",
                                    new Object[]{
                                            date.getYear(),
                                            date.getMonthValue(),
                                            date.getDayOfMonth()
                                    }
                            );
                        }
                    }
            );

            Person.writeExtent(encoder);
            Cook.writeExtent(encoder);
            Customer.writeExtent(encoder);
            DeliveryDriver.writeExtent(encoder);
            Dish.writeExtent(encoder);
            DishOrder.writeExtent(encoder);
            Invoice.writeExtent(encoder);
            Menu.writeExtent(encoder);
            Order.writeExtent(encoder);
            Product.writeExtent(encoder);
            ProductOrder.writeExtent(encoder);
            Receipt.writeExtent(encoder);
            Reservation.writeExtent(encoder);
            Shift.writeExtent(encoder);
            Supplier.writeExtent(encoder);
            SupplyHistory.writeExtent(encoder);
            Table.writeExtent(encoder);
            Waiter.writeExtent(encoder);

            encoder.flush();
        }
    }

    public static void loadAllExtents() throws IOException, ClassNotFoundException {
        try (XMLDecoder decoder = new XMLDecoder(
                new BufferedInputStream(new FileInputStream(allExtentFile)))) {

            Person.readExtent(decoder);
            Cook.readExtent(decoder);
            Customer.readExtent(decoder);
            DeliveryDriver.readExtent(decoder);
            Dish.readExtent(decoder);
            DishOrder.readExtent(decoder);
            Invoice.readExtent(decoder);
            Menu.readExtent(decoder);
            Order.readExtent(decoder);
            Product.readExtent(decoder);
            ProductOrder.readExtent(decoder);
            Receipt.readExtent(decoder);
            Reservation.readExtent(decoder);
            Shift.readExtent(decoder);
            Supplier.readExtent(decoder);
            SupplyHistory.readExtent(decoder);
            Table.readExtent(decoder);
            Waiter.readExtent(decoder);
        }
    }
}