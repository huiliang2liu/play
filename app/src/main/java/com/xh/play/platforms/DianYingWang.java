package com.xh.play.platforms;

import com.xh.play.entities.Detial;
import com.xh.play.entities.ListMove;
import com.xh.play.entities.Title;
import com.xh.play.utils.Util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.seimicrawler.xpath.JXDocument;
import org.seimicrawler.xpath.JXNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DianYingWang extends AbsPlatform {
    private static final String HOST = "https://www.6vw.cc";

    @Override
    public List<Title> types() {
        List<Title> titles = new ArrayList<>();
        titles.add(new Title("所有", "https://www.6vw.cc/dy2/"));
        return titles;
    }

    @Override
    public List<Title> titles(String url) {
        List<Title> titles = new ArrayList<>();
        try {
            JXDocument jx = JXDocument.create(createConnection(url).get());
            List<JXNode> as = jx.selN("//div[@class='menutv']/ul/li/a");
            for (JXNode a : as) {
                Element element = a.asElement();
                String href = element.attr("href");
                if (href == null || href.isEmpty())
                    continue;
                href = Util.dealWithUrl(href, url, HOST);
                String name = element.text();
                if (name.equals("首页"))
                    continue;
                titles.add(new Title(name, href));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return titles;
    }

    @Override
    public String name() {
        return "电影网";
    }

    @Override
    public ListMove list(String url) {
        ListMove listMove = new ListMove();
        List<Detial> detials = new ArrayList<>();
        listMove.detials = detials;
        try {
            JXDocument jx = JXDocument.create(createConnection(url).get());
            List<JXNode> lis = jx.selN("//div[@class='listBox']/ul/li");
            for (JXNode ls : lis) {
                List<JXNode> nodes = ls.sel("div[@class='listimg']/a/@href");
                if (nodes.size() <= 0)
                    continue;
                String href = nodes.get(0).asString();
                href = Util.dealWithUrl(href, url, HOST);
                String img = "";
                nodes = ls.sel("div[@class='listimg']/a/img/src");
                if (nodes.size() > 0)
                    img = Util.dealWithUrl(nodes.get(0).asString(), url, HOST);
                nodes = ls.sel("div[@class='listInfo']/h3/a/text()");
                String name = "";
                if (nodes.size() > 0)
                    name = nodes.get(0).asString();
                Detial detial = new Detial();
                detial.name = name;
                detial.platformas = name();
                detial.detialUrl = href;
                detial.img = img;
                detials.add(detial);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listMove;
    }

    @Override
    public boolean playDetail(Detial detial) {
        return false;
    }

    @Override
    public String play(Detial.DetailPlayUrl playUrl) {
        return null;
    }

    @Override
    public List<Detial> search(String text) {
        return null;
    }
}
