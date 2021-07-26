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

public class XiGuaShiPin implements IPlatform{
    private static final String HOST ="https://www.ixigua.com";
    @Override
    public List<Title> types() {
        String url  ="https://www.ixigua.com/cinema/filter/dianshiju/";
        List<Title> titles = new ArrayList<>();
        try {
            JXDocument jx = JXDocument.create(Jsoup.connect(url).get());
            List<JXNode> nodes = jx.selN("//div[@class='lvideo-category__content']/ul/li/a");
            for (JXNode node : nodes){
                Element element = node.asElement();
                String title = element.text();
                if(title.equals("全部"))
                    continue;
                titles.add(new Title(title, Util.dealWithUrl(element.attr("href"),url,HOST)));
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
            List<JXNode> nodes = jx.selN("//div[@class='lvideo-category__content']/div");
            ///ul/li/a
            if(nodes.size() > 1)
                nodes = nodes.get(1).sel("ul/li/a");
            else if(nodes.size() > 0)
                nodes = nodes.get(0).sel("ul/li/a");
            for (JXNode node : nodes){
                Element element = node.asElement();
                String title = element.text();
                if(title.equals("全部"))
                    continue;
                titles.add(new Title(title, Util.dealWithUrl(element.attr("href"),url,HOST)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return titles;
    }

    @Override
    public String name() {
        return "西瓜视频";
    }

    @Override
    public ListMove list(String url) {
        ListMove listMove =new ListMove();
        List<Detial> detials =new ArrayList<Detial>();
        listMove.detials =detials;
        try {
            JXDocument jx = JXDocument.create(Jsoup.connect(url).get());
            List<JXNode> as = jx.selN("//div[@class='lvideo-list']/div/a");
            for (JXNode a:as){
                Element element = a.asElement();
                String href =element.attr("href");
                String title =element.attr("title");
                String img = "";
                List<JXNode> nodes = a.sel("div/img/@data-src");
                if(nodes.size()>0)
                    img = nodes.get(0).asString();
                Detial detial = new Detial();
                detial.platformas = name();
                detial.img = Util.dealWithUrl(img,url,HOST);
                detial.name = title;
                detial.detialUrl = Util.dealWithUrl(href,url,HOST);
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
