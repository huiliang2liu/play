package com.xh.movies.platforms;

import android.util.Log;

import com.xh.movies.Base64;
import com.xh.movies.PlatformsManager;
import com.xh.movies.Util;
import com.xh.paser.AbsPlatform;
import com.xh.paser.Detial;
import com.xh.paser.IVip;
import com.xh.paser.ListMove;
import com.xh.paser.Title;
import com.xh.paser.VipParsListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.nodes.Element;
import org.seimicrawler.xpath.JXDocument;
import org.seimicrawler.xpath.JXNode;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IQiYi extends AbsPlatform {
    private static final String TAG = "IQiYi";
    private static final String HOST = "https://www.iq.com";

    @Override
    protected Connection createConnection(String url) {
        Connection connection = super.createConnection(url);
        connection.header("accept-language", "zh-CN,zh;q=0.9");
        return connection;
    }

    @Override
    public List<Title> types() {
        String url = "https://www.iqiyi.com/dianshiju";
        List<Title> titles = new ArrayList<>();
        try {
            JXDocument jx = JXDocument.create(createConnection(url).get());
            List<JXNode> as = jx.selN("//div[@class='nav-channel']/a");
            for (JXNode a : as) {
                Element element = a.asElement();
                String name = element.text();
                if (name.equals("推荐") || name.equals("恋恋剧场") || name.equals("儿童"))
                    continue;
                String href = element.attr("href");
                href = Util.dealWithUrl(href, url, HOST);
                titles.add(new Title(name, href));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return titles;
    }

    @Override
    public List<Title> titles(String url) {
        Log.e("titles", url);
        List<Title> titles = new ArrayList<>();
        try {
            JXDocument jx = JXDocument.create(createConnection(url).get());
            List<JXNode> jss = jx.selN("//ul[@class='ch-classify__list']/li/a/@href");
            Log.e("====", "" + jss.size());
            if (jss.size() <= 0)
                return titles;
            String next = jss.get(0).asString();
            next = Util.dealWithUrl(next, url, HOST);
            Log.e("next", next);
            jx = JXDocument.create(createConnection(next).get());
            List<JXNode> as = jx.selN("//div[@class='category-class category-class1']/span");
            Log.e("====aa", "" + as.size());
            if (as.size() <= 0)
                return titles;
            String name = null;
            for (JXNode a : as) {
                Element element = a.asElement();
                String clazz = element.attr("class");
                if (clazz.contains("selected")) {
                    as = a.sel("span/text()");
                    if (as.size() <= 0)
                        return titles;
                    name = as.get(0).asString();
                    break;
                }
            }
            Log.e("name", "" + name);
            if (name == null || name.isEmpty())
                return titles;
            as = jx.selN("//*[@id='block-B']");
            Log.e("as size", "" + as.size());
            if (as.size() <= 0)
                return titles;
            Element div = as.get(0).asElement();
            JSONArray array = new JSONArray(div.attr(":channel-list"));
            JSONObject object = null;
            int cid = -1000;
            for (int i = 0; i < array.length(); i++) {
                object = array.optJSONObject(i);
                if (name.equals(object.optString("name"))) {
                    cid = object.optInt("cid");
                    break;
                }
            }
            if (cid == -1000)
                return titles;
            Log.e("cid", cid + "");
            array = new JSONArray(div.attr(":first-category-list"));
            int typeId = -1;
            for (int i = 0; i < array.length(); i++) {
                object = array.optJSONObject(i);
                String typeName = object.optString("name");
                if ("类型".equals(typeName) || "风格".equals(typeName)) {
                    typeId = object.optInt("id");
                    array = object.optJSONArray("child");
                    break;
                }

            }
            if (typeId == -1 || array == null || array.length() <= 0)
                return titles;
            for (int i = 0; i < array.length(); i++) {
                object = array.optJSONObject(i);
                name = object.optString("name");
                int id = object.optInt("id");
                String href = String.format("https://pcw-api.iqiyi.com/search/recommend/list?channel_id=%s&data_type=1&mode=24&page_id=1&ret_num=48&three_category_id=%s;must",
                        cid, id);
                Log.e("href", href);
                titles.add(new Title(name, href));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return titles;
    }

    @Override
    public String name() {
        return "爱奇艺";
    }

    @Override
    public ListMove list(String url) {
        ListMove listMove = new ListMove();
        List<Detial> detials = new ArrayList<>();
        listMove.detials = detials;
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.setRequestMethod("GET");
            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(false);
            connection.setRequestProperty("accept-language", "zh-CN,zh;q=0.9");
            connection.connect();
            InputStream is = connection.getInputStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int len;
            byte buff[] = new byte[1024 * 1024];
            while ((len = is.read(buff)) != -1)
                baos.write(buff, 0, len);
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            JSONObject object = new JSONObject(new String(baos.toByteArray()));
            object = object.optJSONObject("data");
            if (object == null)
                return listMove;
            JSONArray array = object.optJSONArray("list");
            if (array == null || array.length() <= 0)
                return listMove;
            for (int i = 0; i < array.length(); i++) {
                object = array.optJSONObject(i);
                String name = object.optString("name");
                String img = object.optString("imageUrl");
                Detial detial = new Detial();
                detial.name = name;
                detial.img = Util.dealWithUrl(img, url, HOST);
                detial.detialUrl = object.optString("playUrl");
                detials.add(detial);
            }
//            https://pcw-api.iq.com/api/albumList?deviceId=e17ee47ca8cdf625ff78c3f6d48f5fed&platformId=3&langCode=zh_cn&modeCode=hk&pn=0&ps=30&chnId=%s&tagValues=%s%sCs4
            if (detials.size() >= 40) {
                Pattern pattern = Pattern.compile("page_id=([0-9]+)");
                Matcher matcher = pattern.matcher(url);
                if (matcher.find()) {
                    int page = Integer.valueOf(matcher.group(1));
                    listMove.next = url.replace("page_id=" + page, "page_id=" + (page + 1));
                }
            }
            try {
                baos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listMove;
    }

    @Override
    public boolean playDetail(Detial detial) {
        String url = detial.detialUrl;
        List<Detial.DetailPlayUrl> playUrls = new ArrayList();
        detial.playUrls = playUrls;
        try {
            JXDocument jx = JXDocument.create(createConnection(url).get());
            List<JXNode> as = jx.selN("//meta[@name='apple-itunes-app']/@content");
            if (as != null && as.size() > 0) {
                String content = as.get(0).asString();
                Log.e(TAG, content);
                Pattern pattern = Pattern.compile("aid=([^&]*)");
                Matcher matcher = pattern.matcher(content);
                String id = null;
                if (matcher.find()) {
                    id = matcher.group(1);
                }
                try {
                    if (id != null && !id.isEmpty()) {
                        String href = String.format("https://pcw-api.iqiyi.com/albums/album/avlistinfo?aid=%s&page=1&size=10000&callback=jsonp_1629872661377_3645", id);
                        Log.e(TAG, href);
                        try {
                            HttpURLConnection connection = (HttpURLConnection) new URL(href).openConnection();
                            connection.setDoOutput(false);
                            connection.setDoInput(true);
                            connection.setRequestMethod("GET");
                            connection.setConnectTimeout(5000);
                            connection.setUseCaches(false);
                            connection.setReadTimeout(5000);
                            connection.connect();
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            InputStream is = connection.getInputStream();
                            int len = 0;
                            byte[] buff = new byte[1024 * 1024];
                            while ((len = is.read(buff)) != -1)
                                baos.write(buff, 0, len);
                            try {
                                is.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            String text = new String(baos.toByteArray());
                            try {
                                baos.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            text = text.substring("try{ jsonp_1629872661377_3645(".length());
                            text = text.substring(0, text.length() - ");}catch(e){}".length());
                            JSONObject jsonObject = new JSONObject(text);
                            jsonObject = jsonObject.optJSONObject("data");
                            if (jsonObject != null) {
                                JSONArray epsodelist = jsonObject.optJSONArray("epsodelist");
                                if (epsodelist != null && epsodelist.length() > 0) {
                                    for (int i = 0; i < epsodelist.length(); i++) {
                                        jsonObject = epsodelist.optJSONObject(i);
                                        Detial.DetailPlayUrl playUrl = new Detial.DetailPlayUrl();
                                        playUrl.href = jsonObject.optString("playUrl");
                                        playUrl.title = jsonObject.optString("name");
                                        playUrls.add(playUrl);
                                    }
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (playUrls.size() <= 0) {
                Detial.DetailPlayUrl playUrl = new Detial.DetailPlayUrl();
                playUrl.href = url;
                playUrl.title = detial.name;
                playUrls.add(playUrl);
            }
            Log.e("====", "" + playUrls.size());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static class Play {
        String url = "";
    }

    @Override
    public String play(Detial.DetailPlayUrl playUrl) {
//        return String.format("http://127.0.0.1:%s/movie?url=%s", PlatformsManager.PORT, new String(Base64.encode(playUrl.href.getBytes(), 0)));
        Log.e("playda", playUrl.href);
        IVip vip = PlatformsManager.vip;
        if (vip == null)
            return "";
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Play url = new Play();
        vip.parse(playUrl.href, new VipParsListener() {
            @Override
            public void onListener(String u) {
                if (u != null && !u.isEmpty()) {
                    url.url = u;
                    countDownLatch.countDown();
                }
            }
        });
        try {
            countDownLatch.await(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return url.url;
    }

    @Override
    public List<Detial> search(String text) {
        Log.e(TAG, text);
        List<Detial> detials = new ArrayList<>();
        String url = "https://so.iqiyi.com/so/q_" + text + "?source=suggest&sr=10018973839088498&ssrt=20210825152437686&ssra=10400ee5a0880a50edda49ae0096e084&s_sr=1&s_token=suggest_1#74cb7a5fb97c8f604f17853ff1f6e0ed#0#%E4%B8%AD%E5%9B%BD";
//                String.format("%s", URLEncoder.encode(text));
        Log.e(TAG, url);
        try {
            JXDocument jx = JXDocument.create(createConnection(url).get());
            List<JXNode> as = jx.selN("//div[@class='layout-main']/div/div/div/div/div/div/a");
            if (as.size() > 0) {
                for (JXNode a : as) {
                    Element element = a.asElement();
                    String href = element.attr("href");
                    href = Util.dealWithUrl(href, url, HOST);
                    if (!href.contains("/v_")) {
                        href = parsePlay(href);
                    }
                    String name = element.attr("title");
                    List<JXNode> imgs = a.sel("img/@src");
                    String img = "";
                    if (imgs.size() > 0)
                        img = imgs.get(0).asString();
                    Detial detial = new Detial();
                    detial.name = name;
                    detial.detialUrl = href;
                    detial.img = img;
                    detials.add(detial);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.e(TAG, "" + detials.size());
        return detials;
    }

    private String parsePlay(String url) {
        Log.e(TAG, url);
        try {
            JXDocument jx = JXDocument.create(createConnection(url).get());
            List<JXNode> href = jx.selN("//*[@id='block-BB']/div/div/a/@href");
            if (href != null && href.size() > 0)
                return Util.dealWithUrl(href.get(0).asString(), url, HOST);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return url;
    }

    @Override
    public boolean hasVip() {
        return true;
    }
}
