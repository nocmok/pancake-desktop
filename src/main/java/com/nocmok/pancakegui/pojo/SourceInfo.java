package com.nocmok.pancakegui.pojo;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
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
        Map<Integer, Spectrum> remapping = remap(mapping);
        if(!isValidMapping(remapping)){
            throw new IllegalArgumentException("mapping contains spectrum duplicates");
        } 
        return new SourceInfo(path, remapping);
    }

    public static boolean isValidMappingStr(Map<Integer, String> mapping) {
        return isValidMapping(remap(mapping));
    }

    public static boolean isValidMapping(Map<Integer, Spectrum> mapping) {
        return (new HashSet<>(mapping.values()).size() == mapping.values().size());
    }

    public File path() {
        return path;
    }

    public Map<Integer, Spectrum> mapping() {
        return bandsMapping;
    }

    @Override
    public int hashCode() {
        return path.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof SourceInfo)) {
            return false;
        }
        SourceInfo otherSource = (SourceInfo) other;
        return otherSource.path.equals(path) && otherSource.bandsMapping.equals(bandsMapping);
    }
}
