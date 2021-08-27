package com.xh.movies.platforms;

import android.util.Log;

import com.xh.movies.PlatformsManager;
import com.xh.movies.Util;
import com.xh.paser.AbsPlatform;
import com.xh.paser.Detial;
import com.xh.paser.IVip;
import com.xh.paser.ListMove;
import com.xh.paser.Title;
import com.xh.paser.VipParsListener;

import org.jsoup.nodes.Element;
import org.seimicrawler.xpath.JXDocument;
import org.seimicrawler.xpath.JXNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Platform1905 extends AbsPlatform {
    private static final String TAG = "Platform1905";
    private static final String HOST = "https://www.1905.com/vod";

    @Override
    public List<Title> types() {
        List<Title> titles = new ArrayList<>();
        String url = "https://www.1905.com/vod/list/n_1_t_1/o3.html?fr=vodhome_js_lx";
        try {
            JXDocument jx = JXDocument.create(createConnection(url).get());
            List<JXNode> as = jx.selN("//div[@class='grid-12x'][1]/p/a");
            for (JXNode a : as) {
                Element element = a.asElement();
                String name = element.text();
                String href = element.attr("href");
                href = Util.dealWithUrl(href, url, HOST);
                titles.add(new Title(name, href));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return titles;
    }

    @Override
    public List<Title> titles(String url) {
        List<Title> titles = new ArrayList<>();
        try {
            JXDocument jx = JXDocument.create(createConnection(url).get());
            List<JXNode> as = jx.selN("//div[@class='grid-12x'][2]/p/a");
            for (JXNode a : as) {
                Element element = a.asElement();
                String name = element.text();
                if ("全部".equals(name))
                    continue;
                String href = element.attr("onclick");
//                location.href='https://www.1905.com/vod/list/n_1/o3p1.html';return false;
                Pattern pattern = Pattern.compile("'([^']*)");
                Matcher matcher = pattern.matcher(href);
                if (!matcher.find())
                    continue;
                href = matcher.group(1);
                href = Util.dealWithUrl(href, url, HOST);
                titles.add(new Title(name, href));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return titles;
    }

    @Override
    public String name() {
        return "1905";
    }

    @Override
    public ListMove list(String url) {
        ListMove listMove = new ListMove();
        List<Detial> detials = new ArrayList<>();
        try {
            JXDocument jx = JXDocument.create(createConnection(url).get());
            List<JXNode> as = jx.selN("//section[@class='mod row search-list']/div/a");
            for (JXNode a : as) {
                Element element = a.asElement();
                String name = element.attr("title");
                String href = element.attr("href");
                Detial detial = new Detial();
                detial.name = name;
                detial.detialUrl = Util.dealWithUrl(href, url, HOST);
                List<JXNode> imgs = a.sel("img/@src");
                if (imgs.size() > 0)
                    detial.img = Util.dealWithUrl(imgs.get(0).asString(), url, HOST);
                detials.add(detial);
            }
            as = jx.selN("//*[@id='vod-page']/a");
            for (JXNode a : as) {
                Element element = a.asElement();
                String name = element.text();
                if ("下一页".equals(name)) {
                    String href = element.attr("href");
                    href = Util.dealWithUrl(href, url, HOST);
                    if (!href.equals(url))
                        listMove.next = href;
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        listMove.detials = detials;
        return listMove;
    }

    @Override
    public boolean playDetail(Detial detial) {
        List<Detial.DetailPlayUrl> playUrls = new ArrayList<>();
        Detial.DetailPlayUrl playUrl = new Detial.DetailPlayUrl();
        playUrl.title = detial.name;
        playUrl.href = detial.detialUrl;
        playUrls.add(playUrl);
        detial.playUrls = playUrls;
        return true;
    }

    private static class Play {
        String url = "";
    }

    @Override
    public String play(Detial.DetailPlayUrl playUrl) {
        Log.e("playda", playUrl.href);
        IVip vip = PlatformsManager.vip;
        if (vip == null)
            return "";
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Play url = new Play();
        vip.parse(playUrl.href, new VipParsListener() {
            @Override
            public void onListener(String u) {
                if (u != null && !u.isEmpty()) {
                    url.url = u;
                    countDownLatch.countDown();
                }
            }
        });
        try {
            countDownLatch.await(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return url.url;
    }

    @Override
    public List<Detial> search(String text) {
        return null;
    }
}
