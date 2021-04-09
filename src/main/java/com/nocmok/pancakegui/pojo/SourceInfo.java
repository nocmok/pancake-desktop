package com.nocmok.pancakegui.pojo;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.nocmok.pancake.Spectrum;

public class SourceInfo {

    private File path;

    /** band number to spectrum */
    private Map<Integer, Spectrum> bandsMapping;

    public SourceInfo(File path, Map<Integer, Spectrum> bandsMapping) {
        this.path = path;
        this.bandsMapping = bandsMapping;
    }

    private static Map<Integer, Spectrum> remap(Map<Integer, String> mapping) {
        Map<Integer, Spectrum> remapping = new HashMap<>();
        for (Map.Entry<Integer, String> entry : mapping.entrySet()) {
            remapping.put(entry.getKey(), Spectrum.valueOf(entry.getValue()));
        }
        return remapping;
    }

    public static SourceInfo of(File path, Map<Integer, String> mapping) {
        return new SourceInfo(path, remap(mapping));
    }

    public File path() {
        return path;
    }

    public Map<Integer, Spectrum> mapping() {
        return bandsMapping;
    }
}
