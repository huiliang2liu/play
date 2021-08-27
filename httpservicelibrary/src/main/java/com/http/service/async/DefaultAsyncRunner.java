package com.http.service.async;

import com.http.service.HttpService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Default threading strategy for NanoHTTPD.
 * <p/>
 * <p>
 * By default, the server spawns a new Thread for every incoming request.
 * These are set to <i>daemon</i> status, and named according to the request
 * number. The name is useful when profiling the application.
 * </p>
 */
public class DefaultAsyncRunner implements AsyncRunner {

    private long requestCount;

    private final List<HttpService.ClientHandler> running = Collections.synchronizedList(new ArrayList<HttpService.ClientHandler>());

    /**
     * @return a list with currently running clients.
     */
    public List<HttpService.ClientHandler> getRunning() {
        return running;
    }

    @Override
    public void closeAll() {
        // copy of the list for concurrency
        for (HttpService.ClientHandler clientHandler : new ArrayList<HttpService.ClientHandler>(this.running)) {
            clientHandler.close();
        }
    }

    @Override
    public void closed(HttpService.ClientHandler clientHandler) {
        this.running.remove(clientHandler);
    }

    @Override
    public void exec(HttpService.ClientHandler clientHandler) {
        ++this.requestCount;
        Thread t = new Thread(clientHandler);
        t.setDaemon(true);
        t.setName("NanoHttpd Request Processor (#" + this.requestCount + ")");
        this.running.add(clientHandler);
        t.start();
    }
}
