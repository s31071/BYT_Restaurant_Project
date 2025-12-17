package classes;

public interface ICook {
    void changeToWaiter(WorkwearSize workwearSize, double maximumTables) throws Exception;
    void changeToDeliveryDriver(String carModel, String registrationNumber, boolean bonusApply) throws Exception;

    double getYearsOfExperience();
    void setYearsOfExperience(double yearsOfExperience);

    String getTitle();
    void setTitle(String title);

    String getSpecialization();
    void setSpecialization(String specialization);

    double calculateSalary();
}

