package oodj_project.core.ui.utils;

import java.awt.Image;

import javax.swing.ImageIcon;

public class IconManager {

    private IconManager() {}

    public static ImageIcon getIcon(String imageResourcePath, int width, int height) {
        return new ImageIcon(
            new ImageIcon(IconManager.class.getResource(imageResourcePath))
                .getImage()
                .getScaledInstance(width, height, Image.SCALE_SMOOTH)
        );
    }
}
