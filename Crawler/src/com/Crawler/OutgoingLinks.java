package com.Crawler;

import java.util.List;


public class OutgoingLinks {

    private Integer srcId;
    private List<String> destUrls;

    public OutgoingLinks(Integer srcId, List<String> destUrls) {
        this.srcId = srcId;
        this.destUrls = destUrls;
    }

    public Integer getSrcId() {
        return srcId;
    }

    public List<String> getDestUrls() {
        return destUrls;
    }
}
