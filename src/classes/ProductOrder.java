package classes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProductOrder implements Serializable {
    public double totalPrice;
    public double weight;
    private List<SupplyHistory> supplyHistoryList = new ArrayList<>();

    public ProductOrder(double totalPrice, double weight) {
        setTotalPrice(totalPrice);
        setWeight(weight);
    }
    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        if (totalPrice < 0) {
            throw new IllegalArgumentException("Total price cannot be negative");
        }
        this.totalPrice = totalPrice;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        if (weight == null) {
            throw new IllegalArgumentException("Weight cannot be empty");
        }
        if (weight <= 0) {
            throw new IllegalArgumentException("Weight must be positive");
        }
        this.weight = weight;
    }

    public void addSupplyHistory(SupplyHistory supplyHistory) {
        if (supplyHistory == null) return;
        supplyHistoryList.add(supplyHistory);
    }

    public List<SupplyHistory> getSupplyHistoryList() {
        return new ArrayList<>(supplyHistoryList);
    }
}

