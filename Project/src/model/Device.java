package model; 

public class Device {
    private int id; // Consider generating the ID internally or via a database
    private String name;
    private double power; // Watts
    private double timeUse; // Hours per day, for example
    private Category category; 

    // Constructors
    public Device() {
    }

    public Device(String name, double power, double timeUse, Category category) {
        this.name = name;
        this.power = power;
        this.timeUse = timeUse;
        this.category = category;
    }

    public Device(String name, double power, double timeUse) {
        this.name = name;
        this.power = power;
        this.timeUse = timeUse;
    }

    public Device(Category category) {
        this.category = category;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    // Optional: a method to get the display name for the category from the device itself
    public String getCategoryDisplayName() {
        return (category != null) ? category.getDescription() : "N/A";
    }

    public double getConsumptionByDay(){
        return this.power * this.timeUse;
    }

    @Override
    public String toString() {
        return "Device{" +
               "name='" + name + '\'' +
               ", power=" + power +
               ", timeOfUse=" + timeUse +
               ", category=" + (category != null ? category.getDescription() : "null") +
               '}';
    }
}