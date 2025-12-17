package classes;

public interface IDeliveryDriver {
    void changeToCook(double yearsOfExperience, String title, String specialization) throws Exception;
    void changeToWaiter(WorkwearSize workwearSize, double maximumTables) throws Exception;

    String getCarModel();
    void setCarModel(String carModel);

    String getRegistrationNumber();
    void setRegistrationNumber(String registrationNumber);

    boolean isBonusApply();
    void setBonusApply(boolean bonusApply);

    double getKmsInDay();
    void setKmsInDay(double kmsInDay);

    double getKmsInMonth();
    void setKmsInMonth(double kmsInMonth);

    double calculateSalary();
}

