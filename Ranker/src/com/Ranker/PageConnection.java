package com.Ranker;

public class PageConnection {

    private Integer sourceId;
    private Integer destinationId;

    public PageConnection(Integer sourceId, Integer destinationId) {
        this.sourceId = sourceId;
        this.destinationId = destinationId;
    }

    public Integer getSourceId() {
        return sourceId;
    }

    public void setSourceId(Integer sourceId) {
        this.sourceId = sourceId;
    }

    public Integer getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(Integer destinationId) {
        this.destinationId = destinationId;
    }
}
