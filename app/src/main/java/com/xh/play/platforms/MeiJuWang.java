package com.xh.play.platforms;

import android.util.Log;

import com.xh.play.entities.Detial;
import com.xh.play.entities.ListMove;
import com.xh.play.entities.Title;
import com.xh.play.platforms.IPlatforms;
import com.xh.play.utils.Util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.seimicrawler.xpath.JXDocument;
import org.seimicrawler.xpath.JXNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MeiJuWang implements IPlatforms {
    private static final String TAG = "MeiJuWang";
    private static final String HOST = "https://91mjw.com";

    public MeiJuWang() {
        super();
    }

    @Override
    public List<Title> main() {
        List<Title> list = new ArrayList<>();
        try {
            Document document = Jsoup.connect(HOST).get();
            JXDocument jx = JXDocument.create(document);
            List<JXNode> titles = jx.selN("//ul[@class='nav']/li/a");
            for (JXNode node : titles) {
                Element element = node.asElement();
                if (element.text().equals("首页"))
                    continue;
                Title title = new Title();
                title.title = element.text();
                if (element.attr("href") == null || element.attr("href").isEmpty())
                    continue;
                title.url = Util.dealWithUrl(element.attr("href"), HOST + "/", HOST);
                list.add(title);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public ListMove list(String url) {
        Log.e(TAG, url);
        ListMove listMove = new ListMove();
        try {
            Document document = Jsoup.connect(url).get();
            JXDocument jx = JXDocument.create(document);
            List<Detial> detials = new ArrayList<>();
            List<JXNode> articles = jx.selN("//div[@class='m-movies clearfix']/article");
            for (JXNode article : articles) {
                List<JXNode> as = article.sel("a");
                if (as.size() <= 0)
                    continue;
                JXNode a = as.get(0);
                List<JXNode> imgs = a.sel("div/img");
                if (imgs.size() <= 0)
                    continue;
                Element imgElement = imgs.get(0).asElement();
                String img = Util.dealWithUrl(imgElement.attr("data-original"), url, HOST);
                String detialUrl = Util.dealWithUrl(a.asElement().attr("href"), url, HOST);
                String score = "";
                String time = "";
                List<JXNode> spans = article.sel("div[@class='pingfen']/span/text()");
                if (spans.size() > 0)
                    score = spans.get(0).asString();
                String state = "";
                String meta = "";
//                pic-text text-right
                List<JXNode> states = article.sel("div[@class='zhuangtai']/span/text()");
                if (states.size() > 0)
                    state = states.get(0).asString();

                List<JXNode> metas = article.sel("div[@class='meta']/span/text()");
                if (metas.size() > 0)
                    meta = metas.get(0).asString();
                String name = "";
                List<JXNode> names = a.sel("h2/text()");
                if (names.size() > 0)
                    name = names.get(0).asString();
                Detial detial = new Detial();
                detial.img = img;
                detial.detialUrl = detialUrl;
                detial.score = score;
                detial.time = time;
                detial.state = state;
                detial.name = name;
                detial.text = meta;
                detial.platformas = name();
                detials.add(detial);
            }
            List<JXNode> next_page = jx.selN("//li[@class='next-page']/a/@href");
            if (next_page.size() > 0) {
                listMove.next = Util.dealWithUrl(next_page.get(0).asString(), url, HOST);
            }
            listMove.detials = detials;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listMove;
    }

    @Override
    public boolean playDetail(Detial detial) {
        try {
            Document document = Jsoup.connect(detial.detialUrl).get();
            JXDocument jx = JXDocument.create(document);
            List<JXNode> strongs = jx.selN("//div[@class='video_info']/text()");
            if (strongs.size() > 0) {
                String string = strongs.get(0).asString();
                string = string.replace(" / ", "/");
                String strings[] = string.split(" ");
                detial.daoyan = strings[0];
                detial.type = strings[3];
                detial.area = strings[4];
                detial.time = strings[6];
                detial.state = strings[strings.length - 1].replace("\n", "");
            }

            strongs = jx.selN("//p[@class='jianjie']/span/text()");
            if (strongs.size() > 0)
                detial.jianjie = strongs.get(0).asString();
            strongs = jx.selN("//div[@class='vlink']/a");
            List<Detial.DetailPlayUrl> detailPlayUrls = new ArrayList<>();
            for (JXNode node : strongs) {
                Element element = node.asElement();
                Detial.DetailPlayUrl detailPlayUrl = new Detial.DetailPlayUrl();
                detailPlayUrl.title = element.text();
                detailPlayUrl.href = String.format("https://91mjw.com/vplay/%s.html", element.attr("id"));
                detailPlayUrls.add(detailPlayUrl);
            }
            detial.playUrls = detailPlayUrls;
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "====", e);
        }
        return false;
    }

    @Override
    public String play(Detial.DetailPlayUrl playUrl) {
        try {
            Document document = Jsoup.connect(playUrl.href).get();
            JXDocument jx = JXDocument.create(document);
            List<JXNode> nodes = jx.selN("//section[@class='container']/script/text()");
            if (nodes.size() <= 0)
                return "";
            String res = nodes.get(0).asString();
            res = res.substring(res.indexOf("var vid=\"") + "var vid=\"".length());
            res = res.substring(0, res.indexOf("\";"));
            res = res.replaceAll("%2F", "/");
            res = res.replaceAll("%3A", ":");
            Log.e(TAG, res);
            return res;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public List<Detial> search(String text) {
        return list(String.format("https://91mjw.com/?s=%s", text)).detials;
    }

    @Override
    public String name() {
        return "美剧网";
    }
}
