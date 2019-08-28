package rmi.unogame.database.users;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import rmi.unogame.applicationServer.service.authentication.UserInfo;
import rmi.unogame.database.users.dto.DatabaseUserInfo;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class UserTable implements Serializable {

    private List<DatabaseUserInfo> users;
    private final ObjectMapper objectMapper;
    private String filePath;

    public UserTable(ObjectMapper objectMapper, int databasePortnumber) {
        this.filePath = "src/main/resources/tables/userTable" + databasePortnumber + ".json";
        this.objectMapper = objectMapper;
        users = readFromFile(objectMapper);
    }

    private List<DatabaseUserInfo> readFromFile(ObjectMapper objectMapper) {
        try {
            return Arrays.stream(objectMapper.readValue(getResource(), DatabaseUserInfo[].class))
                    .collect(Collectors.toList());
        } catch (FileNotFoundException e) {
            createFile();
            return new ArrayList<>();
        } catch (MismatchedInputException mismatch) {
            return new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("could not read userTable");
        }
    }

    private void createFile() {
        try {
            new FileOutputStream(filePath, false).close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void persistUser(UserInfo userInfo) {
        Optional<DatabaseUserInfo> selectedUser = findByName(userInfo.getUsername());

        if (selectedUser.isPresent()) {
            selectedUser.get().update(userInfo);
        } else {
            throw new RuntimeException("Player was not found!");
        }

        writeToFile();
    }

    public Optional<DatabaseUserInfo> findByName(String username) {
        return users.stream()
                    .filter(user -> user.getUsername().equals(username))
                    .findFirst();
    }

    private File getResource() {
        return new File(filePath);
    }

    public void createUser(DatabaseUserInfo databaseUserInfo) {
        users.add(databaseUserInfo);
        writeToFile();
    }

    private void writeToFile() {
        try {
            objectMapper.writeValue(getResource(), users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
