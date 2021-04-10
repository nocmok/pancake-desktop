package com.nocmok.pancakegui;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.nocmok.pancake.Spectrum;
import com.nocmok.pancakegui.pojo.SourceInfo;

/** Session data */
public class Session {

    private List<SourceInfo> dataset;

    private Set<Spectrum> spectrumAssigned;

    public Session() {
        dataset = new ArrayList<>();
        spectrumAssigned = EnumSet.noneOf(Spectrum.class);
    }

    /**
     * 
     * @param source
     * @return success
     */
    public boolean addSource(SourceInfo source) {
        if (!Collections.disjoint(source.mapping().values(), spectrumAssigned)) {
            return false;
        }
        dataset.add(source);
        return true;
    }

    public boolean removeSource(SourceInfo source) {
        boolean success = dataset.remove(source);
        spectrumAssigned.retainAll(source.mapping().values());
        return success;
    }

    /**
     * 
     * @return copy of dataset
     */
    public List<SourceInfo> dataset() {
        return List.copyOf(dataset);
    }

    /**
     * 
     * @return
     */
    public List<SourceInfo> minifyDataset() {
        Map<File, Map<Integer, Spectrum>> dsMap = new HashMap<>();
        for (SourceInfo si : dataset) {
            Map<Integer, Spectrum> siMap = dsMap.get(si.path());
            if (siMap == null) {
                dsMap.put(si.path(), new HashMap<>(si.mapping()));
            } else {
                siMap.putAll(dsMap.get(si.path()));
            }
        }
        List<SourceInfo> minifiedDs = new ArrayList<>();
        for (Entry<File, Map<Integer, Spectrum>> entry : dsMap.entrySet()) {
            minifiedDs.add(new SourceInfo(entry.getKey(), entry.getValue()));
        }
        return minifiedDs;
    }
}
