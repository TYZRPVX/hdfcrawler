package edu.hit.ehealth.main.crawler.basestruct;


/**
 * <p> 所有需要有翻页动作的爬虫都需要继承 {@link NextPageTracker}。
 *
 */
public interface NextPageTracker {

    int extractPageNum(String line);

    String getNextPageUrl();

}
