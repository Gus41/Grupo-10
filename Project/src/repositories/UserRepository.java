package repositories;
import models.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {

    private static final String FILE_NAME = "users.json";
    private List<User> users;

    public UserRepository() {
        this.users = loadUsers();
    }

    public void saveUsers() {
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
            //print error?
            System.out.println(e.toString());
        }
    }

    private List<User> loadUsers() {
        List<User> userList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            StringBuilder jsonContent = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                jsonContent.append(line);
            }

            String content = jsonContent.toString();
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
        } catch (IOException e) {
            System.out.println("IO error");
        }
        return userList;
    }

    public void addUser(User user) {
        users.add(user);
        saveUsers();
        //add a function to add only users to file?
    }


}
