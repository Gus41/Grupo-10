package models;
import java.time.LocalDate;

public class Expense {
    private int id;
    private Device device;
    private double consumptionKWh;
    private LocalDate date;

    public Expense(int id, Device device, double consumptionKWh, LocalDate date) {
        this.id = id;
        this.device = device;
        this.consumptionKWh = consumptionKWh;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public double getConsumptionKWh() {
        return consumptionKWh;
    }

    public void setConsumptionKWh(double consumptionKWh) {
        this.consumptionKWh = consumptionKWh;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
