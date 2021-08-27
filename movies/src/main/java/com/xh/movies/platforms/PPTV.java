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

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.nodes.Element;
import org.seimicrawler.xpath.JXDocument;
import org.seimicrawler.xpath.JXNode;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class PPTV extends AbsPlatform {
    private static final String TAG = "PPTV";
    private static final String HOST = "http://sou.pptv.com";

    @Override
    public List<Title> types() {
        List<Title> titles = new ArrayList<>();
        String url = "http://sou.pptv.com/category/typeid_1_cataid_100_sortType_time";
        try {
            JXDocument jx = JXDocument.create(createConnection(url).get());
            List<JXNode> as = jx.selN("//div[@class='navBox']/nav/ul/li/a");
            for (JXNode a : as) {
                Element element = a.asElement();
                String name = element.text();
                boolean have = "电视剧".equals(name) || "电影".equals(name)
                        || "VIP尊享".equals(name) || "综艺".equals(name) ||
                        "少儿".equals(name) || "动漫".equals(name) || "教育".equals(name);
                if (!have)
                    continue;
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
            List<JXNode> as = jx.selN("//li[@class='filter_detail_row'][1]/ul/li/a");
            for (JXNode a : as) {
                Element element = a.asElement();
                String name = element.text();
                if ("全部".equals(name))
                    continue;
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
    public String name() {
        return "PPTV";
    }

    @Override
    public ListMove list(String url) {
        ListMove listMove = new ListMove();
        List<Detial> detials = new ArrayList<>();
        listMove.detials = detials;
        try {
            JXDocument jx = JXDocument.create(createConnection(url).get());
            List<JXNode> as = jx.selN("//ul[@class='clearfix']/li/div/a");
            for (JXNode a : as) {
                Element element = a.asElement();
                List<JXNode> imgs = a.sel("img");
                if (imgs.size() <= 0)
                    continue;
                String href = element.attr("href");
                href = Util.dealWithUrl(href, url, HOST);
                element = imgs.get(0).asElement();
                String name = element.attr("alt");
                String img = element.attr("src");
                img = Util.dealWithUrl(img, url, HOST);
                Detial detial = new Detial();
                detial.name = name;
                detial.img = img;
                detial.detialUrl = href;
                detials.add(detial);

            }
            as = jx.selN("//div[@class='pagination']/ul/li/a");
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
        return listMove;
    }

    @Override
    public boolean playDetail(Detial detial) {
        List<Detial.DetailPlayUrl> detailPlayUrls = new ArrayList<>();
        try {
            JXDocument jx = JXDocument.create(createConnection(detial.detialUrl).get());
            List<JXNode> scripts = jx.selN("//script/text()");
            for (JXNode script : scripts) {
                String text = script.asString();
                if (!text.contains("webcfg"))
                    continue;
                text = text.trim();
                Log.e(TAG,text);
                text = text.split("webcfg\\s=\\s")[1];
                Log.e(TAG,text);
                text = text.split(";")[0];
                Log.e(TAG,text);
                text = text.trim();
                JSONObject object = new JSONObject(text);
                object = object.optJSONObject("playList");
                if (object != null) {
                    object = object.optJSONObject("data");
                    if (object != null) {
                        JSONArray array = object.optJSONArray("list");
                        if (array != null && array.length() > 0)
                            for (int i = 0; i < array.length(); i++) {
                                object = array.optJSONObject(i);
                                Detial.DetailPlayUrl playUrl = new Detial.DetailPlayUrl();
                                playUrl.href = Util.dealWithUrl(object.optString("url"), detial.detialUrl, HOST);
                                playUrl.title = object.optString("title");
                                detailPlayUrls.add(playUrl);
                            }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (detailPlayUrls.size() <= 0) {
            Detial.DetailPlayUrl playUrl = new Detial.DetailPlayUrl();
            playUrl.href = detial.detialUrl;
            playUrl.title = detial.name;
            detailPlayUrls.add(playUrl);
        }
        detial.playUrls = detailPlayUrls;
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
        String url = String.format("https://sou.pptv.com/s_video?kw=%s&context=default", URLEncoder.encode(text));
        List<Detial> detials = new ArrayList<>();
        try {
            JXDocument jx = JXDocument.create(createConnection(url).get());
            List<JXNode> as = jx.selN("//div[@class='positive-box clearfix']/a");
            for (JXNode a : as) {
                Element element = a.asElement();
                Detial detial = new Detial();
                String name = element.attr("title");
                detial.name = name;
                String href = element.attr("href");
                href = Util.dealWithUrl(href, url, HOST);
                detial.detialUrl = href;
                List<JXNode> imgs = a.sel("img/@src");
                if (imgs.size() > 0)
                    detial.img = Util.dealWithUrl(imgs.get(0).asString(), url, HOST);
                detials.add(detial);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return detials;
    }
}
