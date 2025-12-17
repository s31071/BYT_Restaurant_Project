package classes;

public interface IWaiter {
    void changeToCook(double yearsOfExperience, String title, String specialization) throws Exception;
    void changeToDeliveryDriver(String carModel, String registrationNumber, boolean bonusApply) throws Exception;

    WorkwearSize getWorkwearSize();
    void setWorkwearSize(WorkwearSize workwearSize);

    double getMaximumTables();
    void setMaximumTables(double maximumTables);

    double calculateSalary();
}

