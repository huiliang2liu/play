package com.xh.movies.platforms;

import android.util.Log;


import com.xh.movies.Util;
import com.xh.paser.AbsPlatform;
import com.xh.paser.Detial;
import com.xh.paser.ListMove;
import com.xh.paser.Title;

import org.json.JSONObject;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.seimicrawler.xpath.JXDocument;
import org.seimicrawler.xpath.JXNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AiKanMeiJu extends AbsPlatform {
    private static final String TAG = "AiKanMeiJu";
    private static final String HOST = "https://www.akmeiju.com";

    @Override
    public List<Title> types() {
        List<Title> titles =new ArrayList<>();
        titles.add(new Title("全部","https://www.akmeiju.com/"));
        return titles;
    }

    @Override
    public List<Title> titles(String url) {
        List<Title> titles = new ArrayList<>();
        try {
            Document document = createConnection(url).get();
            JXDocument jx = JXDocument.create(document);
            List<JXNode> nodes = jx.selN("//div[@class='row']/div/ul/li/a");
            for (JXNode node : nodes) {
                Element elementt = node.asElement();
                String text = elementt.text();
                if (text.indexOf("/") < 0)
                    continue;
                String href = elementt.attr("href");
                href = Util.dealWithUrl(href, url, HOST);
                Title title = new Title();
                title.title = text;
                title.url = href;
                titles.add(title);
            }
        } catch (IOException e) {
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
            Document document = createConnection(url).get();
            JXDocument jx = JXDocument.create(document);
            List<JXNode> nodes = jx.selN("//div[@class='myui-panel_bd']/ul[@class='myui-vodlist clearfix']/li/div");
            for (JXNode node : nodes) {
                List<JXNode> as = node.sel("a");
                if (as.size() < 0)
                    continue;
                JXNode a = as.get(0);
                Element ae = a.asElement();
                String img = Util.dealWithUrl(ae.attr("data-original"), url, HOST);
                String detialUrl = Util.dealWithUrl(ae.attr("href"), url, HOST);
                String score = "";
                String time = "";
                List<JXNode> spans = a.sel("span/span/text()");
                if (spans.size() > 1) {
                    score = spans.get(0).asString();
                    time = spans.get(1).asString();
                }
                String state = "";
//                pic-text text-right
                List<JXNode> states = a.sel("span[@class='pic-text text-right']/text()");
                if (states.size() > 0)
                    state = states.get(0).asString();
                String name = "";
                List<JXNode> names = node.sel("div/h4/a/text()");
                if (names.size() > 0)
                    name = names.get(0).asString();
                String text = "";
                List<JXNode> texts = node.sel("div/p/text()");
                if (texts.size() > 0)
                    text = texts.get(0).asString();
                Detial detial = new Detial();
                detial.img = img;
                detial.detialUrl = detialUrl;
                detial.score = score;
                detial.time = time;
                detial.state = state;
                detial.name = name;
                detial.text = text;
                detials.add(detial);
            }
            List<JXNode> li_as = jx.selN("//ul[@class='myui-page text-center clearfix']/li/a");
            for (JXNode node : li_as) {
                Element element = node.asElement();
                if (element.text().contains("下一页")) {
                    String href = element.attr("href");
                    href = Util.dealWithUrl(href, url, HOST);
                    Log.e(TAG, href);
                    listMove.next = href;
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listMove;
    }

    @Override
    public boolean playDetail(Detial detial) {
        try {
            Document document = createConnection(detial.detialUrl).get();
            JXDocument jx = JXDocument.create(document);
            List<JXNode> details = jx.selN("//div[@class='myui-content__detail']");
            String type = "";
            String area = "";
            String daoyan = "";
            String jianjie = "";
            List<String> blanks = new ArrayList<>();
            if (details.size() > 0) {
                JXNode detail = details.get(0);
                List<JXNode> p = detail.sel("p");
                if (p.size() > 0) {
                    List<JXNode> p_a = p.get(0).sel("a/text()");
                    if (p_a.size() > 0) {
                        type = p_a.get(0).asString();
                        area = p_a.get(1).asString();
                    }
                    List<JXNode> blankNs = p.get(2).sel("a/text()");
                    if (blankNs.size() > 0)
                        for (JXNode node : blankNs)
                            blanks.add(node.asString());
                    List<JXNode> daoyans = p.get(3).sel("a/text()");
                    if (daoyans.size() > 0)
                        daoyan = daoyans.get(0).asString();
                }

            }
            List<JXNode> jianjies = jx.selN("//div[@class='myui-panel_bd']/div/span[@class='sketch content']/text()");
            if (jianjies.size() > 0)
                jianjie = jianjies.get(0).asString();
            List<JXNode> a_s = null;
            for (int i = 1; i < 10; i++) {
                a_s = jx.selN(String.format("//div[@id='playlist%s']/ul/li/a", i));
                if (a_s.size() > 0)
                    break;
            }

            if (a_s == null || a_s.size() <= 0)
                return false;
            List<Detial.DetailPlayUrl> playUrls = new ArrayList<>();
            for (JXNode a : a_s) {
                Element element = a.asElement();
                String title = element.text();
                String href = element.attr("href");
                Log.e(TAG, href);
                href = Util.dealWithUrl(href, detial.detialUrl, HOST);
                Detial.DetailPlayUrl playUrl = new Detial.DetailPlayUrl();
                playUrl.title = title;
                playUrl.href = href;
                playUrls.add(playUrl);
            }

            detial.type = type;
            detial.area = area;
            detial.daoyan = daoyan;
            detial.jianjie = jianjie;
            detial.playUrls = playUrls;
            detial.platformas = name();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public String play(Detial.DetailPlayUrl playUrl) {
        Log.e(TAG, playUrl.href);
        try {
            Document document = createConnection(playUrl.href).get();
            JXDocument jx = JXDocument.create(document);
            List<JXNode> ress = jx.selN("//div/script/text()");
            for (JXNode node : ress) {
                String res = node.asString();
                Log.e(TAG, res);
                if (res.contains("=") && res.contains("{")) {
                    res = res.substring(res.indexOf("=") + 1);
                    JSONObject jsonObject = new JSONObject(res);
                    res = jsonObject.getString("url");
                    res = Util.dealWithUrl(res, playUrl.href, HOST);
                    return res;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public List<Detial> search(String text) {
        String url = String.format("https://www.akmeiju.com/vsearch.html?wd=%s&submit=", text);
        List<Detial> detials = new ArrayList<>();
        try {
            Document document = createConnection(url).get();
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
        return "爱看美剧";
    }
}
