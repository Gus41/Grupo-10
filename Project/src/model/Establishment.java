package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Establishment {
    private int id; // Considere gerar o ID internamente ou via BD
    private String name;
    private List<Device> devices; 
    private List<User> users;  

    /**
     * @param id
     * @param name
     * @param devices
     */
    public Establishment(int id, String name, List<Device> devices,List<User> users ) {
        this.id = id;
        this.name = name;
        this.devices = (devices != null) ? new ArrayList<>(devices) : new ArrayList<>();
        this.users = new ArrayList<>();

    }

    public Establishment(){

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
    
        return Collections.unmodifiableList(devices);
    }

    public void setDevices(List<Device> devices) {
    
        this.devices = (devices != null) ? new ArrayList<>(devices) : new ArrayList<>();
    }

    public List<User> getUsers() {
        return Collections.unmodifiableList(users);
    }

    public void setUsers(List<User> users) {
        this.users = (users != null) ? new ArrayList<>(users) : new ArrayList<>();
    }

    /**
     * Adiciona um dispositivo ao estabelecimento.
     * @param device O dispositivo a ser adicionado.
     * @throws IllegalArgumentException se o dispositivo for nulo.
     */
    public void addDevice(Device device) {
        if (device == null) {
            throw new IllegalArgumentException("Cannot add a null device.");
        }
        this.devices.add(device);
    }

    /**
     * Remove um dispositivo do estabelecimento.
     * @param device O dispositivo a ser removido.
     * @return true se o dispositivo foi removido, false caso contrário.
     */
    public boolean removeDevice(Device device) {
        if (device == null) {
            return false;
        }
        return this.devices.remove(device); // Requer Device.equals/hashCode
    }

    // --- MÉTODO PARA ADICIONAR USUÁRIO ---
    /**
     * Adiciona um usuário ao estabelecimento.
     * Não adiciona o usuário se ele for nulo ou se já estiver associado (baseado no username).
     * @param user O usuário a ser adicionado.
     * @throws IllegalArgumentException se o usuário for nulo.
     */
    public void addUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("Cannot add a null user.");
        }
        // Verifica se o usuário já existe para evitar duplicados
        // Isso requer que User.equals() e User.hashCode() estejam implementados (baseado no username)
        if (!this.users.contains(user)) {
            this.users.add(user);
        } else {
            System.out.println("User " + user.getUsername() + " is already associated with this establishment.");
            // Ou você pode lançar uma exceção específica se preferir
            // throw new UserAlreadyAssociatedException("User " + user.getUsername() + " is already associated.");
        }
    }

    /**
     * Remove um usuário do estabelecimento.
     * @param user O usuário a ser removido.
     * @return true se o usuário foi removido, false caso contrário.
     */
    public boolean removeUser(User user) {
        if (user == null) {
            return false;
        }
        return this.users.remove(user); // Requer User.equals/hashCode
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
        if (o == null || getClass() != o.getClass()) return false;
        Establishment that = (Establishment) o;
        return id == that.id; // Supondo que 'id' é o identificador único
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}