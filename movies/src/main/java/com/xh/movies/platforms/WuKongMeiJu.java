package com.xh.movies.platforms;

import com.xh.movies.Util;
import com.xh.paser.AbsPlatform;
import com.xh.paser.Detial;
import com.xh.paser.ListMove;
import com.xh.paser.Title;

import org.json.JSONObject;
import org.jsoup.nodes.Element;
import org.seimicrawler.xpath.JXDocument;
import org.seimicrawler.xpath.JXNode;

import java.util.ArrayList;
import java.util.List;

public class WuKongMeiJu extends AbsPlatform {
    private final static String HOTS = "https://www.wukongmeiju.com";

    @Override
    public List<Title> types() {
        List<Title> title = new ArrayList<>();
        title.add(new Title("全部", "https://www.wukongmeiju.com/"));
        return title;
    }

    @Override
    public List<Title> titles(String url) {
        List<Title> titles = new ArrayList<>();
        try {
            JXDocument jx = JXDocument.create(createConnection(url).get());
            List<JXNode> as = jx.selN("//ul[@class='nav navbar-nav ff-nav']/li/a");
            for (JXNode a : as) {
                Element element = a.asElement();
                String href = element.attr("href");
                if (href == null || href.isEmpty() || !href.contains("/detail/"))
                    continue;
                href = Util.dealWithUrl(href, url, HOTS);
                titles.add(new Title(element.text(), href));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return titles;
    }

    @Override
    public String name() {
        return "悟空影视";
    }

    @Override
    public ListMove list(String url) {
        ListMove listMove = new ListMove();
        List<Detial> detials = new ArrayList();
        listMove.detials = detials;
        try {
            JXDocument jx = JXDocument.create(createConnection(url).get());
            List<JXNode> as = jx.selN("//p[@class='image']/a");
            for (JXNode a : as) {
                String href = a.asElement().attr("href");
                if (href == null || href.isEmpty())
                    continue;
                href = Util.dealWithUrl(href, url, HOTS);
                List<JXNode> nodes = a.sel("img");
                String img = "";
                String name = "";
                if (nodes.size() > 0) {
                    Element element = nodes.get(0).asElement();
                    img = Util.dealWithUrl(element.attr("data-original"), url, HOTS);
                    name = element.attr("alt");
                }
                Detial detial = new Detial();
                detial.platformas = name();
                detial.name = name;
                detial.img = img;
                detial.detialUrl = href;
                detials.add(detial);
            }
            as = jx.selN("//li[@class='page-item']/a");
            for (JXNode a : as) {
                Element element = a.asElement();
                String text = element.text();
                if ("»".equals(text)) {
                    listMove.next = Util.dealWithUrl(element.attr("href"), url, HOTS);
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
        List<Detial.DetailPlayUrl> playUrls = new ArrayList<>();
        detial.playUrls = playUrls;
        try {
            JXDocument jx = JXDocument.create(createConnection(detial.detialUrl).get());
            List<JXNode> titles = jx.selN("//ul[@class='nav nav-tabs ff-playurl-tab']/li/a/span/text()");
            List<JXNode> uls = jx.selN("//ul[@data-more]");
            int max = titles.size() > uls.size() ? uls.size() : titles.size();
            for (int i = 0; i < max; i++) {
                String title = titles.get(i).asString();
                List<JXNode> as = uls.get(i).sel("li/a");
                for (JXNode a : as) {
                    Element element = a.asElement();
                    String href = element.attr("href");
                    if (href == null || href.isEmpty())
                        continue;
                    href = Util.dealWithUrl(href, detial.detialUrl, HOTS);
                    Detial.DetailPlayUrl playUrl = new Detial.DetailPlayUrl();
                    playUrl.title = title + " " + element.text();
                    playUrl.href = href;
                    playUrls.add(playUrl);
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public String play(Detial.DetailPlayUrl playUrl) {
        try {
            JXDocument jx = JXDocument.create(createConnection(playUrl.href).get());
            List<JXNode> nodes = jx.selN("//script/text()");
            for (JXNode node : nodes) {
                String text = node.asString();
                if (text == null || text.isEmpty() || !text.contains(".m3u8") || !text.contains("="))
                    continue;
                text = text.substring(text.indexOf("=") + 1).replace(";", "").trim();
                JSONObject json = new JSONObject(text);
                return Util.dealWithUrl(json.getString("url"), playUrl.href, HOTS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public List<Detial> search(String text) {
        String url = String.format("https://www.wukongmeiju.com/search/%s.html", text);
        List<Detial> detials = new ArrayList<Detial>();
        while (url != null && !url.isEmpty()) {
            ListMove listMove = list(url);
            url = listMove.next;
            detials.addAll(listMove.detials);
        }
        return detials;
    }
}
