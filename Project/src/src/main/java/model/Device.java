package model;

public class Device {
    private int id;
    private String name;
    private Category category;
    private double power;
    private double timeUse;

    public Device(int id, String name, Category category, double power, double timeUse) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.power = power;
        this.timeUse = timeUse;
    }

    public Device(String name, double power, double timeUse, Category category) {
        this.name = name;
        this.power = power;
        this.timeUse = timeUse;
        this.category = category;
    }

    public Device(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
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

    @Override
     public String toString() {
        return "Device{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", category=" + category +
               ",power=" + power +
               ",timeuse=" + timeUse +
               '}';
        
}

}

