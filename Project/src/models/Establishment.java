package models;

public class Establishment {
    private int id;
    private String name;
    private Device[] devices;
    private String[] users;

    public Establishment(int id, String name, Device[] devices) {
        this.id = id;
        this.name = name;
        this.devices = devices;
    }

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

    public Device[] getDevices() {
        return devices;
    }

    public void setDevices(Device[] devices) {
        this.devices = devices;
    }


    public void addDevice(Device device) {
        int length = devices.length;
        Device[] newDevices = new Device[length + 1];
        System.arraycopy(devices, 0, newDevices, 0, length);
        newDevices[length] = device;
        this.devices = newDevices;
    }

    @Override
    public String toString() {
        return "Establishment{id=" + id + ", name='" + name + "', devices=" + devices.length + "}";
    }
}
