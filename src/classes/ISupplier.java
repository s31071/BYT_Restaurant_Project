package classes;

public interface ISupplier {
    String getCompanyName();
    void setCompanyName(String companyName);

    Category getCategory();
    void setCategory(Category category);

    double getDeliveryCost();
    void setDeliveryCost(double deliveryCost);
}
