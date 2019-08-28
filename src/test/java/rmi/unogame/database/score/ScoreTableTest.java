package rmi.unogame.database.score;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.util.Map;

public class ScoreTableTest {

    @Test
    public void createScoreBoard() {
        ScoreTable scoreTable = new ScoreTable(new ObjectMapper(), 1300);
    }

    @Test
    public void updateScore() {
        ScoreTable scoreTable = new ScoreTable(new ObjectMapper(), 1300);
        scoreTable.updateScore("username1", 3);
        scoreTable.updateScore("username2", 3);
        Map<String, Integer> firstLines = scoreTable.getFirstLines(2);

    }
}