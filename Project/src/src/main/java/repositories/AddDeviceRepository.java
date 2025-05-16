package repositories;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader; // For more robust empty file handling

import model.Device;

import com.google.gson.JsonSyntaxException; // For Gson parsing errors

import java.io.FileReader;
import java.io.FileWriter;
import java.io.Writer;
import java.io.IOException;
import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class AddDeviceRepository {
    private static final String FILE_NAME = "../src/db/devices.json"; // Ensure this path is correct relative to your execution context
    private final Gson gson;

    public AddDeviceRepository() {
        // Initialize Gson, setPrettyPrinting for readable JSON output
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    public void saveDevice(Device device) {
        List<Device> deviceList;
        File dbFile = new File(FILE_NAME);

        try {
            // 1. Read existing content and parse or initialize List<Device>
            if (dbFile.exists() && dbFile.length() > 0) {
                try (FileReader fileReader = new FileReader(dbFile);
                     JsonReader jsonReader = new JsonReader(fileReader)) { // Use JsonReader for fine-grained control

                    // Check if the file is effectively empty (e.g. just whitespace, or literally "null")
                    // JsonReader.peek() helps determine if there's actual JSON content
                    if (jsonReader.peek() == com.google.gson.stream.JsonToken.NULL ||
                        jsonReader.peek() == com.google.gson.stream.JsonToken.END_DOCUMENT) {
                        deviceList = new ArrayList<>();
                    } else {
                        Type listType = new TypeToken<ArrayList<Device>>() {}.getType();
                        deviceList = gson.fromJson(jsonReader, listType);
                        // If the file contained "null" as its content, gson.fromJson might return null.
                        if (deviceList == null) {
                            deviceList = new ArrayList<>();
                        }
                    }
                } catch (JsonSyntaxException e) {
                    // This can happen if the file content is not valid JSON array for devices
                    System.err.println("JsonSyntaxException: Malformed JSON in " + FILE_NAME + ". Initializing a new list. Error: " + e.getMessage());
                    deviceList = new ArrayList<>();
                } catch (IOException e) {
                    System.err.println("IOException while reading " + FILE_NAME + ". Initializing a new list. Error: " + e.getMessage());
                    e.printStackTrace();
                    deviceList = new ArrayList<>(); // Fallback to new list on read error
                }
            } else {
                deviceList = new ArrayList<>();
            }

            // 2. Add the new device object to the list
            // Gson handles the conversion of the Device object to JSON internally
            deviceList.add(device);

            // 3. Write the updated list back to the file
            try (Writer writer = new FileWriter(FILE_NAME)) { // Overwrites the file
                gson.toJson(deviceList, writer);
                System.out.println("Device saved successfully to " + FILE_NAME);
            } catch (IOException e) {
                System.err.println("IOException occurred while writing to " + FILE_NAME + ": " + e.getMessage());
                e.printStackTrace();
            }

        } catch (Exception e) { // Catch-all for any other unexpected errors
            System.err.println("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Optional: A method to retrieve all devices (demonstrates deserialization)
    public List<Device> getAllDevices() {
        List<Device> deviceList = new ArrayList<>();
        File dbFile = new File(FILE_NAME);

        if (dbFile.exists() && dbFile.length() > 0) {
            try (FileReader fileReader = new FileReader(dbFile);
                 JsonReader jsonReader = new JsonReader(fileReader)) {

                if (jsonReader.peek() == com.google.gson.stream.JsonToken.NULL ||
                    jsonReader.peek() == com.google.gson.stream.JsonToken.END_DOCUMENT) {
                    return deviceList; // Empty list
                }

                Type listType = new TypeToken<ArrayList<Device>>() {}.getType();
                deviceList = gson.fromJson(jsonReader, listType);
                if (deviceList == null) { // If file contained "null"
                    deviceList = new ArrayList<>();
                }
                return deviceList;

            } catch (JsonSyntaxException e) {
                System.err.println("JsonSyntaxException: Malformed JSON in " + FILE_NAME + " during getAllDevices. Returning empty list. Error: " + e.getMessage());
            } catch (IOException e) {
                System.err.println("IOException while reading " + FILE_NAME + " during getAllDevices: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return deviceList; // Return empty list if file doesn't exist, is empty, or on error
    }
}