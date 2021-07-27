package com.xh.play.platforms;

import android.util.Log;

import androidx.dynamicanimation.animation.SpringAnimation;

import com.nostra13.universalimageloader.utils.L;
import com.xh.play.entities.Detial;
import com.xh.play.entities.ListMove;
import com.xh.play.entities.Live;
import com.xh.play.entities.Title;
import com.xh.play.utils.Util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.seimicrawler.xpath.JXDocument;
import org.seimicrawler.xpath.JXNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DianYingMao extends AbsPlatform {
    private static final String HOTS = "https://www.mvcat.com";
    private static final String TAG = "DianYingMao";

    @Override
    public List<Title> types() {
        List<Title> titles = new ArrayList<>();
        titles.add(new Title("全部","https://www.mvcat.com/"));
        return titles;
    }

    @Override
    public List<Title> titles(String url) {
        List<Title> titles = new ArrayList<>();
        titles.add(new Title("动作", Util.dealWithUrl("//www.mvcat.com/movie/action/", url, HOTS)));
        titles.add(new Title("战争", Util.dealWithUrl("//www.mvcat.com/movie/war/", url, HOTS)));
        titles.add(new Title("科幻", Util.dealWithUrl("//www.mvcat.com/movie/sf/", url, HOTS)));
        titles.add(new Title("悬疑", Util.dealWithUrl("//www.mvcat.com/movie/suspense/", url, HOTS)));
        titles.add(new Title("喜剧", Util.dealWithUrl("//www.mvcat.com/movie/comedy/", url, HOTS)));
        titles.add(new Title("爱情", Util.dealWithUrl("//www.mvcat.com/movie/love/", url, HOTS)));
        titles.add(new Title("励志", Util.dealWithUrl("//www.mvcat.com/movie/spirit/", url, HOTS)));
        titles.add(new Title("动画", Util.dealWithUrl("//www.mvcat.com/movie/cartoon/", url, HOTS)));
        titles.add(new Title("惊悚", Util.dealWithUrl("//www.mvcat.com/movie/horror/", url, HOTS)));
        titles.add(new Title("犯罪", Util.dealWithUrl("//www.mvcat.com/movie/crime/", url, HOTS)));
        titles.add(new Title("情色", Util.dealWithUrl("//www.mvcat.com/movie/erotic/", url, HOTS)));
        titles.add(new Title("纪录", Util.dealWithUrl("//www.mvcat.com/movie/documentary/", url, HOTS)));
        titles.add(new Title("剧情", Util.dealWithUrl("//www.mvcat.com/movie/drama/", url, HOTS)));
        titles.add(new Title("cult", Util.dealWithUrl("//www.mvcat.com/movie/cult/", url, HOTS)));
        return titles;
    }


    @Override
    public ListMove list(String url) {
        ListMove listMove = list1(url);
        if (listMove.next != null && !listMove.next.isEmpty()) {
            ListMove next = list1(listMove.next);
            listMove.detials.addAll(next.detials);
            listMove.next = next.next;
        }
        Log.e(TAG, listMove.next);
        return listMove;
    }

    private ListMove list1(String url) {
        ListMove listMove = new ListMove();
        List<Detial> detials = new ArrayList<>();
        listMove.detials = detials;
        try {
            JXDocument jx = JXDocument.create(createConnection(url).get());
            List<JXNode> as = jx.selN("//div[@class='ipages columns clear']/a");
            for (JXNode a : as) {
                String href = a.asElement().attr("href");
                if (href == null || href.isEmpty())
                    continue;
                href = Util.dealWithUrl(href, url, HOTS);
                List<JXNode> nodes = a.sel("p[@class='poster']/@style");
                String img = "";
                if (nodes.size() > 0)
                    img = nodes.get(0).asString();
                try {
                    img = img.substring(img.indexOf("('") + 2, img.indexOf("')"));
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                img = Util.dealWithUrl(img, url, HOTS);
                nodes = a.sel("h3/text()");
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
            as = jx.selN("//div[@class='pages']/a");
            for (JXNode a : as) {
                Element element = a.asElement();
                if (element.text().equals("下一页")) {
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

    public List<Live> live() {
        String url = "https://www.mvcat.com/live/";
        List<Live> detailPlayUrls = new ArrayList<>();
        try {
            Document document = createConnection(url).get();
            JXDocument jx = JXDocument.create(document);
            List<JXNode> lis = jx.selN("//ul[@class='tabs']/li");
            for (JXNode li : lis) {
                List<JXNode> nodes = li.sel("label/text()");
                if (nodes.size() <= 0)
                    continue;
                String name = nodes.get(0).asString();
                nodes = li.sel("div/a");
                if (nodes.size() <= 0)
                    continue;
                Live live = new Live();
                live.name = name;
                List<Detial.DetailPlayUrl> detailPlayUrls1 = new ArrayList<>();
                live.detailPlayUrls = detailPlayUrls1;
                for (JXNode node : nodes) {
                    Element element = node.asElement();
                    name = element.text();
                    String href = element.attr("href");
                    if (href == null)
                        continue;
                    href = Util.dealWithUrl(href, url, HOTS);
                    Detial.DetailPlayUrl detailPlayUrl = new Detial.DetailPlayUrl();
                    detailPlayUrl.title = name;
                    detailPlayUrl.href = href;
                    detailPlayUrls1.add(detailPlayUrl);
                }
                detailPlayUrls.add(live);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return detailPlayUrls;
    }

    @Override
    public String name() {
        return "电影猫";
    }
}
