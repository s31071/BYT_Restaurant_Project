package classes;

import java.time.LocalDate;

public class DeliveryDriver extends Employee{
    private String carModel;
    private String registrationNumber;
    private static final double kmBonus = 1;
    private boolean bonusApply;
    private double kmsInDay;
    private double kmsInMonth;

    public DeliveryDriver(String name, String surname, String phoneNumber, String address, String email, LocalDate employmentDate, Contract contract, String carModel, String registrationNumber, boolean bonusApply) {
        super(name, surname, phoneNumber, address, email, employmentDate, contract);
        this.carModel = carModel;
        this.registrationNumber = registrationNumber;
        this.bonusApply = bonusApply;
        kmsInDay = 0;
        kmsInMonth = 0;
    }

    @Override
    double calculateSalary(Contract contract, LocalDate employmentDate) {
        return getBaseSalary()*168+kmsInMonth*(bonusApply ? kmBonus : 0);
    }

    private void confirmDelivery(){}

    private void saveDailyKms(){
        kmsInMonth += kmsInDay;
        kmsInDay = 0;
    }

    private void reportIssue(){}
}
