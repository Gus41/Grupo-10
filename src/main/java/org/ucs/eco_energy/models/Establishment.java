package org.ucs.eco_energy.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Establishment {
    private int id;
    private String name;
    private List<Device> devices;
    private List<User> users;

    public Establishment(int id, String name, List<Device> devices, List<User> users) {
        this.id = id;
        this.name = name;
        this.devices = (devices != null) ? new ArrayList<>(devices) : new ArrayList<>();
        this.users = (users != null) ? new ArrayList<>(users) : new ArrayList<>();
    }

    public Establishment() {
        this.devices = new ArrayList<>();
        this.users = new ArrayList<>();
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

    public List<Device> getDevices() {
        if (devices == null) {
            devices = new ArrayList<>();
        }
        return Collections.unmodifiableList(devices);
    }

    public void setDevices(List<Device> devices) {
        this.devices = (devices != null) ? new ArrayList<>(devices) : new ArrayList<>();
    }

    public List<User> getUsers() {
        if (users == null) {
            users = new ArrayList<>();
        }
        return Collections.unmodifiableList(users);
    }

    public void setUsers(List<User> users) {
        this.users = (users != null) ? new ArrayList<>(users) : new ArrayList<>();
    }

    public void addDevice(Device device) {
        if (device == null) throw new IllegalArgumentException("Cannot add a null device.");
        if (this.devices == null) this.devices = new ArrayList<>();
        this.devices.add(device);
    }

    public boolean removeDevice(Device device) {
        return device != null && this.devices != null && this.devices.remove(device);
    }

    public void addUser(User user) {
        if (user == null) throw new IllegalArgumentException("Cannot add a null user.");
        if (this.users == null) this.users = new ArrayList<>();
        if (!this.users.contains(user)) {
            this.users.add(user);
        } else {
            System.out.println("User " + user.getUsername() + " is already associated with this establishment.");
        }
    }

    public boolean removeUser(User user) {
        return user != null && this.users != null && this.users.remove(user);
    }

    @Override
    public String toString() {
        return "Establishment{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", devices=" + devices +
                ", users=" + users +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Establishment)) return false;
        Establishment that = (Establishment) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
