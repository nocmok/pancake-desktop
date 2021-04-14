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
import java.awt.Graphics2D;

import com.nocmok.pancake.Pancake;
import com.nocmok.pancake.PancakeDataset;
import com.nocmok.pancakegui.PancakeApp;
import com.nocmok.pancakegui.pojo.ImageInfo;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

public class ImageUtils {

    private static final ImageUtils singleton = new ImageUtils();

    private static final LRUCache<File, ImageInfo> imageInfoCache = new LRUCache<File, ImageInfo>(10);

    private static final LRUCache<File, Image> imageOverviewCache = new LRUCache<File, Image>(10);

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
        Image imgInCache = imageOverviewCache.get(imgFile);
        if (imgInCache != null) {
            return imgInCache;
        }
        try {
            BufferedImage image = ImageIO.read(imgFile);
            Image overview = SwingFXUtils.toFXImage(resize(image, width, height), null);
            imageOverviewCache.add(imgFile, overview);
            return overview;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void getImageThumbnail(File imgFile, int width, int height, Consumer<Image> listener) {
        Image imageInCache = imageOverviewCache.get(imgFile);
        if(imageInCache != null){
            listener.accept(imageInCache);
        }
        PancakeApp.app().worker().submit(() -> {
            Image image = getImageThumbnail(imgFile, width, height);
            Platform.runLater(() -> listener.accept(image));
        });
    }

    private BufferedImage resize(BufferedImage image, int newW, int newH) {
        java.awt.Image tmp = image.getScaledInstance(newW, newH, java.awt.Image.SCALE_FAST);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return dimg;
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
                return resize(image, renderXSize, renderYSize);
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
