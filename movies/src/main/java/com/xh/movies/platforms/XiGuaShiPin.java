package com.xh.movies.platforms;

import android.util.Log;

import com.xh.movies.Base64;
import com.xh.movies.Util;
import com.xh.paser.AbsPlatform;
import com.xh.paser.Detial;
import com.xh.paser.ListMove;
import com.xh.paser.Title;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.nodes.Element;
import org.seimicrawler.xpath.JXDocument;
import org.seimicrawler.xpath.JXNode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XiGuaShiPin extends AbsPlatform {
    private static final String TAG = "XiGuaShiPin";
    private static final String HOST = "https://www.ixigua.com";
    private static final String COOKIE = "MONITOR_WEB_ID=c2a9d1e9-a4d0-4c77-a7f5-e25af42e28f1; ttcid=e6b8995b6fe344289c41d682252e8e4b24; _tea_utm_cache_2285={%22utm_source%22:%22sogou_lvideo%22%2C%22utm_medium%22:%22sogou_referral%22%2C%22utm_campaign%22:%22cooperation%22}; ixigua-a-s=1; BD_REF=1; __ac_nonce=060ffa19700328d7db0be; __ac_signature=_02B4Z6wo00f0134hmxAAAIDBTneshM3KPaN-BZ-AAL-D9b; ttwid=1%7CswI_LMhLObZQ_OXEOOGQ2xzaAZfVRBOJI4ggXbjCvWs%7C1627365817%7Cb80acbe7e98b65944fced7f5b5173792b794570e2a3192c755a96092262da982";

    @Override
    public List<Title> types() {
        String url = "https://www.ixigua.com/cinema/filter/dianshiju/";
        List<Title> titles = new ArrayList<>();
        try {
            JXDocument jx = JXDocument.create(createConnection(url).header("cookie", COOKIE).get());
            List<JXNode> nodes = jx.selN("//div[@class='lvideo-category__content']/ul/li/a");
            for (JXNode node : nodes) {
                Element element = node.asElement();
                String title = element.text();
                if (title.equals("全部"))
                    continue;
                titles.add(new Title(title, Util.dealWithUrl(element.attr("href"), url, HOST)));
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
            JXDocument jx = JXDocument.create(createConnection(url).header("cookie", COOKIE).get());
            List<JXNode> nodes = jx.selN("//div[@class='lvideo-category__content']/div");
            ///ul/li/a
            if (nodes.size() > 1)
                nodes = nodes.get(1).sel("ul/li/a");
            else if (nodes.size() > 0)
                nodes = nodes.get(0).sel("ul/li/a");
            for (JXNode node : nodes) {
                Element element = node.asElement();
                String title = element.text();
                if (title.equals("全部"))
                    continue;
                titles.add(new Title(title, Util.dealWithUrl(element.attr("href"), url, HOST)));
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
//        <script id="SSR_HYDRATED_DATA">
        ListMove listMove = new ListMove();
        List<Detial> detials = new ArrayList<Detial>();
        listMove.detials = detials;
        try {
            JXDocument jx = JXDocument.create(createConnection(url).header("cookie", COOKIE).get());
            List<JXNode> as = jx.selN("//script[@id='SSR_HYDRATED_DATA']/text()");
            if (as.size() > 0) {
                Pattern p = Pattern.compile("[^{]*(.*)");
                Matcher matcher = p.matcher(as.get(0).asString());
                if (matcher.find()) {
                    String text = matcher.group(1);
                    text = text.replaceAll("undefined", "null");
                    JSONObject object = new JSONObject(text);
                    JSONArray AlbumInCategory = object.getJSONArray("AlbumInCategory");
                    for (int i = 0; i < AlbumInCategory.length(); i++) {
                        object = AlbumInCategory.getJSONObject(i);
                        JSONArray albumList = object.getJSONArray("albumList");
                        for (int j = 0; j < albumList.length(); j++) {
                            object = albumList.getJSONObject(j);
                            JSONArray coverList = object.getJSONArray("coverList");
                            String img = "";
                            if (coverList.length() > 0) {
                                img = coverList.getJSONObject(0).getString("url");
                            }
                            Detial detial = new Detial();
                            detial.platformas = name();
                            detial.img = Util.dealWithUrl(img, url, HOST);
                            detial.name = object.getString("title");
                            detial.detialUrl = String.format("https://www.ixigua.com/%s", object.getString("albumId"));
                            detials.add(detial);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listMove;
    }

    @Override
    public boolean playDetail(Detial detial) {
        Log.e(TAG, detial.detialUrl);
        try {
            JXDocument jx = JXDocument.create(createConnection(detial.detialUrl).timeout(3000).header("cookie", COOKIE).get());
            List<JXNode> nodes = jx.selN("//script[@id='SSR_HYDRATED_DATA']/text()");
            if (nodes.size() > 0) {
                Pattern p = Pattern.compile("[^{]*(.*)");
                Matcher matcher = p.matcher(nodes.get(0).asString());
                if (matcher.find()) {
                    String text = matcher.group(1);
                    text = text.replaceAll("undefined", "null");
                    JSONObject object = new JSONObject(text);
                    object = object.getJSONObject("anyVideo");
                    object = object.getJSONObject("gidInformation");
                    object = object.getJSONObject("packerData");
                    object = object.getJSONObject("videoResource");
                    List<Detial.DetailPlayUrl> playUrls = null;
                    try {
                        object = object.getJSONObject("normal");
                        object = object.getJSONObject("video_list");
                        Iterator<String> iterator = object.keys();
                        playUrls = new ArrayList<>();
                        while (iterator.hasNext()) {
                            String key = iterator.next();
                            JSONObject jsonObject = object.getJSONObject(key);
                            Detial.DetailPlayUrl playUrl = new Detial.DetailPlayUrl();
                            playUrl.title = jsonObject.getString("definition");
                            playUrl.href = new String(Base64.decode(jsonObject.getString("main_url"), 0));
                            playUrls.add(playUrl);
                        }
                    } catch (JSONException e) {
                        try {
                            object = object.getJSONObject("dash_120fps");
                            object = object.getJSONObject("dynamic_video");
                            String href = object.getString("main_url");
                            Detial.DetailPlayUrl playUrl = new Detial.DetailPlayUrl();
                            playUrl.title = "测试";
                            playUrl.href = new String(Base64.decode(href, 0));
                            playUrls.add(playUrl);
                        } catch (JSONException jsonException) {
                            object = object.getJSONObject("normal_6min");
                            object = object.getJSONObject("video_list");
                            Iterator<String> iterator = object.keys();
                            playUrls = new ArrayList<>();
                            while (iterator.hasNext()) {
                                String key = iterator.next();
                                JSONObject jsonObject = object.getJSONObject(key);
                                Detial.DetailPlayUrl playUrl = new Detial.DetailPlayUrl();
                                playUrl.title = jsonObject.getString("definition");
                                playUrl.href = new String(Base64.decode(jsonObject.getString("main_url"), 0));
                                playUrls.add(playUrl);
                            }
                        }
                    }
                    detial.playUrls = playUrls;
                    return playUrls.size() > 0;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public String play(Detial.DetailPlayUrl playUrl) {
        Log.e(TAG, "" + playUrl.href);
        return playUrl.href;
    }

    @Override
    public List<Detial> search(String world) {
        List<Detial> detials = new ArrayList<>();
        String url = String.format("https://www.ixigua.com/search/%s/", world);
        try {
            JXDocument jx = JXDocument.create(createConnection(url).header("cookie", COOKIE).get());
            List<JXNode> nodes = jx.selN("//script[@id='SSR_HYDRATED_DATA']/text()");
            if (nodes.size() > 0) {
                Pattern p = Pattern.compile("[^{]*(.*)");
                Matcher matcher = p.matcher(nodes.get(0).asString());
                if (matcher.find()) {
                    String text = matcher.group(1);
                    text = text.replaceAll("undefined", "null");
                    JSONObject object = new JSONObject(text);
                    object = object.getJSONObject("complexSearch");
                    JSONArray array = object.getJSONArray("data");
                    object = array.getJSONObject(0).getJSONObject("data");
                    array = object.getJSONArray("display");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject a = array.getJSONObject(i);
                        String name = a.getString("title");
                        String img = null;
                        try {
                            img = new JSONObject(a.getString("extra")).getString("album_image");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        String score = a.getString("rating");
                        a = a.getJSONObject("episode_link");
                        JSONArray asc_link = a.getJSONArray("asc_link");
                        if (asc_link.length() <= 0)
                            asc_link = a.getJSONArray("desc_link");
                        String href = "";
                        if (asc_link.length() > 0) {
                            a = asc_link.getJSONObject(0);
                            String album_id = a.getString("album_id");
                            String id = a.getString("id");
                            href = String.format("https://www.ixigua.com/%s?id=%s", album_id, id);
                        }
                        Detial detial = new Detial();
                        detial.img = img;
                        detial.name = name;
                        detial.score = score;
                        detial.detialUrl = href;
                        detial.platformas = name();
                        detials.add(detial);
//                        System.out.println(String.format("name:%s,img:%s,score:%s,href:%s", name, img, score, href));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return detials;
    }
}
