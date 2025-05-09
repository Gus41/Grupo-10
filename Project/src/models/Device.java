package models;

public class Device {
    private int id;
    private String name;
    private String category;
    private double power;
    private double timeUse;
    //TODO: Add time of use

    public Device(int id, String name, String category, double power, double timeUse) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.power = power;
        this.timeUse = timeUse;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

     public double getPower() {
        return power;
    }

    public void setPower(double power) {
        this.power = power;
    }

     public double getTimeUse() {
        return timeUse;
    }

    public void setTimeUse(double timeUse) {
        this.timeUse = timeUse;
    }

    public double getConsumptionByDay(){
        return this.power * this.timeUse;
    }
        
}

