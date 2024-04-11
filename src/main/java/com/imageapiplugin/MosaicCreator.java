package com.imageapiplugin;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class MosaicCreator {
    private List<JImage> dependentImages;

    public MosaicCreator(List<JImage> dependentImages) {
        this.dependentImages = dependentImages;
    }

    public JImage findBestMatchingImage(int[] pixelColor) {
        JImage bestMatch = null;
        double minDistance = Double.MAX_VALUE;

        for (JImage img : dependentImages) {
            double distance = JImage.colorDistance(pixelColor, img.getAverageColor());
            if (distance < minDistance) {
                minDistance = distance;
                bestMatch = img;
            }
        }
        return bestMatch;
    }

    public BufferedImage createMosaic(JImage hostImage) {
        int tileWidth = 16;
        int tileHeight = 16;

        BufferedImage mosaic = new BufferedImage(hostImage.getImage().getWidth() * tileWidth,
                hostImage.getImage().getHeight() * tileHeight,
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = mosaic.createGraphics();

        for (int y = 0; y < hostImage.getImage().getHeight(); y++) {
            for (int x = 0; x < hostImage.getImage().getWidth(); x++) {
                int pixel = hostImage.getImage().getRGB(x, y);
                int alpha = (pixel >> 24) & 0xff;
                int red = (pixel >> 16) & 0xff;
                int green = (pixel >> 8) & 0xff;
                int blue = (pixel) & 0xff;

                JImage bestMatch = findBestMatchingImage(new int[]{red, green, blue, alpha});
                BufferedImage tileImage = bestMatch.getImage();
                g2d.drawImage(tileImage, x * tileWidth, y * tileHeight, tileWidth, tileHeight, null);
            }
        }

        g2d.dispose();
        return mosaic;
    }
}
