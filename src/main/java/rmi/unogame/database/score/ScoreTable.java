package rmi.unogame.database.score;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.fasterxml.jackson.databind.type.MapLikeType;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.io.*;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ScoreTable implements Serializable {
    Map<String, Integer> scoreboard;
    private final ObjectMapper objectMapper;
    private String filePath = "src/main/resources/tables/scoreTable.json";

    public ScoreTable(ObjectMapper objectMapper, int databasePortnumber) {
        this.filePath = "src/main/resources/tables/scoreTable" + databasePortnumber + ".json";
        this.objectMapper = objectMapper;
        scoreboard = readFromFile(objectMapper);
    }

    public void updateScore(String username, int delta) {
        scoreboard.merge(username, delta, Integer::sum);
        writeToFile();
    }

    public Map<String, Integer> getFirstLines(int amountOfLines) {
        return scoreboard.entrySet().parallelStream()
                .sorted(Comparator.comparingInt(Map.Entry::getValue))
                .limit(amountOfLines)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private Map<String, Integer> readFromFile(ObjectMapper objectMapper) {
        MapLikeType type = TypeFactory.defaultInstance().constructMapLikeType(HashMap.class, String.class, Integer.class);
        try {
            return objectMapper.readValue(getResource(), type);
        } catch (MismatchedInputException mismatch) {
            return new HashMap<>();
        } catch (FileNotFoundException e) {
            createFile();
            return new HashMap<>();
        } catch (IOException e) {
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

    private File getResource() {
        try {
            return new File(filePath);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Could not get the scoreTable file");
        }
    }

    private void writeToFile() {
        try {
            objectMapper.writeValue(getResource(), scoreboard);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getScoreOnUsername(String username) {
        return scoreboard.get(username);
    }
}
