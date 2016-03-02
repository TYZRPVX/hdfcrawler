package edu.hit.ehealth.main.crawler.basestruct;

import edu.hit.ehealth.main.util.Utils;
import org.apache.http.client.fluent.Content;
import org.apache.http.concurrent.FutureCallback;

public class FutureListener implements FutureCallback<Content> {

    @Override
    public void failed(final Exception ex) {
        if (Utils.SHOULD_PRT) System.out.println("\n" + ex.getClass() + ", " + ex.getMessage() + ": ");
    }

    @Override
    public void completed(final Content content) {
        if (Utils.SHOULD_PRT) System.out.println("\nRequest completed: ");
//        content.
    }

    @Override
    public void cancelled() {
    }

}
