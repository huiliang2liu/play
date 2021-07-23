package com.xh.play.platforms;

import android.util.Log;

import com.xh.play.entities.Detial;
import com.xh.play.entities.ListMove;
import com.xh.play.entities.Title;
import com.xh.play.utils.Util;

import org.json.JSONArray;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.seimicrawler.xpath.JXDocument;
import org.seimicrawler.xpath.JXNode;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class MeiJuTianTang implements IPlatforms {
    private static final String HOST = "https://www.meijutt.tv";
    private static final String TAG = "MeiJuTianTang";

    public MeiJuTianTang() {
        super();
    }

    @Override
    public List<Title> main() {
        List<Title> titles = new ArrayList<>();
        try {
            Document document = Jsoup.connect(HOST).get();
            JXDocument jx = JXDocument.create(document);
            List<JXNode> as = jx.selN("//ul[@class='secNacUl']/li/a");
            for (JXNode node : as) {
                Element a = node.asElement();
                String href = a.attr("href");
                if (href == null || href.isEmpty() || !href.contains("file"))
                    continue;
                href = Util.dealWithUrl(href, HOST + "/", HOST);
                String text = a.text();
                Title title = new Title();
                title.title = text;
                title.url = href;
                titles.add(title);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return titles;
    }

    @Override
    public ListMove list(String url) {
        Log.e(TAG, "list");
        Log.e(TAG, url);
        ListMove listMove = new ListMove();
        List<Detial> detials = new ArrayList<>();
        listMove.detials = detials;
        try {
            Document document = Jsoup.connect(url).get();
            JXDocument jx = JXDocument.create(document);
            List<JXNode> nodes = jx.selN("//div/div[@class='page']/a");
            String next = "";
            for (JXNode node : nodes) {
                Element element = node.asElement();
                if (element.text().equals("下一页")) {
                    next = Util.dealWithUrl(element.attr("href"), url, HOST);
                    break;
                }
            }
            listMove.next = next;
            nodes = jx.selN("//div/div[@class='cn_box2']");
            for (JXNode node : nodes) {
                List<JXNode> list = node.sel("div/div/a");
                if (list.size() <= 0)
                    continue;
                Detial detial = new Detial();
                String href = list.get(0).asElement().attr("href");
                detial.detialUrl = Util.dealWithUrl(href, url, HOST);
                list = list.get(0).sel("img/@src");
                String img = "";
                if (list.size() > 0)
                    img = list.get(0).asString();
                detial.img = Util.dealWithUrl(img, url, HOST);
                list = node.sel("ul/li/a/text()");
                String title = "";
                if (list.size() > 0)
                    title = list.get(0).asString();
                detial.name = title;
                detial.platformas = name();
                list = node.sel("div/div/em/strong/text()");
                if (list.size() > 0) {
                    String score = list.get(0).asString();
                    list = node.sel("div/div/em/span/text()");
                    if (list.size() > 0)
                        score += list.get(0).asString();
                    detial.score = score;
                }
                detials.add(detial);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listMove;
    }

    @Override
    public boolean playDetail(Detial detial) {
        Log.e(TAG, "playDetail");
        Log.e(TAG, detial.detialUrl);
        try {
            Document document = Jsoup.connect(detial.detialUrl).get();
            JXDocument jx = JXDocument.create(document);
            List<JXNode> nodes = jx.selN("//ul[@class='mn_list_li_movie']/ul/li/a/@href");
            String href = "";
            for (JXNode node : nodes) {
                href = node.asString();
                if (href == null || href.isEmpty())
                    continue;
                href = Util.dealWithUrl(href, detial.detialUrl, HOST);
            }
            document = Jsoup.connect(href).get();
            jx = JXDocument.create(document);
            nodes = jx.selN("//div[@class='ptitle']/script/@src");
            if (nodes.size() <= 0)
                return false;
            String js = nodes.get(0).asString();
            js = Util.dealWithUrl(js, detial.detialUrl, HOST);
            URLConnection connection = new URL(js).openConnection();
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.connect();
            js = Util.stream2string(connection.getInputStream(), null);
            js = js.substring("var VideoListJson=".length(), js.length() - ",urlinfo='http://'+document.domain+'/video/25986-<from>-<pos>.html';".length());
            Log.e(TAG, js);
            JSONArray jsonArray = new JSONArray(js);
            List<Detial.DetailPlayUrl> detailPlayUrls = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONArray array = jsonArray.getJSONArray(i);
                String name = array.getString(0);
                array = array.getJSONArray(1);
                for (int j = 0; j < array.length(); j++) {
                    JSONArray src = array.getJSONArray(j);
                    Detial.DetailPlayUrl detailPlayUrl = new Detial.DetailPlayUrl();
                    detailPlayUrl.title = name + src.getString(0);
                    detailPlayUrl.href = src.getString(1);
                    detailPlayUrl.href = detailPlayUrl.href.substring(0, detailPlayUrl.href.lastIndexOf("."));
                    detailPlayUrl.href += ".m3u8";
                    detailPlayUrl.href = Util.dealWithUrl(detailPlayUrl.href, detial.detialUrl, HOST);
                    detailPlayUrls.add(detailPlayUrl);
                }
            }
            detial.playUrls = detailPlayUrls;
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        ////div[@class='ptitle']/script/@src
        return false;
    }

    @Override
    public String play(Detial.DetailPlayUrl playUrl) {
        Log.e(TAG, playUrl.href);
        return playUrl.href;
    }

    @Override
    public List<Detial> search(String text) {
        if (true)
            return new ArrayList<>();
        Log.e(TAG, "search");
        String url = "https://www.meijutt.tv/sousuo/index.asp";
        List<Detial> detials = new ArrayList<>();
        try {
            Document document = Jsoup.connect(url).data("searchword", text).post();
            JXDocument jx = JXDocument.create(document);
            List<JXNode> nodes = jx.selN("//div/div[@class='page']/a");
            String next = "";
            for (JXNode node : nodes) {
                Element element = node.asElement();
                if (element.text().equals("下一页")) {
                    next = Util.dealWithUrl(element.attr("href"), url, HOST);
                    break;
                }
            }
            nodes = jx.selN("//div/div[@class='cn_box2']");
            for (JXNode node : nodes) {
                List<JXNode> list = node.sel("div/div/a");
                if (list.size() <= 0)
                    continue;
                Detial detial = new Detial();
                String href = list.get(0).asElement().attr("href");
                detial.detialUrl = Util.dealWithUrl(href, url, HOST);
                list = list.get(0).sel("img/@src");
                String img = "";
                if (list.size() > 0)
                    img = list.get(0).asString();
                detial.img = Util.dealWithUrl(img, url, HOST);
                list = node.sel("ul/li/a/text()");
                String title = "";
                if (list.size() > 0)
                    title = list.get(0).asString();
                detial.name = title;
                detial.platformas = name();
                list = node.sel("div/div/em/strong/text()");
                if (list.size() > 0) {
                    String score = list.get(0).asString();
                    list = node.sel("div/div/em/span/text()");
                    if (list.size() > 0)
                        score += list.get(0).asString();
                    detial.score = score;
                }
                detials.add(detial);
            }
            while (next != null && !next.isEmpty()) {
                ListMove move = list(next);
                next = move.next;
                detials.addAll(move.detials);
            }
            Log.e(TAG, "dada " + detials.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return detials;
    }

    @Override
    public String name() {
        return "美剧天堂";
    }
}
