package com.glamreserve.glamreserve.entities.utils;

import java.util.List;

public class GeocodingResponse {
    private String status;
    private List<GeocodingResult> results;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<GeocodingResult> getResults() {
        return results;
    }

    public void setResults(List<GeocodingResult> results) {
        this.results = results;
    }
}