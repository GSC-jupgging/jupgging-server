package com.gdsc.jupgging.domain;

import java.util.List;

public class MapApiResponse {

    private List<MapApiFeature> features;

    public List<MapApiFeature> getFeatures() {
        return features;
    }

    public void setFeatures(List<MapApiFeature> features) {
        this.features = features;
    }
}
