package com.http.service.async;

import com.http.service.HttpService;

public interface AsyncRunner {
    void closeAll();

    void closed(HttpService.ClientHandler clientHandler);

    void exec(HttpService.ClientHandler code);
}
