package com.xh.play.platforms;

import android.util.Log;

import com.nostra13.universalimageloader.utils.L;
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
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class MeiJuNiao implements IPlatforms {
    private static final String TAG = "MeiJuNiao";
    private static final String HOST = "http://www.meijuniao.com";

    @Override
    public List<Title> main() {
        //div[@class='nav-content bgray']/div[@class='nav-c-share']
        List<Title> titles = new ArrayList<>();
        String url = "http://www.meijuniao.com/xijupian/";
        try {
            Document document = Jsoup.connect(url).get();
            JXDocument jx = JXDocument.create(document);
            List<JXNode> nodes = jx.selN("//div[@class='nav-content bgray']/div[@class='nav-c-share']");
            for (JXNode node : nodes) {
                List<JXNode> as = node.sel("a");
                for (int i = 1; i < as.size(); i++) {
                    Element element = as.get(i).asElement();
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
    public ListMove list(String url) {
        ListMove move = new ListMove();
        List<Detial> detials = new ArrayList<>();
        move.detials = detials;
        try {
            Document document = Jsoup.connect(url).get();
            JXDocument jx = JXDocument.create(document);
            List<JXNode> lis = jx.selN("//ul[@id='contents']/li");
            for (JXNode node : lis) {
                List<JXNode> nodes = node.sel("a");
                if (nodes.size() <= 0)
                    continue;
                String href = nodes.get(0).asElement().attr("href");
                if (href == null || href.isEmpty())
                    continue;
                href = Util.dealWithUrl(href, url, HOST);
                nodes = node.sel("a/img/@data-original");
                String img = "";
                if (nodes.size() > 0)
                    img = nodes.get(0).asString();
                String state = "";
                nodes = node.sel("a/i[@class='box-img-txt']/text()");
                if (nodes.size() > 0)
                    state = nodes.get(0).asString();
                String source = "";
                nodes = node.sel("div/span/text()");
                if (nodes.size() > 0)
                    source = nodes.get(0).asString();
                String name = "";
                nodes = node.sel("div/div/p/a/text()");
                if (nodes.size() > 0)
                    name = nodes.get(0).asString();
                Detial detial = new Detial();
                detial.detialUrl = href;
                detial.img = img;
                detial.state = state;
                detial.source = source;
                detial.name = name;
                detials.add(detial);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return move;
    }

    @Override
    public boolean playDetail(Detial detial) {
        try {
            Document document = Jsoup.connect(detial.detialUrl).get();
            JXDocument jx = JXDocument.create(document);
            List<JXNode> nodes = jx.selN("//p[@class='fd-list']");
            String type = "";
            String daoyan = "";
            String text = "";
            String area = "";
            String time = "";
            String jianjie = "";
            float score = 0;
            if (nodes.size() > 4) {
                List<JXNode> as = nodes.get(1).sel("span/a/text()");
                if (as.size() > 1) {
                    daoyan = as.get(as.size() - 1).asString();
                    for (int i = 0; i < as.size() - 1; i++)
                        type = type + as.get(i).asString() + " ";
                }
                as = nodes.get(2).sel("span/a/text()");
                for (JXNode node : as)
                    text = text + node.asString() + " ";
//                地区：美国
                as = nodes.get(2).sel("span/text()");
                if (as.size() > 1) {
                    area = as.get(0).asString();
                    area = area.substring(as.indexOf("："));
                    time = as.get(1).asString();
                    time = time.substring(time.indexOf("："));
                }
            }
            nodes = jx.selN("//ul[@class='rating']/li/@class");
            if (nodes.size() > 0) {
                for (JXNode node : nodes)
                    if (node.asString().contains("active"))
                        score++;
                score = score * 10 / nodes.size();
            }
            nodes = jx.selN("//p[@class='fd-list vod-jj']/span/text()");
            if (nodes.size() > 0)
                jianjie = nodes.get(0).asString();
            nodes = jx.selN("//div[@class='lv-bf-list']/a");
            if (nodes.size() <= 0)
                return false;
            List<Detial.DetailPlayUrl> playUrls = new ArrayList<>(nodes.size());
            for (JXNode node : nodes) {
                Element element = node.asElement();
                String href = element.attr("href");
                if (href == null || href.isEmpty())
                    continue;
                Detial.DetailPlayUrl detailPlayUrl = new Detial.DetailPlayUrl();
                detailPlayUrl.href = Util.dealWithUrl(href, detial.detialUrl, HOST);
                detailPlayUrl.title = element.text();
                playUrls.add(detailPlayUrl);
            }
            detial.type = type;
            detial.daoyan = daoyan;
            detial.text = text;
            detial.area = area;
            detial.time = time;
            detial.jianjie = jianjie;
            detial.playUrls = playUrls;
            return playUrls.size() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public String play(Detial.DetailPlayUrl playUrl) {
        try {
            Document document = Jsoup.connect(playUrl.href).get();
            JXDocument jx = JXDocument.create(document);
            List<JXNode> nodes = jx.selN("//div[@id='zanpiancms_player']/script/text()");
            if (nodes.size() > 0) {
                String href = nodes.get(0).asString();
                if (href == null || href.isEmpty()) {
                    nodes = jx.selN("//div[@id='zanpiancms_player']/script/@src");
                    if (nodes.size() < 0)
                        return "";
                    Log.e(TAG, nodes.get(0).asString());
                    URLConnection connection = new URL(nodes.get(0).asString()).openConnection();
                    connection.setConnectTimeout(5000);
                    connection.setReadTimeout(5000);
                    connection.connect();
                    href = Util.stream2string(connection.getInputStream(), null);
                } else
                    Log.e(TAG, href);
                href = href.substring(href.indexOf("{"));
                href = href.substring(0, href.indexOf("};") + 1);
                Log.e(TAG, href);
                JSONObject jsonObject = new JSONObject(href);
                href = jsonObject.getString("url");
                Log.e(TAG, href);
                return href;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Detial> search(String text) {
        return null;
    }

    @Override
    public String name() {
        return "美剧鸟";
    }
}
