package com.xh.movies.platforms;

import com.xh.movies.Util;
import com.xh.paser.AbsPlatform;
import com.xh.paser.Detial;
import com.xh.paser.ListMove;
import com.xh.paser.Title;

import org.jsoup.nodes.Element;
import org.seimicrawler.xpath.JXDocument;
import org.seimicrawler.xpath.JXNode;

import java.util.ArrayList;
import java.util.List;

public class BT4KYingYuan extends AbsPlatform {
    private static final String HOST = "http://www.bt4kyy.cc";

    @Override
    public List<Title> types() {
        String url = "http://www.bt4kyy.cc/";
        List<Title> titles = new ArrayList<>();
        try {
            JXDocument jx = JXDocument.create(createConnection(url).get());
            List<JXNode> nodes = jx.selN("//div[@class='nav mb_none']/span/a");
            for (JXNode node : nodes) {
                String href = node.asElement().attr("href");
                if (href == null || href.isEmpty())
                    continue;
                String title = "";
                List<JXNode> spans = node.sel("span/text()");
                if (spans.size() > 0)
                    title = spans.get(0).asString();
                titles.add(new Title(title, Util.dealWithUrl(href, url, HOST)));
            }
            nodes = jx.selN("//ul[@class='clearfix']/li/a");
            for (JXNode node : nodes) {
                Element element = node.asElement();
                String href = element.attr("href");
                if (href == null || href.isEmpty())
                    continue;
                if (href.contains("/l/")) {
                    String text = element.text();
                    titles.add(new Title(text, Util.dealWithUrl(href, url, HOST)));
                }
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
            List<JXNode> nodes = jx.selN("//div[@class='case']/div");
            if (nodes.size() > 0) {
                nodes = nodes.get(0).sel("div/ul/li/a");
                for (JXNode node : nodes) {
                    Element element = node.asElement();
                    String href = element.attr("href");
                    if (href == null || href.isEmpty())
                        continue;
                    String text = element.text();
                    if(text.equals("全部"))
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
    public String name() {
        return "BT4K影院";
    }

    @Override
    public ListMove list(String url) {
        ListMove listMove = new ListMove();
        List<Detial> detials =new ArrayList<>();
        listMove.detials = detials;
        try {
            JXDocument jx = JXDocument.create(createConnection(url).get());
            List<JXNode> divs = jx.selN("//div[@class='li_all']");
            for (JXNode div : divs) {
                List<JXNode> nodes = div.sel("div[@class='li_img']/a/@href");
                String href = "";
                if (nodes.size() > 0)
                    href = nodes.get(0).asString();
                href = Util.dealWithUrl(href, url, HOST);
                nodes = div.sel("div[@class='li_img']/a/img/@src");
                String img = "";
                if (nodes.size() > 0)
                    img = nodes.get(0).asString();
                img = Util.dealWithUrl(img, url, HOST);
                nodes = div.sel("div[@class='li_text']/p[@class='name']/a/text()");
                String name ="";
                if(nodes.size() > 0)
                    name=nodes.get(0).asString();
                nodes = div.sel("div[@class='li_text']/p[@class='actor']/a/text()");
                String score = "";
                if(nodes.size()>0)
                    score=nodes.get(0).asString();
                Detial detial =new Detial();
                detial.detialUrl = href;
                detial.img = img;
                detial.name = name;
                detial.score = score;
                detial.platformas = name();
                detials.add(detial);
            }
            divs = jx.selN("//div[@class='page clearfix mb_none']/a");
            for(JXNode node:divs){
                Element element = node.asElement();
                String text =element.text();
                if(text.equals("下页")){
                    listMove.next = Util.dealWithUrl(element.attr("href"),url,HOST);
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
            List<JXNode> divs = jx.selN("//div[@id='stab11']/div");
            for(JXNode div :divs){
                String title = "";
                List<JXNode> nodes = div.sel("div/font/text()");
                if(nodes.size()>0)
                    title = nodes.get(0).asString();
                nodes = div.sel("ul/div/ul/li/a");
                for (JXNode node:nodes){
                    Element element = node.asElement();
                    String href = element.attr("href");
                    String text = element.text();
                    Detial.DetailPlayUrl playUrl =new Detial.DetailPlayUrl();
                    playUrl.href = Util.dealWithUrl(href,detial.detialUrl,HOST);
                    playUrl.title = title+" "+text;
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
            List<JXNode> nodes = jx.selN("//div[@class='player']/script/text()");
            for(JXNode node:nodes){
                String text = node.asString();
                if(text.contains(".m3u8")){
                    return text.split("var now=\"")[1].split("\";var pn=")[0];
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public List<Detial> search(String text) {
        String url ="http://www.bt4kyy.cc/search1.php";
        List<Detial> detials = new ArrayList<Detial>();
        try {
            JXDocument jx = JXDocument.create(createConnection(url).data("searchword",text).post());
            ListMove listMove = search(jx,url);
            detials.addAll(listMove.detials);
            while(listMove.next!=null&&!listMove.next.isEmpty()&&!url.equals(listMove.next)){
                url = listMove.next;
                listMove = search(JXDocument.create(createConnection(listMove.next).get()),listMove.next);
                detials.addAll(listMove.detials);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return detials;
    }

    private ListMove search(JXDocument jx,String url){
        ListMove listMove = new ListMove();
        List<Detial> detials =new ArrayList<>();
        listMove.detials = detials;
        try {
            List<JXNode> divs = jx.selN("//div[@class='li_all']");
            for (JXNode div : divs) {
                List<JXNode> nodes = div.sel("div[@class='li_img']/a/@href");
                String href = "";
                if (nodes.size() > 0)
                    href = nodes.get(0).asString();
                href = Util.dealWithUrl(href, url, HOST);
                nodes = div.sel("div[@class='li_img']/a/img/@src");
                String img = "";
                if (nodes.size() > 0)
                    img = nodes.get(0).asString();
                img = Util.dealWithUrl(img, url, HOST);
                nodes = div.sel("div[@class='li_text']/p[@class='name']/a/text()");
                String name ="";
                if(nodes.size() > 0)
                    name=nodes.get(0).asString();
                nodes = div.sel("div[@class='li_text']/p[@class='actor']/a/text()");
                String score = "";
                if(nodes.size()>0)
                    score=nodes.get(0).asString();
                Detial detial =new Detial();
                detial.detialUrl = href;
                detial.img = img;
                detial.name = name;
                detial.score = score;
                detial.platformas = name();
                detials.add(detial);
            }
            divs = jx.selN("//div[@class='page clearfix mb_none']/a");
            for(JXNode node:divs){
                Element element = node.asElement();
                String text =element.text();
                if(text.equals("下页")){
                    listMove.next = Util.dealWithUrl(element.attr("href"),url,HOST);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listMove;
    }
}
