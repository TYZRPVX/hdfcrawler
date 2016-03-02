package edu.hit.ehealth.main.crawler.basestruct;

public interface NextPageTracker {

    int extractPageNum(String line);

    String getNextPageUrl();

}
