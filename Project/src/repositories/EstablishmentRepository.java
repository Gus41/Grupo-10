package repositories;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import models.Category;
import models.Device;
import models.Establishment;

public class EstablishmentRepository {

    private static final String FILE_NAME = "../src/db/establishments.json";
    private List<Establishment> establishments;

    public EstablishmentRepository() {
        this.establishments = loadEstablishments();
    }

    public void saveEstablishments() {
        System.out.println("Saving establishments");
        try (Writer writer = new FileWriter(FILE_NAME)) {
            writer.write("[\n");
            for (int i = 0; i < establishments.size(); i++) {
                Establishment establishment = establishments.get(i);
                writer.write("  {\n");
                writer.write("    \"id\": " + establishment.getId() + ",\n");
                writer.write("    \"name\": \"" + establishment.getName() + "\",\n");
                writer.write("    \"devices\": [\n");

                Device[] devices = establishment.getDevices();
                for (int j = 0; j < devices.length; j++) {
                    Device device = devices[j];
                    writer.write("      {\n");
                    writer.write("        \"id\": " + device.getId() + ",\n");
                    writer.write("        \"name\": \"" + device.getName() + "\",\n");
                    writer.write("        \"category\": \"" + device.getCategory().toString() + "\"\n");
                    writer.write("      }");
                    if (j < devices.length - 1) {
                        writer.write(",");
                    }
                    writer.write("\n");
                }

                writer.write("    ],\n");
                writer.write("    \"users\": [");
                String[] users = establishment.getUsers();
                if (users != null && users.length > 0) {
                    for (int j = 0; j < users.length; j++) {
                        writer.write("\"" + users[j] + "\"");
                        if (j < users.length - 1) {
                            writer.write(", ");
                        }
                    }
                }
                writer.write("]\n");

                writer.write("  }");
                if (i < establishments.size() - 1) {
                    writer.write(",");
                }
                writer.write("\n");
            }
            writer.write("]");
        } catch (IOException e) {
            System.out.println(e.toString());
        }
        System.out.println("Establishments saved in file");
    }

    public List<Establishment> loadEstablishments() {
        List<Establishment> establishmentList = new ArrayList<>();

        try {
            File file = new File(FILE_NAME);
            if (!file.exists()) {
                file.createNewFile();
                try (FileWriter writer = new FileWriter(file)) {
                    writer.write("[]");
                }
            }

            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                StringBuilder jsonContent = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    jsonContent.append(line);
                }

                String content = jsonContent.toString().trim();
                if (content.startsWith("[") && content.endsWith("]")) {
                    String jsonArray = content.substring(1, content.length() - 1).trim();

                    if (!jsonArray.isEmpty()) {
                        String[] establishmentStrings = jsonArray.split("},\\s*\\{");

                        for (String establishmentString : establishmentStrings) {
                            establishmentString = establishmentString.replace("{", "").replace("}", "").trim();

                            String[] fields = establishmentString.split(",\\s*");

                            int id = -1;
                            String name = null;
                            List<Device> devices = new ArrayList<>();
                            List<String> users = new ArrayList<>();

                            for (String field : fields) {
                                if (field.startsWith("\"id\":")) {
                                    id = Integer.parseInt(field.split(":")[1].trim());
                                } else if (field.startsWith("\"name\":")) {
                                    name = field.split(":")[1].replace("\"", "").trim();
                                } else if (field.startsWith("\"devices\":")) {
                                    String deviceArray = field.substring(field.indexOf("[") + 1, field.lastIndexOf("]")).trim();
                                    if (!deviceArray.isEmpty()) {
                                        String[] deviceStrings = deviceArray.split("},\\s*\\{");
                                        for (String deviceString : deviceStrings) {
                                            deviceString = deviceString.replace("{", "").replace("}", "").trim();

                                            String[] deviceFields = deviceString.split(",\\s*");

                                            int deviceId = -1;
                                            String deviceName = null;
                                            String categoryStr = null;

                                            for (String deviceField : deviceFields) {
                                                if (deviceField.startsWith("\"id\":")) {
                                                    deviceId = Integer.parseInt(deviceField.split(":")[1].trim());
                                                } else if (deviceField.startsWith("\"name\":")) {
                                                    deviceName = deviceField.split(":")[1].replace("\"", "").trim();
                                                } else if (deviceField.startsWith("\"category\":")) {
                                                    categoryStr = deviceField.split(":")[1].replace("\"", "").trim();
                                                }
                                            }

                                            if (deviceId != -1 && deviceName != null && categoryStr != null) {
                                                devices.add(new Device(deviceId, deviceName, new Category(0,categoryStr)));
                                            }
                                        }
                                    }
                                } else if (field.startsWith("\"users\":")) {
                                    //TODO: if an establishment has more than an user, will return a error here
                                    String usersArr = field.substring(field.indexOf("[") + 1, field.lastIndexOf("]")).trim();

                                    usersArr = usersArr.replace("\"", "").trim();

                                    System.out.println(usersArr);
                                    users.add(usersArr);

                                }
                            }

                            if (id != -1 && name != null && !users.isEmpty()) {
                                Establishment establishment = new Establishment(id, name, devices.toArray(new Device[0]));
                                establishment.setUsers(users.toArray(new String[0]));
                                establishmentList.add(establishment);
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo:");
            e.printStackTrace();
        }

        System.out.println(establishmentList);
        return establishmentList;
    }

    public void addEstablishment(Establishment establishment) {
        establishments.add(establishment);
        System.out.println("Adding establishment");
        saveEstablishments();
    }

    public List<Establishment> getEstablishments() {
        return this.establishments;
    }

    public void addDeviceToEstablishment(int establishmentId, Device device) {
        for (Establishment establishment : establishments) {
            if (establishment.getId() == establishmentId) {
                establishment.addDevice(device);
                saveEstablishments();
                break;
            }
        }
    }
    public Establishment getEstablishmentByUser(String userName) {
        System.out.println(userName);
        for (Establishment establishment : establishments) {
            if (establishment.getUsers() != null) {
                for (String user: establishment.getUsers()){
                    System.out.println(user);
                    if(userName.equals(user)){
                        return establishment;
                    }
                }
            }else{
                System.out.println("sem users");
            }
        }
        return null;
    }

}
