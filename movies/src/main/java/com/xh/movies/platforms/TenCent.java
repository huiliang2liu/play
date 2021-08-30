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
import org.json.JSONObject;
import org.jsoup.nodes.Element;
import org.seimicrawler.xpath.JXDocument;
import org.seimicrawler.xpath.JXNode;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TenCent extends AbsPlatform {
    private static final String HOST = "https://v.qq.com";

    @Override
    public List<Title> types() {
        List<Title> titles = new ArrayList<>();
        titles.add(new Title("电视剧", "https://v.qq.com/channel/tv"));
        titles.add(new Title("电影", "https://v.qq.com/channel/movie"));
        titles.add(new Title("综艺", "https://v.qq.com/channel/variety"));
        titles.add(new Title("动漫", "https://v.qq.com/channel/cartoon"));
        titles.add(new Title("少儿", "https://v.qq.com/channel/child"));
        titles.add(new Title("纪录片", "https://v.qq.com/channel/doco"));
//        titles.add(new Title("少儿", "https://v.qq.com/channel/child"));
//        titles.add(new Title("电视剧", "https://v.qq.com/channel/tv"));
        return titles;
    }

    @Override
    public List<Title> titles(String url) {
        List<Title> titles = new ArrayList<>();
        String end = "&offset=0&pagesize=30";
        try {
//            /channel / tv ? listpage = 1 & channel = tv & feature = 7
//            https://v.qq.com/x/bu/pagesheet/list?append=1&channel=tv&feature=7
            JXDocument jx = JXDocument.create(createConnection(url).get());
            List<JXNode> as = jx.selN("//div[@class='site_subnav_inner']/a");
            for (JXNode a : as) {
                Element element = a.asElement();
                String title = element.text();
                if (title.endsWith("精选") || title.endsWith("片库"))
                    continue;
                String href = element.attr("href");
                String hrefs[] = href.split("\\?");
                if (hrefs.length < 2)
                    continue;
//                https://v.qq.com/x/bu/pagesheet/list?append=1&channel=tv&iarea=815&listpage=2&offset=600&pagesize=30
                href = hrefs[1];
                href += end;
                href = "https://v.qq.com/x/bu/pagesheet/list?" + href;
                titles.add(new Title(title, Util.dealWithUrl(href, url, HOST)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return titles;
    }

    @Override
    public String name() {
        return "腾讯";
    }

    @Override
    public ListMove list(String url) {
        ListMove move = new ListMove();
        List<Detial> detials = new ArrayList();
        move.detials = detials;
        try {
            JXDocument jx = JXDocument.create(createConnection(url).get());
            List<JXNode> as = jx.selN("//div[@class='mod_figure mod_figure_v_default mod_figure_list_box']/div/a");
            for (JXNode a : as) {
                Element element = a.asElement();
                String href = element.attr("href");
                href = Util.dealWithUrl(href, url, HOST);
                String name = element.attr("title");
                JXNode i = a.sel("img[1]").get(0);
                String img = i.asElement().attr("src");
                Detial detial = new Detial();
                detial.detialUrl = href;
                detial.name = name;
                detial.text = element.attr("data-float");
                detial.img = Util.dealWithUrl(img, url, HOST);
                detial.platformas = name();
                detials.add(detial);
            }
            if (detials.size() >= 30) {
                Pattern pattern = Pattern.compile("listpage=([0-9]+)");
                Matcher matcher = pattern.matcher(url);
                String next = url;
                if (matcher.find()) {
                    int listpage = Integer.valueOf(matcher.group(1));
                    next = next.replace("listpage=" + listpage, "listpage=" + (listpage + 1));
                }
                pattern = Pattern.compile("offset=([0-9]+)");
                matcher = pattern.matcher(url);
                if (matcher.find()) {
                    int listpage = Integer.valueOf(matcher.group(1));
                    next = next.replace("offset=" + listpage, "offset=" + (listpage + 30));
                }
                move.next = next;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
//        String end = "&offset=0&pagesize=30";
//        listpage = 1
//        https://v.qq.com/x/bu/pagesheet/list?append=1&channel=tv&feature=7&listpage=2&offset=120&pagesize=30
//        https://v.qq.com/x/bu/pagesheet/list?append=1&channel=movie&listpage=0&offset=0&pagesize=30
        return move;
    }

    @Override
    public boolean playDetail(Detial detial) {
        try {
            List<Detial.DetailPlayUrl> playUrls = new ArrayList();
            detial.playUrls = playUrls;
            getList(playUrls, detial.text, 0);
            if (playUrls.size() <= 0) {
                Detial.DetailPlayUrl playUrl = new Detial.DetailPlayUrl();
                playUrl.href = detial.detialUrl;
                playUrl.title = detial.name;
                playUrls.add(playUrl);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void getList(List<Detial.DetailPlayUrl> detailPlayUrls, String cid, int page_num) {
        String pageContext = String.format("cid=%s&page_num=%s&page_size=100&id_type=1&req_type=6&req_from=web&req_from_second_type=", cid, page_num);
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL("https://pbaccess.video.qq.com/trpc.universal_backend_service.page_server_rpc.PageServer/GetPageData?video_appid=3000010&vplatform=2").openConnection();
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("referer", "https://v.qq.com/");
            connection.setRequestProperty("cookie", "pgv_pvi=3198041088; pgv_pvid=7152785862; RK=XCpgrTHyNw; ptcz=9b4b1b27042ff232df9fd5e858d770d4781d63a2e81c38022fc5d9a623b12b64; _ga=GA1.2.2066256450.1605693674; _tc_unionid=bf779fa5-e900-468d-87e0-3a81a2403392; sd_userid=68361615257950550; sd_cookie_crttime=1615257950550; tvfe_boss_uuid=3182985122c5bbe6; video_guid=bbe1cbccb7920994; video_platform=2; pgv_info=ssid=s298966110; vversion_name=8.2.95; video_omgid=bbe1cbccb7920994");
            connection.connect();
            JSONObject params = new JSONObject();
            params.put("has_cache", 1);
            JSONObject page_params = new JSONObject();
            page_params.put("req_from", "web");
            page_params.put("page_type", "detail_operation");
            page_params.put("page_id", "vsite_episode_list");
            page_params.put("id_type", "1");
            page_params.put("lid", "");
            page_params.put("vid", "");
            page_params.put("page_context", pageContext);
            params.put("page_params", page_params);
            OutputStream os = connection.getOutputStream();
            os.write(params.toString().getBytes());
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int len = 0;
            byte[] buff = new byte[1024 * 1024];
            InputStream is = connection.getInputStream();
            while ((len = is.read(buff)) != -1) {
                baos.write(buff, 0, len);
            }
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            JSONObject jsonObject = new JSONObject(new String(baos.toByteArray()));
            jsonObject = jsonObject.optJSONObject("data");
            List<Detial.DetailPlayUrl> playUrls = new ArrayList<>();
            if (jsonObject != null) {
                JSONArray array = jsonObject.optJSONArray("module_list_datas");
                if (array != null && array.length() > 0) {
                    array = array.optJSONObject(0).optJSONArray("module_datas");
                    if (array != null && array.length() > 0) {
                        jsonObject = array.optJSONObject(0).optJSONObject("item_data_lists");
                        if (jsonObject != null) {
                            array = jsonObject.optJSONArray("item_datas");
                            if (array != null && array.length() > 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    jsonObject = array.optJSONObject(i).optJSONObject("item_params");
                                    String href = String.format("https://v.qq.com/x/cover/%s/%s.html", cid, jsonObject.optString("vid"));
                                    String title = jsonObject.optString("play_title");
                                    String duration = jsonObject.optString("duration");
                                    Detial.DetailPlayUrl playUrl = new Detial.DetailPlayUrl();
                                    playUrl.href = href;
                                    playUrl.title = title;
                                    playUrl.duration = duration;
                                    playUrls.add(playUrl);
                                }
                            }
                        }

                    }
                }
            }
            detailPlayUrls.addAll(playUrls);
            if (playUrls.size() >= 100) {
                getList(detailPlayUrls, cid, page_num + 1);
            }
            try {
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class Play {
        String url = "";
    }

    @Override
    public String play(Detial.DetailPlayUrl playUrl) {
//        return String.format("http://127.0.0.1:%s/movie?url=%s", PlatformsManager.PORT, new String(Base64.encode(playUrl.href.getBytes(), 0)));
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
        List<Detial> detials = new ArrayList();
        String url = String.format("https://v.qq.com/x/search/?q=%s&stag=0&smartbox_ab=", URLEncoder.encode(text));
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setReadTimeout(5000);
            connection.setConnectTimeout(5000);
            connection.setDoOutput(false);
            connection.setUseCaches(false);
            connection.setRequestProperty("Content-Type", "application/x-www-from-urlencoded");
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();
            int len = 0;
            byte[] buff = new byte[1024 * 1024];
            InputStream is = connection.getInputStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            while ((len = is.read(buff)) != -1) {
                baos.write(buff, 0, len);
            }
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
//            JXDocument jx = JXDocument.create(createConnection(url).get());
            JXDocument jx = JXDocument.create(new String(baos.toByteArray()));
            try {
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            List<JXNode> divs = jx.selN("//div[@class='wrapper_main']/div/div");
            Log.e("=====", "" + divs.size());
            for (JXNode div : divs) {
                List<JXNode> as = div.sel("div[@class='_infos']/div/a");
                if (as.size() <= 0)
                    continue;
                JXNode a = as.get(0);
                Element element = a.asElement();
                String href = element.attr("href");
                href = Util.dealWithUrl(href, url, HOST);
                String name = element.attr("title");
                JXNode i = a.sel("img[1]").get(0);
                String img = i.asElement().attr("src");
                Detial detial = new Detial();
                detial.detialUrl = href;
                detial.name = name;
                detial.text = div.asElement().attr("data-id");
//                detial.text = element.attr("dt-params");
//                if (detial.text != null && !detial.text.isEmpty()) {
//                    Pattern pattern = Pattern.compile("cid=([^&]*)");
//                    Matcher matcher = pattern.matcher(detial.text);
//                    if (matcher.find()) {
//                        detial.text = matcher.group(1);
//                    } else{
//                         pattern = Pattern.compile("[^/]([^.^/]*).html");
//                         matcher = pattern.matcher(href);
//                         if(matcher.find())
//                             detial.text = matcher.group(1);
//                         else
//                             detial.text = "";
//                    }
//
//                }
                detial.img = Util.dealWithUrl(img, url, HOST);
                detial.platformas = name();
                detials.add(detial);
            }
            if (detials.size() >= 30) {
                Pattern pattern = Pattern.compile("listpage=([0-9]+)");
                Matcher matcher = pattern.matcher(url);
                String next = url;
                if (matcher.find()) {
                    int listpage = Integer.valueOf(matcher.group(1));
                    next = next.replace("listpage=" + listpage, "listpage=" + (listpage + 1));
                }
                pattern = Pattern.compile("offset=([0-9]+)");
                matcher = pattern.matcher(url);
                if (matcher.find()) {
                    int listpage = Integer.valueOf(matcher.group(1));
                    next = next.replace("offset=" + listpage, "offset=" + (listpage + 30));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return detials;
    }

    @Override
    public boolean hasVip() {
        return true;
    }
}
