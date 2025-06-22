package org.ucs.eco_energy.repositories;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer; // For more robust empty file handling
import java.lang.reflect.Type; // For Gson parsing errors
import java.util.ArrayList;
import java.util.List;

import org.ucs.eco_energy.models.Device;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import org.ucs.eco_energy.models.Establishment;

public class AddDeviceRepository {
    private static final String FILE_NAME = "./src/main/java/org/ucs/eco_energy/db/devices.json"; // Ensure this path is correct relative to your execution context
    private final Gson gson;

    public AddDeviceRepository() {
        // Initialize Gson, setPrettyPrinting for readable JSON output
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }
    public void saveDevice(Device device, Establishment establishment) {
        List<Device> deviceList;
        File dbFile = new File(FILE_NAME);

        EstablishmentRepository establishmentRepository = new EstablishmentRepository();

        try {
            // Inicializa lista e variável para rastrear o maior ID
            deviceList = new ArrayList<>();
            int maxDeviceId = 0;

            if (dbFile.exists() && dbFile.length() > 0) {
                try (FileReader fileReader = new FileReader(dbFile);
                     JsonReader jsonReader = new JsonReader(fileReader)) {

                    if (jsonReader.peek() != com.google.gson.stream.JsonToken.NULL &&
                            jsonReader.peek() != com.google.gson.stream.JsonToken.END_DOCUMENT) {

                        Type listType = new TypeToken<ArrayList<Device>>() {}.getType();
                        deviceList = gson.fromJson(jsonReader, listType);
                        if (deviceList == null) {
                            deviceList = new ArrayList<>();
                        }

                        // Encontra o maior ID existente
                        for (Device d : deviceList) {
                            if (d.getId() > maxDeviceId) {
                                maxDeviceId = d.getId();
                            }
                        }
                    }

                } catch (JsonSyntaxException e) {
                    System.err.println("JsonSyntaxException: Malformed JSON in " + FILE_NAME + ". Initializing a new list. Error: " + e.getMessage());
                    deviceList = new ArrayList<>();
                } catch (IOException e) {
                    System.err.println("IOException while reading " + FILE_NAME + ". Initializing a new list. Error: " + e.getMessage());
                    e.printStackTrace();
                    deviceList = new ArrayList<>();
                }
            }

            // Define ID único para o novo device
            int newDeviceId = maxDeviceId + 1;
            device.setId(newDeviceId);
            System.out.println("Assigned ID " + newDeviceId + " to new device: " + device.getName());

            deviceList.add(device);

            try (Writer writer = new FileWriter(FILE_NAME)) {
                gson.toJson(deviceList, writer);
                System.out.println("Device saved successfully to " + FILE_NAME);
            } catch (IOException e) {
                System.err.println("IOException occurred while writing to " + FILE_NAME + ": " + e.getMessage());
                e.printStackTrace();
            }

            try {
                establishmentRepository.addDeviceToEstablishment(establishment.getId(), device);
                System.out.println("Device associated to establishment " + establishment.getId() + " successfully.");
            } catch (Exception e) {
                System.err.println("Failed to associate device to establishment: " + e.getMessage());
                e.printStackTrace();
            }

        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }



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