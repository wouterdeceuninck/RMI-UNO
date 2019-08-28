package rmi.unogame.client.gameplay.opponent;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import rmi.unogame.client.gameplay.PlayerInfo;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class OpponentUI {

    @FXML
    private HBox opponent1Box, opponent2Box, opponent3Box;

    @FXML
    private TextField opponent1, opponent2, opponent3;

    private Image backImage;
    private final String path;

    public void initialize() {
        opponent1 = new TextField();
        opponent2 = new TextField();
        opponent3 = new TextField();
    }

    public OpponentUI(int theme, String path) {
        this.path = path;
        try {
            backImage = new Image(new FileInputStream(path + theme + "/UNO-Back.png"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    protected void setOpponentCards(PlayerInfo playerInfo) {
        if (playerInfo.getId() == 1) {
            updateHBox(playerInfo, opponent3Box);
        }
        if (playerInfo.getId() == 2) {
            updateHBox(playerInfo, opponent1Box);
        }
        if (playerInfo.getId() == 3) {
            updateHBox(playerInfo, opponent2Box);
        }
    }

    private void updateHBox(PlayerInfo playerInfo, HBox opponent2Box) {
        opponent2Box.getChildren().clear();
        opponent2Box.getChildren().addAll(Optional.ofNullable(getImageView(playerInfo)).orElseThrow(() -> new RuntimeException("There were no Cards found!")));
    }

    private List<ImageView> getImageView(PlayerInfo playerInfo) {
        return IntStream.range(0, playerInfo.getAmountOfCards())
                .mapToObj(count -> createImageView())
                .collect(Collectors.toList());
    }

    private ImageView createImageView() {
        ImageView imageView1;
        imageView1 = new ImageView(backImage);
        imageView1.setFitHeight(OpponentGUISide.NORD.getFitHeightSize());
        imageView1.setPreserveRatio(true);
        imageView1.setRotate(OpponentGUISide.NORD.getRotationDegrees());
        return imageView1;
    }

    protected void setOpponentsNames(PlayerInfo playerInfo) {
        if (playerInfo.getId() == 1) {
            opponent3.setText(playerInfo.getUsername());
        }
        if (playerInfo.getId() == 2) {
            opponent1.setText(playerInfo.getUsername());
        }
        if (playerInfo.getId() == 3) {
            opponent2.setText(playerInfo.getUsername());
        }
    }

}
