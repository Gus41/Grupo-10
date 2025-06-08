package org.ucs.eco_energy.repositories;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.ucs.eco_energy.models.User;

public class UserRepository {

    private static final String FILE_NAME = "./src/main/java/org/ucs/eco_energy/db/users.json";
    private List<User> users;

    public UserRepository() {
        this.users = loadUsers();
    }

    public void saveUsers() {
        System.out.println("Saving users");
        try (Writer writer = new FileWriter(FILE_NAME)) {
            writer.write("[\n");
            for (int i = 0; i < users.size(); i++) {
                User user = users.get(i);
                writer.write("  {\n");
                writer.write("    \"username\": \"" + user.getUsername() + "\",\n");
                writer.write("    \"password\": \"" + user.getPassword() + "\",\n");
                writer.write("    \"contact\": \"" + user.getContact() + "\"\n");
                writer.write("  }");
                if (i < users.size() - 1) {
                    writer.write(",");
                }
                writer.write("\n");
            }
            writer.write("]");
        } catch (IOException e) {
            System.out.println(e.toString());
        }
        System.out.println("Users saved in file");
    }
    public List<User> loadUsers() {
        List<User> userList = new ArrayList<>();

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
                        String[] userStrings = jsonArray.split("},\\s*\\{");

                        for (String userString : userStrings) {
                            userString = userString.replace("{", "").replace("}", "").trim();

                            String[] userFields = userString.split(",\\s*");

                            String username = null, password = null, contact = null;

                            for (String field : userFields) {
                                if (field.startsWith("\"username\":")) {
                                    username = field.split(":")[1].replace("\"", "").trim();
                                } else if (field.startsWith("\"password\":")) {
                                    password = field.split(":")[1].replace("\"", "").trim();
                                } else if (field.startsWith("\"contact\":")) {
                                    contact = field.split(":")[1].replace("\"", "").trim();
                                }
                            }

                            if (username != null && password != null && contact != null) {
                                userList.add(new User(username, password, contact));
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo:");
            e.printStackTrace();
        }

        return userList;
    }


    public void addUser(User user) {
        users.add(user);
        System.out.println("adding user");
        saveUsers();
    }

    public List<User> getUsers(){
        return this.users;
    }


}
