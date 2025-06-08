package org.ucs.eco_energy.repositories;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import org.ucs.eco_energy.models.Category;
import org.ucs.eco_energy.models.Device;
import org.ucs.eco_energy.models.Establishment;
import org.ucs.eco_energy.models.User;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class EstablishmentRepository {

    
    private static final String FILE_NAME_STR = "establishments.json"; 
    private static final Path DATA_DIRECTORY = Paths.get(System.getProperty("user.home"), ".myappdata", "db"); // Ex: C:\Users\SeuNome\.myappdata\db
    private static final Path FILE_PATH = DATA_DIRECTORY.resolve(FILE_NAME_STR); // Caminho completo do arquivo

    private List<Establishment> establishments;
    private final Gson gson;
    private final AtomicInteger nextEstablishmentId;
    private final AtomicInteger nextDeviceId; 

    public EstablishmentRepository() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        this.gson = gsonBuilder.create();

        // Garante que o diretório de dados exista
        ensureDataDirectoryExists();

        this.establishments = loadEstablishments();

        // Inicializa o contador de ID de Establishment
        int maxEstablishmentId = 0;
        if (this.establishments != null) {
            for (Establishment est : this.establishments) {
                if (est.getId() > maxEstablishmentId) {
                    maxEstablishmentId = est.getId();
                }
            }
        }
        this.nextEstablishmentId = new AtomicInteger(maxEstablishmentId);
        System.out.println("Repository initialized. Next Establishment ID will start after: " + maxEstablishmentId);

        // Inicializa o contador de ID de Device
        int maxDeviceId = 0;
        if (this.establishments != null) {
            for (Establishment est : this.establishments) {
                if (est.getDevices() != null) {
                    for (Device dev : est.getDevices()) {
                        if (dev.getId() > maxDeviceId) {
                            maxDeviceId = dev.getId();
                        }
                    }
                }
            }
        }
        this.nextDeviceId = new AtomicInteger(maxDeviceId);
        System.out.println("Repository initialized. Next Device ID will start after: " + maxDeviceId);
    }

    private void ensureDataDirectoryExists() {
        if (!Files.exists(DATA_DIRECTORY)) {
            try {
                Files.createDirectories(DATA_DIRECTORY);
                System.out.println("Created data directory: " + DATA_DIRECTORY.toAbsolutePath());
            } catch (IOException e) {
                System.err.println("Could not create data directory: " + DATA_DIRECTORY.toAbsolutePath());
                // Considerar lançar uma RuntimeException aqui se o diretório for crítico
                e.printStackTrace();
            }
        }
    }

    public void saveEstablishments() {
        System.out.println("Saving establishments...");
        if (!Files.exists(DATA_DIRECTORY)) {
            ensureDataDirectoryExists(); // Tenta criar de novo se não existir
            if (!Files.exists(DATA_DIRECTORY)) { // Se ainda não existir, não podemos prosseguir
                 System.err.println("Data directory " + DATA_DIRECTORY.toAbsolutePath() + " does not exist and could not be created. Cannot save.");
                 return;
            }
        }

        try (Writer writer = Files.newBufferedWriter(FILE_PATH)) { // Usar Files.newBufferedWriter é mais moderno
            gson.toJson(establishments, writer);
        } catch (IOException e) {
            System.err.println("Error saving establishments to file: " + e.getMessage());
            e.printStackTrace();
        }
        System.out.println("Establishments saved in file: " + FILE_PATH.toAbsolutePath());
    }

    public List<Establishment> loadEstablishments() {
        System.out.println("Loading establishments from: " + FILE_PATH.toAbsolutePath());

        if (!Files.exists(FILE_PATH)) {
            System.out.println("File " + FILE_PATH.getFileName() + " not found. Initializing with an empty list.");
            return new ArrayList<>();
        }

        try (Reader reader = Files.newBufferedReader(FILE_PATH)) {
            Type establishmentListType = new TypeToken<ArrayList<Establishment>>() {}.getType();
            List<Establishment> loadedList = gson.fromJson(reader, establishmentListType);

            if (loadedList == null) {
                System.out.println("File was empty or invalid, initializing with an empty list.");
                return new ArrayList<>();
            }
            System.out.println("Loaded " + loadedList.size() + " establishments.");
            return loadedList;

        } catch (FileNotFoundException e) { // Teoricamente, Files.exists() já cobriria isso
            System.err.println("Establishments file not found: " + e.getMessage());
            return new ArrayList<>();
        } catch (IOException e) {
            System.err.println("Error reading establishments file: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        } catch (JsonSyntaxException e) {
            System.err.println("Error parsing JSON from establishments file: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void addEstablishment(Establishment establishment) {
        int newId = nextEstablishmentId.incrementAndGet();
        establishment.setId(newId);
        if (establishment.getDevices() != null) {
            for (Device device : establishment.getDevices()) {
                if (device.getId() <= 0) { // Assume-se que ID 0 ou negativo significa "sem ID atribuído"
                    int newDeviceId = nextDeviceId.incrementAndGet();
                    device.setId(newDeviceId);
                     System.out.println("Assigned new ID " + newDeviceId + " to device " + device.getName() + " in new establishment " + establishment.getName());
                }
            }
        }


        this.establishments.add(establishment);
        System.out.println("Adding establishment: " + establishment.getName() + " with ID: " + newId);
        saveEstablishments();
    }

    public List<Establishment> getEstablishments() {
    
        return Collections.unmodifiableList(new ArrayList<>(this.establishments));
    }

    public void addDeviceToEstablishment(int establishmentId, Device device) {
        Establishment targetEstablishment = null;
        for (Establishment establishment : establishments) {
            if (establishment.getId() == establishmentId) {
                targetEstablishment = establishment;
                break;
            }
        }

        if (targetEstablishment != null) {
            
            int newDeviceId = nextDeviceId.incrementAndGet();
            device.setId(newDeviceId); 
            targetEstablishment.addDevice(device); 
            System.out.println("Added device " + device.getName() + " (ID: " + newDeviceId + ") to establishment " + targetEstablishment.getName());
            saveEstablishments();
        } else {
            System.out.println("Establishment with ID " + establishmentId + " not found. Device " + device.getName() + " not added.");
        }
    }

    public Establishment getEstablishmentByUser(String userName) {
        for (Establishment establishment : establishments) {
            if (establishment.getUsers() != null) {
                
                for (User users : establishment.getUsers()) { // Se getUsers() retornar String[] ou List<String>
                    if (userName.equals(users.getUsername())) {
                        return establishment;
                    }
                }
               
            }
        }
        return null;
    }

    
}