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

public class YingShiDaQuan implements IPlatform {
    private static final String TAG = "YingShiDaQuan";
    private static final String HOST = "https://73m.cc";

    @Override
    public List<Title> types() {
        List<Title> titles = new ArrayList<>();
        {
            Title title = new Title();
            title.url = "https://73m.cc/show/1-----------.html";
            title.title = "电影";
            titles.add(title);
        }
        {
            Title title = new Title();
            title.url = "https://73m.cc/show/2-----------.html";
            title.title = "电视剧";
            titles.add(title);
        }
        {
            Title title = new Title();
            title.url = "https://73m.cc/show/3-----------.html";
            title.title = "综艺";
            titles.add(title);
        }
        {
            Title title = new Title();
            title.url = "https://73m.cc/show/4-----------.html";
            title.title = "动漫";
            titles.add(title);
        }
        return titles;
    }

    @Override
    public List<Title> titles(String url) {
        List<Title> titles = new ArrayList<>();
        try {
            JXDocument jx = JXDocument.create(Jsoup.connect(url).get());
            List<JXNode> nodes = jx.selN("//div[@class='slideDown-box']/ul");
            if (nodes.size() > 0) {
                nodes = nodes.get(0).sel("li/a");
                for (JXNode node : nodes) {
                    Element element = node.asElement();
                    String text = element.text();
                    if ("类型".equals(text) || "全部".equals(text))
                        continue;
                    String href = element.attr("href");
                    if (href == null || href.isEmpty())
                        continue;
                    titles.add(new Title(text, Util.dealWithUrl(href, url, HOST)));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return titles;
    }


    @Override
    public ListMove list(String url) {
        ListMove listMove = new ListMove();
        List<Detial> detials = new ArrayList<>();
        listMove.detials = detials;
        try {
            Document document = Jsoup.connect(url).get();
            JXDocument jx = JXDocument.create(document);
            List<JXNode> divs = jx.selN("//div[@class='myui-panel_bd']/ul[@class='myui-vodlist clearfix']/li/div");
            for (JXNode node : divs) {
                List<JXNode> a_s = node.sel("a");
                if (a_s.size() <= 0)
                    continue;
                JXNode a = a_s.get(0);
                Element aE = a.asElement();
                String detialUrl = aE.attr("href");
                detialUrl = Util.dealWithUrl(detialUrl, url, HOST);
                String img = aE.attr("data-original");
                img = Util.dealWithUrl(img, url, HOST);
                String score = "";
                List<JXNode> scores = a.sel("span[@class='pic-tag pic-tag-top']/text()");
                if (scores.size() > 0) {
                    score = scores.get(0).asString();
                }
                String state = "";
                List<JXNode> states = a.sel("span[@class='pic-text text-right']/text()");
                if (states.size() > 0) {
                    state = states.get(0).asString();
                }
                List<JXNode> names = node.sel("div/h4/a/text()");
                String name = "";
                if (names.size() > 0)
                    name = names.get(0).asString();
                List<JXNode> texts = node.sel("div/h4/a/text()");
                String text = "";
                if (texts.size() > 0)
                    text = texts.get(0).asString();
                Detial detial = new Detial();
                detial.img = img;
                detial.detialUrl = detialUrl;
                detial.score = score;
                detial.state = state;
                detial.name = name;
                detial.text = text;
                detials.add(detial);
            }
            List<JXNode> nexts = jx.selN("//a[@class='btn btn-default']");
            for (JXNode node : nexts) {
                Element element = node.asElement();
                if (element.text().equals("下一页")) {
                    listMove.next = element.attr("href");
                    listMove.next = Util.dealWithUrl(listMove.next, url, HOST);
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listMove;
    }

    @Override
    public boolean playDetail(Detial detail) {
        try {
            Document document = Jsoup.connect(detail.detialUrl).get();
            JXDocument jx = JXDocument.create(document);
            List<JXNode> nodes = jx.selN("//div[@class='myui-content__detail']");
            if (nodes.size() > 0) {
                List<JXNode> p = nodes.get(0).sel("p");
                List<JXNode> p_a = p.get(0).sel("a/text()");
                detail.type = p_a.get(0).asString();
                detail.area = p_a.get(1).asString();
//                detail.year = p_a[2]
                detail.state = p.get(1).sel("span[@class='text-red']/text()").get(0).asString();
//                detail.blanks = p[2].xpath('a/text()')
                detail.daoyan = p.get(3).sel("a/text()").get(0).asString();
                detail.jianjie = jx.selN("//div[@class='myui-panel_bd']/div/span/p/text()").get(0).asString();
            }
            detail.platformas = name();
            List<Detial.DetailPlayUrl> detailPlayUrls = new ArrayList<>();
            for (int i = 1; i < 10; i++) {
                List<JXNode> a_s = jx.selN(String.format("//div[@id='playlist%s']/ul/li/a", i));
                if (a_s.size() > 0) {
                    for (JXNode node : a_s) {
                        Element a = node.asElement();
                        Detial.DetailPlayUrl playUrl = new Detial.DetailPlayUrl();
                        playUrl.title = a.text();
                        playUrl.href = a.attr("href");
                        playUrl.href = Util.dealWithUrl(playUrl.href, detail.detialUrl, HOST);
                        detailPlayUrls.add(playUrl);
                    }
                    detail.playUrls = detailPlayUrls;
                    return true;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public String play(Detial.DetailPlayUrl playUrl) {
        try {
            Document document = Jsoup.connect(playUrl.href).get();
            JXDocument jx = JXDocument.create(document);
            String res = jx.selN("//div/script/text()").get(0).asString();
            res = res.substring(res.indexOf("=") + 1);
            res = new JSONObject(res).getString("url");
            return res;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    @Override
    public List<Detial> search(String text) {
        String url = String.format("https://73m.cc/search/-------------.html?wd=%s&submit=", text);
        List<Detial> detials = new ArrayList<>();
        try {
            Document document = Jsoup.connect(url).get();
            JXDocument jx = JXDocument.create(document);
            List<JXNode> lis = jx.selN("//ul[@id='searchList']/li");
            for (JXNode node : lis) {
                List<JXNode> a = node.sel("div[@class='thumb']/a");
                if (a.size() < 0)
                    continue;
                Detial detial = new Detial();
                Element element = a.get(0).asElement();
                detial.img = Util.dealWithUrl(element.attr("data-original"), url, HOST);
                detial.detialUrl = Util.dealWithUrl(element.attr("href"), url, HOST);
                List<JXNode> scores = a.get(0).sel("span[@class='pic-tag pic-tag-top']/text()");
                if (scores.size() > 0) {
                    detial.score = scores.get(0).asString();
                }
                List<JXNode> states = a.get(0).sel("span[@class='pic-text text-right']/text()");
                if (states.size() > 0) {
                    detial.state = states.get(0).asString();
                }
                List<JXNode> names = node.sel("div/h4/a/text()");
                if (names.size() > 0)
                    detial.name = names.get(0).asString();
                List<JXNode> ps = node.sel("div/p/text()");
                detial.daoyan = ps.get(0).asString();
                detial.text = ps.get(1).asString();
                detial.type = ps.get(2).asString();
                detial.platformas = name();
                Log.e(TAG, detial.type);
                detials.add(detial);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return detials;
    }

    @Override
    public String name() {
        return "影视大全";
    }
}
