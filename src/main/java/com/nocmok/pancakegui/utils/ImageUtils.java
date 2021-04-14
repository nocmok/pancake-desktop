package com.nocmok.pancakegui.utils;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.function.Consumer;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.Dimension;
import java.awt.Graphics2D;

import com.nocmok.pancake.Pancake;
import com.nocmok.pancake.PancakeDataset;
import com.nocmok.pancakegui.PancakeApp;
import com.nocmok.pancakegui.pojo.ImageInfo;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

public class ImageUtils {

    private static final ImageUtils singleton = new ImageUtils();

    private static final LRUCache<File, ImageInfo> imageInfoCache = new LRUCache<File, ImageInfo>(10);

    public static ImageUtils get() {
        return singleton;
    }

    private ImageUtils() {
    }

    public void getInfo(File imgFile, Consumer<ImageInfo> listener) {
        ImageInfo infoInCache = imageInfoCache.get(imgFile);
        if (infoInCache != null) {
            listener.accept(infoInCache);
        }
        PancakeApp.app().worker().submit(() -> {
            ImageInfo info = getInfo(imgFile);
            Platform.runLater(() -> listener.accept(info));
        });
    }

    public Image getImageThumbnail(File imgFile, int width, int height) {
        try {
            return new Image(imgFile.toURI().toURL().toString(), width, height, true, false, false);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private BufferedImage readRegionAsBufferedImage(File imgFile, int x0, int y0, int xsize, int ysize, int renderXSize,
            int renderYSize) {
        Rectangle region = new Rectangle(x0, y0, xsize, ysize);
        try (ImageInputStream stream = ImageIO.createImageInputStream(imgFile)) {
            Iterator<ImageReader> readers = ImageIO.getImageReaders(stream);
            if (readers.hasNext()) {
                ImageReader reader = readers.next();
                reader.setInput(stream);

                ImageReadParam param = reader.getDefaultReadParam();
                param.setSourceRegion(region);

                BufferedImage image = (BufferedImage) reader.read(0, param);

                java.awt.Image tmp = image.getScaledInstance(renderXSize, renderYSize, java.awt.Image.SCALE_FAST);
                BufferedImage dimg = new BufferedImage(renderXSize, renderYSize, BufferedImage.TYPE_INT_ARGB);

                Graphics2D g2d = dimg.createGraphics();
                g2d.drawImage(tmp, 0, 0, null);
                g2d.dispose();

                return dimg;
            } else {
                throw new RuntimeException("unsupported file format");
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("failed to read region due to i/o error", e);
        }
    }

    public Image readRegion(File imgFile, int x0, int y0, int xsize, int ysize, int renderXSize, int renderYSize) {
        BufferedImage image = readRegionAsBufferedImage(imgFile, x0, y0, xsize, ysize, renderXSize, renderYSize);
        Image result = SwingFXUtils.toFXImage(image, null);
        return result;
    }

    public ImageInfo getInfo(File imgFile) {
        ImageInfo ii = imageInfoCache.get(imgFile);
        if (ii != null) {
            return ii;
        }
        PancakeDataset dataset = Pancake.open(imgFile, Pancake.ACCESS_READONLY);
        ii = new ImageInfo(dataset.xSize(), dataset.ySize(), imgFile, dataset.bands().size(), dataset.formatString());
        imageInfoCache.add(imgFile, ii);
        return ii;
    }
}
