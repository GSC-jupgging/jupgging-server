package com.gdsc.jupgging.domain;

public class CoordinateRequest {

    private Coordinate[] coordinates;

    public CoordinateRequest() {
    }

    public CoordinateRequest(Coordinate[] coordinates) {
        this.coordinates = coordinates;
    }

    public Coordinate[] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinate[] coordinates) {
        this.coordinates = coordinates;
    }
}
