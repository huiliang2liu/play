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

public class UUMeiJu implements IPlatform {
    private static final String HOST = "https://www.uumjw.com";
    private static final List<String> names = new ArrayList<>();

    static {
        names.add("美剧");
        names.add("日韩剧");
        names.add("泰剧");
        names.add("动漫");
        names.add("综艺");
    }

    @Override
    public List<Title> types() {
        String url = "https://www.uumjw.com/vtype/1.html";
        List<Title> titles = new ArrayList<>();
        try {
            JXDocument jx = JXDocument.create(Jsoup.connect(url).get());
            List<JXNode> nodes = jx.selN("//li[@class='grid-item']/a");
            for (JXNode node : nodes) {
                Element element = node.asElement();
                String name = element.attr("title");
                if (names.indexOf(name) < 0)
                    continue;
                String href = element.attr("href");
                if (href == null || href.isEmpty())
                    continue;
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
            JXDocument jx = JXDocument.create(Jsoup.connect(url).get());
            List<JXNode> divs = jx.selN("//div[@class='scroll-content']");
            for (JXNode div : divs) {
                List<JXNode> nodes = div.sel("a/text()");
                if (nodes.size() <= 0 || !nodes.get(0).asString().equals("全部剧情"))
                    continue;
                nodes = div.sel("div/a");
                for (JXNode node : nodes) {
                    Element element = node.asElement();
                    String href = element.attr("href");
                    if (href == null || href.isEmpty())
                        continue;
                    href = Util.dealWithUrl(href, url, HOST);
                    String title = element.attr("title");
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
        return "uu美剧";
    }

    @Override
    public ListMove list(String url) {
        ListMove listMove = new ListMove();
        List<Detial> detials = new ArrayList();
        listMove.detials = detials;
        try {
            JXDocument jx = JXDocument.create(Jsoup.connect(url).get());
            List<JXNode> divs = jx.selN("//div[@class='module-item']");
            for (JXNode div : divs) {
                List<JXNode> nodes = div.sel("div/a");
                if (nodes.size() <= 0)
                    continue;
                Element element = nodes.get(0).asElement();
                String title = element.attr("title");
                String href = element.attr("href");
                if (href == null || href.isEmpty())
                    continue;
                href = Util.dealWithUrl(href, url, HOST);
                nodes = div.sel("div/div/img/@data-src");
                String img = "";
                if (nodes.size() > 0)
                    img = nodes.get(0).asString();
                Detial detial = new Detial();
                detial.detialUrl = href;
                detial.name = title;
                detial.platformas = name();
                detial.img = img;
                detials.add(detial);
            }
            divs = jx.selN("//div[@id='page']/a");
            for (JXNode node : divs) {
                Element element = node.asElement();
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
            JXDocument jx = JXDocument.create(Jsoup.connect(detial.detialUrl).get());
            List<JXNode> scroll_contents = jx.selN("//div[@class='scroll-content']");
            List<JXNode> titles = jx.selN("//div[@class='module-tab-content']/div/span/text()");
            int max = scroll_contents.size() > titles.size() ? scroll_contents.size() : titles.size();
            List<Detial.DetailPlayUrl> playUrls = new ArrayList<>();
            for (int i = 0; i < max; i++) {
                String name = titles.get(i).asString();
                List<JXNode> as = scroll_contents.get(i).sel("a");
                for (JXNode a : as) {
                    Element element = a.asElement();
                    String href = element.attr("href");
                    if (href == null || href.isEmpty())
                        continue;
                    href = Util.dealWithUrl(href, detial.detialUrl, HOST);
                    String title = "";
                    List<JXNode> nodes = a.sel("span/text()");
                    if (nodes.size() > 0)
                        title = name + " " + nodes.get(0).asString();
                    Detial.DetailPlayUrl detailPlayUrl = new Detial.DetailPlayUrl();
                    detailPlayUrl.href = href;
                    detailPlayUrl.title = title;
                    playUrls.add(detailPlayUrl);
                }
            }
            detial.playUrls = playUrls;
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public String play(Detial.DetailPlayUrl playUrl) {
        try {
            JXDocument jx = JXDocument.create(Jsoup.connect(playUrl.href).get());
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
        String url = String.format("https://www.uumjw.com/index.php/vsearch.html?wd=%s", text);
        List<Detial> detials = new ArrayList<>();
        try {
            JXDocument jx = JXDocument.create(Jsoup.connect(url).get());
            List<JXNode> divs = jx.selN("//div[@class='module-item-pic']");
            for (JXNode div : divs) {
                List<JXNode> nodes = div.sel("a/@href");
                if (nodes.size() <= 0)
                    continue;
                String href = nodes.get(0).asString();
                if (href == null || href.isEmpty())
                    continue;
                href = Util.dealWithUrl(href, url, HOST);
                nodes = div.sel("img");
                String name = "";
                String img = "";
                if (nodes.size() > 0) {
                    Element element = nodes.get(0).asElement();
                    name = element.attr("alt");
                    img = element.attr("data-src");
                    img = Util.dealWithUrl(img, url, HOST);
                }
                Detial detial = new Detial();
                detial.img = img;
                detial.platformas = name();
                detial.name = name;
                detial.detialUrl = href;
                detials.add(detial);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return detials;
    }
}
