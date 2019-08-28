package rmi.unogame.client.gameplay.util;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class ImageViewFactory {

        public static ImageView buildForCardName(String pathToFile) throws FileNotFoundException {
            ImageView imageView = new ImageView(
                    new Image(new FileInputStream(pathToFile), 0, 250, true, true));
            imageView.setFocusTraversable(true);
            imageView.setFitHeight(125);
            imageView.setOpacity(0.5);
            imageView.setPreserveRatio(true);
            return imageView;
        }
    }