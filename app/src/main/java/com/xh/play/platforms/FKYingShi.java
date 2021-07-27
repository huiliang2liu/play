package com.xh.play.platforms;

import com.xh.play.entities.Detial;
import com.xh.play.entities.ListMove;
import com.xh.play.entities.Title;
import com.xh.play.utils.Util;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.seimicrawler.xpath.JXDocument;
import org.seimicrawler.xpath.JXNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FKYingShi extends AbsPlatform {
    private final static String HOST = "https://www.25ys.net";

    @Override
    public List<Title> types() {
        String url = "https://www.25ys.net/";
        List<Title> titles = new ArrayList<>();
        try {
            JXDocument jx = JXDocument.create(createConnection(url).get());
            List<JXNode> nodes = jx.selN("//div[@class='row']/ul/li/a");
            for (JXNode node : nodes) {
                Element element = node.asElement();
                String href = element.attr("href");
                if (href == null || href.isEmpty() || !href.contains("/type/"))
                    continue;
                href = Util.dealWithUrl(href, url, HOST);
                String text = element.text();
                titles.add(new Title(text, href));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return titles;
    }

    @Override
    public List<Title> titles(String url) {
        List<Title> titles = new ArrayList();
        try {
            JXDocument jx = JXDocument.create(createConnection(url).get());
            List<JXNode> uls = jx.selN("//div[@class='stui-pannel_hd']/ul");
            for (JXNode ul : uls) {
                List<JXNode> nodes = ul.sel("li/span/text()");
                if (nodes.size() <= 0)
                    continue;
                if (!nodes.get(0).asString().equals("按类型"))
                    continue;
                nodes = ul.sel("li/a");
                for (JXNode node : nodes) {
                    Element element = node.asElement();
                    String title = element.text();
                    if ("全部".equals(title))
                        continue;
                    String href = element.attr("href");
                    if (href == null && href.isEmpty())
                        continue;
                    href = Util.dealWithUrl(href, url, HOST);
                    titles.add(new Title(title, href));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return titles;
    }

    @Override
    public String name() {
        return "FY影视";
    }

    @Override
    public ListMove list(String url) {
        ListMove listMove = new ListMove();
        List<Detial> detials = new ArrayList();
        listMove.detials = detials;
        try {
            JXDocument jx = JXDocument.create(createConnection(url).get());
            List<JXNode> as = jx.selN("//div[@class='stui-vodlist__box']/a");
            for (JXNode a : as) {
                Element element = a.asElement();
                String href = element.attr("href");
                if (href == null || href.isEmpty())
                    continue;
                href = Util.dealWithUrl(href, url, HOST);
                String name = element.attr("title");
                String img = element.attr("data-original");
                img = Util.dealWithUrl(img, url, HOST);
                Detial detial = new Detial();
                detial.detialUrl = href;
                detial.name = name;
                detial.img = img;
                detial.platformas = name();
                detials.add(detial);
            }
            as = jx.selN("//ul[@class='stui-page text-center clearfix']/li/a");
            for (JXNode a : as) {
                Element element = a.asElement();
                String text = element.text();
                if ("下一页".equals(text)) {
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
        try {
            JXDocument jx = JXDocument.create(createConnection(detial.detialUrl).get());
            List<JXNode> nodes = jx.selN("//a[@class='fzz']");
            if (nodes.size() <= 0)
                return false;
            List<Detial.DetailPlayUrl> playUrls = new ArrayList();
            detial.playUrls = playUrls;
            for (JXNode node : nodes) {
                Element element = node.asElement();
                String href = element.attr("href");
                if (href == null || href.isEmpty())
                    continue;
                href = Util.dealWithUrl(href, detial.detialUrl, HOST);
                String text = element.text();
                Detial.DetailPlayUrl playUrl = new Detial.DetailPlayUrl();
                playUrl.href = href;
                playUrl.title = text;
                playUrls.add(playUrl);
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
                text = text.substring(text.indexOf("=") + 1);
                JSONObject json = new JSONObject(text);
                return Util.dealWithUrl(json.getString("url"), playUrl.href, HOST);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public List<Detial> search(String text) {
        String url = String.format("https://www.25ys.net/vod/search.html?wd=%s&submit=", text);
        List<Detial> detials = new ArrayList<>();
        while (true) {
            ListMove list = searchUrl(url);
            detials.addAll(list.detials);
            if (list.next == null || list.next.isEmpty())
                break;
            url = list.next;
        }
        return detials;
    }

    private ListMove searchUrl(String url) {
        ListMove listMove = new ListMove();
        List<Detial> detials = new ArrayList<>();
        listMove.detials = detials;
        try {
            JXDocument jx = JXDocument.create(createConnection(url).get());
            List<JXNode> as = jx.selN("//div[@class='thumb']/a");
            for (JXNode a : as) {
                Element element = a.asElement();
                String href = element.attr("href");
                if (href == null || href.isEmpty())
                    continue;
                href = Util.dealWithUrl(href, url, HOST);
                String title = element.attr("title");
                String img = element.attr("data-original");
                img = Util.dealWithUrl(img, url, HOST);
                Detial detial = new Detial();
                detial.detialUrl = href;
                detial.name = title;
                detial.img = img;
                detial.platformas = name();
                detials.add(detial);
            }
            as = jx.selN("//ul[@class='stui-page text-center clearfix']/li/a");
            for (JXNode a : as) {
                Element element = a.asElement();
                String text = element.text();
                if ("下一页".equals(text)) {
                    listMove.next = Util.dealWithUrl(element.attr("href"), url, HOST);
                    if (url.equals(listMove.next))
                        listMove.next = "";
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listMove;
    }
}
