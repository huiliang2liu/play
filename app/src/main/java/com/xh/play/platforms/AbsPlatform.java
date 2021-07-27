package com.xh.play.platforms;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

public abstract class AbsPlatform implements IPlatform{

    protected final Connection createConnection(String url){
        Connection connection = Jsoup.connect(url);
        connection.timeout(10000);
        return connection;
    }
}
