package com.xh.play.platforms;

import android.util.Log;

import com.xh.play.entities.Detial;
import com.xh.play.entities.ListMove;
import com.xh.play.entities.Title;
import com.xh.play.utils.Util;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.seimicrawler.xpath.JXDocument;
import org.seimicrawler.xpath.JXNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WaiJuWang implements IPlatform {
    private static final String HOST = "https://www.5kysw.com";
    private static final String TAG = "WaiJuWang";

    @Override
    public List<Title> types() {
        Log.e(TAG, "types");
        String url = "https://www.5kysw.com/";
        List<Title> titles = new ArrayList<>();
        try {
            JXDocument jx = JXDocument.create(Jsoup.connect(url).get());
            List<JXNode> as = jx.selN("//ul[@class='menulist hidden-xs']/li/a");
            for (JXNode node : as) {
                Element element = node.asElement();
                String name = element.text();
                if (name.equals("首页") || name.equals("专题"))
                    continue;
                name = name.trim();
                String href = element.attr("href");
                if (href == null || href.isEmpty())
                    continue;
                href = Util.dealWithUrl(href, url, HOST);
                Title title = new Title();
                title.title = name;
                title.url = href;
                titles.add(title);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return titles;
    }

    @Override
    public List<Title> titles(String url) {
        Log.e(TAG, "titles");
        List<Title> titles = new ArrayList<>();
        try {
            JXDocument jx = JXDocument.create(Jsoup.connect(url).get());
            List<JXNode> dds = jx.selN("//dd[@class='clearfix']");
            if (dds.size() > 0) {
                dds = dds.get(0).sel("a");
                for (JXNode node : dds) {
                    Element element = node.asElement();
                    Title title = new Title();
                    title.title = element.text();
                    title.url = Util.dealWithUrl(element.attr("href"), url, HOST);
                    titles.add(title);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return titles;
    }

    @Override
    public String name() {
        return "外剧网";
    }

    @Override
    public ListMove list(String url) {
        Log.e(TAG, "list");
        ListMove listMove = new ListMove();
        try {
            JXDocument jx = JXDocument.create(Jsoup.connect(url).get());
            List<JXNode> lis = jx.selN("//div[@class='item']/ul[@class='clearfix']/li/a");
            List<Detial> detials = new ArrayList();
            for (JXNode li : lis) {
                Element element = li.asElement();
                String href = element.attr("href");
                if (href == null || href.isEmpty())
                    continue;
                href = Util.dealWithUrl(href, url, HOST);
                String img = element.attr("data-original");
                img = Util.dealWithUrl(img, url, HOST);
                String title = element.attr("title");
                String score = "0.0";
                List<JXNode> nodes = li.sel("span[@class='score']/text()");
                if (nodes.size() > 0)
                    score = nodes.get(0).asString();
                Detial detial = new Detial();
                detial.detialUrl = href;
                detial.img = img;
                detial.name = title;
                detial.score = score;
                detial.platformas = name();
                detials.add(detial);
            }
            listMove.detials = detials;
            lis = jx.selN("//a[@class='page_link']");
            for (JXNode node : lis) {
                Element element = node.asElement();
                String text = element.text();
                if (text.equals("下一页")) {
                    listMove.next = Util.dealWithUrl(element.attr("href"), url, HOST);
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
        Log.e(TAG, "playDetail");
        List<Detial.DetailPlayUrl> detailPlayUrls = new ArrayList<>();
        try {
            JXDocument jx = JXDocument.create(Jsoup.connect(detial.detialUrl).get());
            List<JXNode> divs = jx.selN("//div[@id='playlist']/div");
            for (JXNode div : divs) {
                List<JXNode> nodes = div.sel("a/@title");
                String text = "";
                if (nodes.size() > 0)
                    text = nodes.get(0).asString();
                nodes = div.sel("div/ul/li/a");
                for (JXNode node : nodes) {
                    Element element = node.asElement();
                    String href = element.attr("href");
                    if (href == null || href.isEmpty())
                        continue;
                    href = Util.dealWithUrl(href, detial.detialUrl, HOST);
                    String title = text + " " + element.attr("title");
                    Detial.DetailPlayUrl detailPlayUrl = new Detial.DetailPlayUrl();
                    detailPlayUrl.title = title;
                    detailPlayUrl.href = href;
                    detailPlayUrls.add(detailPlayUrl);
                }
                detial.playUrls = detailPlayUrls;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public String play(Detial.DetailPlayUrl playUrl) {
        Log.e(TAG, "play");
        try {
            JXDocument jx = JXDocument.create(Jsoup.connect(playUrl.href).get());
            List<JXNode> nodes = jx.selN("//script/text()");
            for (JXNode node : nodes) {
                String text = node.asString();
                if (text.contains(".m3u8") && text.contains("=")) {
                    JSONObject jsonObject = new JSONObject(text.split("=")[1]);
                    return jsonObject.getString("url");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public List<Detial> search(String text) {
        Log.e(TAG, "search");
        List<Detial> detials =new ArrayList<Detial>();
        String url = "https://www.5kysw.com/vodsearch.html";
        try {
            JXDocument jx = JXDocument.create(Jsoup.connect(url).data("wd", text).post());
            List<JXNode> dls = jx.selN("//dl[@class='content']");
            for (JXNode dl : dls) {
                List<JXNode> nodes = dl.sel("dt/a");
                if (nodes.size() <= 0)
                    continue;
                Element element = nodes.get(0).asElement();
                String href = element.attr("href");
                if (href == null || href.isEmpty())
                    continue;
                href = Util.dealWithUrl(href, url, HOST);
                String style = element.attr("style");
                if (style != null && style.contains("(") && style.contains(")")) {
                    style = Util.dealWithUrl(style.split("\\(")[1].split("\\)")[0],url,HOST);
                }
                String name = "";
                nodes = dl.sel("dd/div/h3/a/text()");
                if (nodes.size() > 0)
                    name = nodes.get(0).asString();
                Detial detial = new Detial();
                detial.detialUrl = href;
                detial.img = style;
                detial.name= name;
                detial.platformas=name();
                detials.add(detial);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return detials;
    }
}
