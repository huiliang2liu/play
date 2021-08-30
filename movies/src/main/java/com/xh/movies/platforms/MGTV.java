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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MGTV extends AbsPlatform {
    private static final String HOST = "https://www.mgtv.com";
    private static final String TAG = "MGTV";
    private static final Map<String, List<Title>> map = new HashMap<>();

    static {
        {
            List<Title> titles = new ArrayList<>();
            titles.add(new Title("甜蜜互宠", "https://pianku.api.mgtv.com/rider/list/pcweb/v3?platform=pcweb&channelId=2&pn=1&pc=80&hudong=1&_support=10000000&kind=14&area=a1&year=all&edition=all&feature=all&chargeInfo=a1&sort=c2&callback=jsonp_1629943684405_89289"));
            titles.add(new Title("古装", "https://pianku.api.mgtv.com/rider/list/pcweb/v3?platform=pcweb&channelId=2&pn=1&pc=80&hudong=1&_support=10000000&kind=148&area=a1&year=all&edition=all&feature=all&chargeInfo=a1&sort=c2&callback=jsonp_1629943946743_65406"));
            titles.add(new Title("虐恋情深", "https://pianku.api.mgtv.com/rider/list/pcweb/v3?platform=pcweb&channelId=2&pn=1&pc=80&hudong=1&_support=10000000&kind=15&area=a1&year=all&edition=all&feature=all&chargeInfo=a1&sort=c2&callback=jsonp_1629943989001_683"));
            titles.add(new Title("仙侠玄幻", "https://pianku.api.mgtv.com/rider/list/pcweb/v3?platform=pcweb&channelId=2&pn=1&pc=80&hudong=1&_support=10000000&kind=17&area=a1&year=all&edition=all&feature=all&chargeInfo=a1&sort=c2&callback=jsonp_1629944032637_28169"));
            titles.add(new Title("青涩校园", "https://pianku.api.mgtv.com/rider/list/pcweb/v3?platform=pcweb&channelId=2&pn=1&pc=80&hudong=1&_support=10000000&kind=16&area=a1&year=all&edition=all&feature=all&chargeInfo=a1&sort=c2&callback=jsonp_1629944072303_19771"));
            titles.add(new Title("悬疑", "https://pianku.api.mgtv.com/rider/list/pcweb/v3?platform=pcweb&channelId=2&pn=1&pc=80&hudong=1&_support=10000000&kind=2024&area=a1&year=all&edition=all&feature=all&chargeInfo=a1&sort=c2&callback=jsonp_1629944103688_50898"));
            titles.add(new Title("喜剧", "https://pianku.api.mgtv.com/rider/list/pcweb/v3?platform=pcweb&channelId=2&pn=1&pc=80&hudong=1&_support=10000000&kind=2027&area=a1&year=all&edition=all&feature=all&chargeInfo=a1&sort=c2&callback=jsonp_1629944131910_96773"));
            titles.add(new Title("都市职场", "https://pianku.api.mgtv.com/rider/list/pcweb/v3?platform=pcweb&channelId=2&pn=1&pc=80&hudong=1&_support=10000000&kind=19&area=a1&year=all&edition=all&feature=all&chargeInfo=a1&sort=c2&callback=jsonp_1629944160071_65375"));
            titles.add(new Title("偶像", "https://pianku.api.mgtv.com/rider/list/pcweb/v3?platform=pcweb&channelId=2&pn=1&pc=80&hudong=1&_support=10000000&kind=147&area=a1&year=all&edition=all&feature=all&chargeInfo=a1&sort=c2&callback=jsonp_1629944187727_6863"));
            titles.add(new Title("谍战", "https://pianku.api.mgtv.com/rider/list/pcweb/v3?platform=pcweb&channelId=2&pn=1&pc=80&hudong=1&_support=10000000&kind=2026&area=a1&year=all&edition=all&feature=all&chargeInfo=a1&sort=c2&callback=jsonp_1629944218183_23762"));
            titles.add(new Title("家庭", "https://pianku.api.mgtv.com/rider/list/pcweb/v3?platform=pcweb&channelId=2&pn=1&pc=80&hudong=1&_support=10000000&kind=2029&area=a1&year=all&edition=all&feature=all&chargeInfo=a1&sort=c2&callback=jsonp_1629944244567_98207"));
            titles.add(new Title("芒果出品", "https://pianku.api.mgtv.com/rider/list/pcweb/v3?platform=pcweb&channelId=2&pn=1&pc=80&hudong=1&_support=10000000&kind=23&area=a1&year=all&edition=all&feature=all&chargeInfo=a1&sort=c2&callback=jsonp_1629944268786_77063"));
            titles.add(new Title("乡村", "https://pianku.api.mgtv.com/rider/list/pcweb/v3?platform=pcweb&channelId=2&pn=1&pc=80&hudong=1&_support=10000000&kind=2032&area=a1&year=all&edition=all&feature=all&chargeInfo=a1&sort=c2&callback=jsonp_1629944297342_99376"));
            titles.add(new Title("青春", "https://pianku.api.mgtv.com/rider/list/pcweb/v3?platform=pcweb&channelId=2&pn=1&pc=80&hudong=1&_support=10000000&kind=2025&area=a1&year=all&edition=all&feature=all&chargeInfo=a1&sort=c2&callback=jsonp_1629944320278_20246"));
            titles.add(new Title("历史", "https://pianku.api.mgtv.com/rider/list/pcweb/v3?platform=pcweb&channelId=2&pn=1&pc=80&hudong=1&_support=10000000&kind=2030&area=a1&year=all&edition=all&feature=all&chargeInfo=a1&sort=c2&callback=jsonp_1629944347401_28265"));
            titles.add(new Title("其他", "https://pianku.api.mgtv.com/rider/list/pcweb/v3?platform=pcweb&channelId=2&pn=1&pc=80&hudong=1&_support=10000000&kind=26&area=a1&year=all&edition=all&feature=all&chargeInfo=a1&sort=c2&callback=jsonp_1629944370359_64784"));
            map.put("电视剧", titles);
        }
        {
            List<Title> titles = new ArrayList<>();
            titles.add(new Title("爱情", "https://pianku.api.mgtv.com/rider/list/pcweb/v3?platform=pcweb&channelId=3&pn=1&pc=80&hudong=1&_support=10000000&kind=175&edition=a1&year=all&chargeInfo=a1&sort=c2&callback=jsonp_1629950217383_81978"));
            titles.add(new Title("喜剧", "https://pianku.api.mgtv.com/rider/list/pcweb/v3?platform=pcweb&channelId=3&pn=1&pc=80&hudong=1&_support=10000000&kind=176&edition=a1&year=all&chargeInfo=a1&sort=c2&callback=jsonp_1629950308293_24636"));
            titles.add(new Title("动作", "https://pianku.api.mgtv.com/rider/list/pcweb/v3?platform=pcweb&channelId=3&pn=1&pc=80&hudong=1&_support=10000000&kind=177&edition=a1&year=all&chargeInfo=a1&sort=c2&callback=jsonp_1629950338596_60375"));
            titles.add(new Title("青春", "https://pianku.api.mgtv.com/rider/list/pcweb/v3?platform=pcweb&channelId=3&pn=1&pc=80&hudong=1&_support=10000000&kind=39&edition=a1&year=all&chargeInfo=a1&sort=c2&callback=jsonp_1629950366364_72455"));
            titles.add(new Title("科幻", "https://pianku.api.mgtv.com/rider/list/pcweb/v3?platform=pcweb&channelId=3&pn=1&pc=80&hudong=1&_support=10000000&kind=178&edition=a1&year=all&chargeInfo=a1&sort=c2&callback=jsonp_1629950396086_98913"));
            titles.add(new Title("恐怖悬疑", "https://pianku.api.mgtv.com/rider/list/pcweb/v3?platform=pcweb&channelId=3&pn=1&pc=80&hudong=1&_support=10000000&kind=43&edition=a1&year=all&chargeInfo=a1&sort=c2&callback=jsonp_1629950422987_64064"));
            titles.add(new Title("战争", "https://pianku.api.mgtv.com/rider/list/pcweb/v3?platform=pcweb&channelId=3&pn=1&pc=80&hudong=1&_support=10000000&kind=44&edition=a1&year=all&chargeInfo=a1&sort=c2&callback=jsonp_1629950447101_3078"));
            titles.add(new Title("警匪", "https://pianku.api.mgtv.com/rider/list/pcweb/v3?platform=pcweb&channelId=3&pn=1&pc=80&hudong=1&_support=10000000&kind=45&edition=a1&year=all&chargeInfo=a1&sort=c2&callback=jsonp_1629950474486_38604"));
            titles.add(new Title("历史", "https://pianku.api.mgtv.com/rider/list/pcweb/v3?platform=pcweb&channelId=3&pn=1&pc=80&hudong=1&_support=10000000&kind=46&edition=a1&year=all&chargeInfo=a1&sort=c2&callback=jsonp_1629950500870_92447"));
            titles.add(new Title("动画", "https://pianku.api.mgtv.com/rider/list/pcweb/v3?platform=pcweb&channelId=3&pn=1&pc=80&hudong=1&_support=10000000&kind=48&edition=a1&year=all&chargeInfo=a1&sort=c2&callback=jsonp_1629950523843_150"));
            titles.add(new Title("其他", "https://pianku.api.mgtv.com/rider/list/pcweb/v3?platform=pcweb&channelId=3&pn=1&pc=80&hudong=1&_support=10000000&kind=50&edition=a1&year=all&chargeInfo=a1&sort=c2&callback=jsonp_1629950560420_39543"));
            map.put("电影", titles);
        }
        {
            List<Title> titles = new ArrayList<>();
            titles.add(new Title("儿歌", "https://pianku.api.mgtv.com/rider/list/pcweb/v3?platform=pcweb&channelId=10&pn=1&pc=80&hudong=1&_support=10000000&kind=184&fitAge=197&callback=jsonp_1629951941471_78466"));
            titles.add(new Title("益智", "https://pianku.api.mgtv.com/rider/list/pcweb/v3?platform=pcweb&channelId=10&pn=1&pc=80&hudong=1&_support=10000000&kind=83&fitAge=197&callback=jsonp_1629951967560_90433"));
            titles.add(new Title("冒险", "https://pianku.api.mgtv.com/rider/list/pcweb/v3?platform=pcweb&channelId=10&pn=1&pc=80&hudong=1&_support=10000000&kind=87&fitAge=197&callback=jsonp_1629951998425_50271"));
            titles.add(new Title("搞笑", "https://pianku.api.mgtv.com/rider/list/pcweb/v3?platform=pcweb&channelId=10&pn=1&pc=80&hudong=1&_support=10000000&kind=62&fitAge=197&callback=jsonp_1629952022111_35766"));
            titles.add(new Title("少儿节目", "https://pianku.api.mgtv.com/rider/list/pcweb/v3?platform=pcweb&channelId=10&pn=1&pc=80&hudong=1&_support=10000000&kind=81&fitAge=197&callback=jsonp_1629952044393_11869"));
            titles.add(new Title("亲子幼教", "https://pianku.api.mgtv.com/rider/list/pcweb/v3?platform=pcweb&channelId=10&pn=1&pc=80&hudong=1&_support=10000000&kind=68&fitAge=197&callback=jsonp_1629952065210_77559"));
            titles.add(new Title("竞技", "https://pianku.api.mgtv.com/rider/list/pcweb/v3?platform=pcweb&channelId=10&pn=1&pc=80&hudong=1&_support=10000000&kind=88&fitAge=197&callback=jsonp_1629952094290_17113"));
            titles.add(new Title("励志", "https://pianku.api.mgtv.com/rider/list/pcweb/v3?platform=pcweb&channelId=10&pn=1&pc=80&hudong=1&_support=10000000&kind=194&fitAge=197&callback=jsonp_1629952120082_46336"));
            titles.add(new Title("机战", "https://pianku.api.mgtv.com/rider/list/pcweb/v3?platform=pcweb&channelId=10&pn=1&pc=80&hudong=1&_support=10000000&kind=89&fitAge=197&callback=jsonp_1629952145496_16239"));
            map.put("少儿", titles);
        }
        {
            List<Title> titles = new ArrayList<>();
            titles.add(new Title("青春", "https://pianku.api.mgtv.com/rider/list/pcweb/v3?platform=pcweb&channelId=50&pn=1&pc=80&hudong=1&_support=10000000&kind=63&area=a1&edition=a1&sort=c2&callback=jsonp_1629952261795_8261"));
            titles.add(new Title("恋爱", "https://pianku.api.mgtv.com/rider/list/pcweb/v3?platform=pcweb&channelId=50&pn=1&pc=80&hudong=1&_support=10000000&kind=84&area=a1&edition=a1&sort=c2&callback=jsonp_1629952286880_74342"));
            titles.add(new Title("冒险", "https://pianku.api.mgtv.com/rider/list/pcweb/v3?platform=pcweb&channelId=50&pn=1&pc=80&hudong=1&_support=10000000&kind=87&area=a1&edition=a1&sort=c2&callback=jsonp_1629952311265_70629"));
            titles.add(new Title("搞笑", "https://pianku.api.mgtv.com/rider/list/pcweb/v3?platform=pcweb&channelId=50&pn=1&pc=80&hudong=1&_support=10000000&kind=62&area=a1&edition=a1&sort=c2&callback=jsonp_1629952335681_80027"));
            titles.add(new Title("竞技", "https://pianku.api.mgtv.com/rider/list/pcweb/v3?platform=pcweb&channelId=50&pn=1&pc=80&hudong=1&_support=10000000&kind=88&area=a1&edition=a1&sort=c2&callback=jsonp_1629952362434_13978"));
            titles.add(new Title("经典", "https://pianku.api.mgtv.com/rider/list/pcweb/v3?platform=pcweb&channelId=50&pn=1&pc=80&hudong=1&_support=10000000&kind=95&area=a1&edition=a1&sort=c2&callback=jsonp_1629952385937_48842"));
            titles.add(new Title("魔幻", "https://pianku.api.mgtv.com/rider/list/pcweb/v3?platform=pcweb&channelId=50&pn=1&pc=80&hudong=1&_support=10000000&kind=91&area=a1&edition=a1&sort=c2&callback=jsonp_1629952416713_41542"));
            titles.add(new Title("玄幻武侠", "https://pianku.api.mgtv.com/rider/list/pcweb/v3?platform=pcweb&channelId=50&pn=1&pc=80&hudong=1&_support=10000000&kind=196&area=a1&edition=a1&sort=c2&callback=jsonp_1629952438872_40871"));
            titles.add(new Title("科幻", "https://pianku.api.mgtv.com/rider/list/pcweb/v3?platform=pcweb&channelId=50&pn=1&pc=80&hudong=1&_support=10000000&kind=90&area=a1&edition=a1&sort=c2&callback=jsonp_1629952473049_21421"));
            titles.add(new Title("特摄", "https://pianku.api.mgtv.com/rider/list/pcweb/v3?platform=pcweb&channelId=50&pn=1&pc=80&hudong=1&_support=10000000&kind=66&area=a1&edition=a1&sort=c2&callback=jsonp_1629952496498_60171"));
            titles.add(new Title("推理", "https://pianku.api.mgtv.com/rider/list/pcweb/v3?platform=pcweb&channelId=50&pn=1&pc=80&hudong=1&_support=10000000&kind=67&area=a1&edition=a1&sort=c2&callback=jsonp_1629952524425_88660"));
            titles.add(new Title("芒果出品", "https://pianku.api.mgtv.com/rider/list/pcweb/v3?platform=pcweb&channelId=50&pn=1&pc=80&hudong=1&_support=10000000&kind=69&area=a1&edition=a1&sort=c2&callback=jsonp_1629952545539_10872"));
            titles.add(new Title("其他", "https://pianku.api.mgtv.com/rider/list/pcweb/v3?platform=pcweb&channelId=50&pn=1&pc=80&hudong=1&_support=10000000&kind=72&area=a1&edition=a1&sort=c2&callback=jsonp_1629952567808_28639"));
            map.put("动漫", titles);
        }
        {
            List<Title> titles = new ArrayList<>();
            titles.add(new Title("音乐", "https://pianku.api.mgtv.com/rider/list/pcweb/v3?platform=pcweb&channelId=1&pn=1&pc=80&hudong=1&_support=10000000&kind=8&area=a1&sort=c2&callback=jsonp_1629952640178_4052"));
            titles.add(new Title("真人秀", "https://pianku.api.mgtv.com/rider/list/pcweb/v3?platform=pcweb&channelId=1&pn=1&pc=80&hudong=1&_support=10000000&kind=173&area=a1&sort=c2&callback=jsonp_1629952661937_70691"));
            titles.add(new Title("王牌综艺", "https://pianku.api.mgtv.com/rider/list/pcweb/v3?platform=pcweb&channelId=1&pn=1&pc=80&hudong=1&_support=10000000&kind=4&area=a1&sort=c2&callback=jsonp_1629952685802_49269"));
            titles.add(new Title("搞笑", "https://pianku.api.mgtv.com/rider/list/pcweb/v3?platform=pcweb&channelId=1&pn=1&pc=80&hudong=1&_support=10000000&kind=6&area=a1&sort=c2&callback=jsonp_1629952708913_39483"));
            titles.add(new Title("脱口秀", "https://pianku.api.mgtv.com/rider/list/pcweb/v3?platform=pcweb&channelId=1&pn=1&pc=80&hudong=1&_support=10000000&kind=180&area=a1&sort=c2&callback=jsonp_1629952729788_4642"));
            titles.add(new Title("情感", "https://pianku.api.mgtv.com/rider/list/pcweb/v3?platform=pcweb&channelId=1&pn=1&pc=80&hudong=1&_support=10000000&kind=7&area=a1&sort=c2&callback=jsonp_1629952748721_19419"));
            titles.add(new Title("亲子", "https://pianku.api.mgtv.com/rider/list/pcweb/v3?platform=pcweb&channelId=1&pn=1&pc=80&hudong=1&_support=10000000&kind=179&area=a1&sort=c2&callback=jsonp_1629952769033_37413"));
            titles.add(new Title("八卦", "https://pianku.api.mgtv.com/rider/list/pcweb/v3?platform=pcweb&channelId=1&pn=1&pc=80&hudong=1&_support=10000000&kind=2034&area=a1&sort=c2&callback=jsonp_1629952789833_71503"));
            titles.add(new Title("晚会", "https://pianku.api.mgtv.com/rider/list/pcweb/v3?platform=pcweb&channelId=1&pn=1&pc=80&hudong=1&_support=10000000&kind=168&area=a1&sort=c2&callback=jsonp_1629952809381_70486"));
            titles.add(new Title("旅游", "https://pianku.api.mgtv.com/rider/list/pcweb/v3?platform=pcweb&channelId=1&pn=1&pc=80&hudong=1&_support=10000000&kind=170&area=a1&sort=c2&callback=jsonp_1629952827231_57349"));
            titles.add(new Title("选秀", "https://pianku.api.mgtv.com/rider/list/pcweb/v3?platform=pcweb&channelId=1&pn=1&pc=80&hudong=1&_support=10000000&kind=181&area=a1&sort=c2&callback=jsonp_1629952848682_39041"));
            titles.add(new Title("美食", "https://pianku.api.mgtv.com/rider/list/pcweb/v3?platform=pcweb&channelId=1&pn=1&pc=80&hudong=1&_support=10000000&kind=2033&area=a1&sort=c2&callback=jsonp_1629952870953_85968"));
            titles.add(new Title("生活", "https://pianku.api.mgtv.com/rider/list/pcweb/v3?platform=pcweb&channelId=1&pn=1&pc=80&hudong=1&_support=10000000&kind=9&area=a1&sort=c2&callback=jsonp_1629952893962_29376"));
            titles.add(new Title("时尚", "https://pianku.api.mgtv.com/rider/list/pcweb/v3?platform=pcweb&channelId=1&pn=1&pc=80&hudong=1&_support=10000000&kind=171&area=a1&sort=c2&callback=jsonp_1629952912539_72834"));
            titles.add(new Title("竞技", "https://pianku.api.mgtv.com/rider/list/pcweb/v3?platform=pcweb&channelId=1&pn=1&pc=80&hudong=1&_support=10000000&kind=174&area=a1&sort=c2&callback=jsonp_1629952939235_4215"));
            titles.add(new Title("访谈", "https://pianku.api.mgtv.com/rider/list/pcweb/v3?platform=pcweb&channelId=1&pn=1&pc=80&hudong=1&_support=10000000&kind=172&area=a1&sort=c2&callback=jsonp_1629952966050_22653"));
            titles.add(new Title("纪实", "https://pianku.api.mgtv.com/rider/list/pcweb/v3?platform=pcweb&channelId=1&pn=1&pc=80&hudong=1&_support=10000000&kind=2035&area=a1&sort=c2&callback=jsonp_1629952984332_81428"));
            map.put("综艺", titles);
        }
        {
            List<Title> titles = new ArrayList<>();
            titles.add(new Title("美食", "https://pianku.api.mgtv.com/rider/list/pcweb/v3?platform=pcweb&channelId=51&pn=1&pc=80&hudong=1&_support=10000000&kind=97&sort=c1&callback=jsonp_1629953266089_6734"));
            titles.add(new Title("文化", "https://pianku.api.mgtv.com/rider/list/pcweb/v3?platform=pcweb&channelId=51&pn=1&pc=80&hudong=1&_support=10000000&kind=98&sort=c1&callback=jsonp_1629953310305_72288"));
            titles.add(new Title("社会", "https://pianku.api.mgtv.com/rider/list/pcweb/v3?platform=pcweb&channelId=51&pn=1&pc=80&hudong=1&_support=10000000&kind=99&sort=c1&callback=jsonp_1629953331250_67689"));
            titles.add(new Title("历史", "https://pianku.api.mgtv.com/rider/list/pcweb/v3?platform=pcweb&channelId=51&pn=1&pc=80&hudong=1&_support=10000000&kind=100&sort=c1&callback=jsonp_1629953350201_56435"));
            titles.add(new Title("军事", "https://pianku.api.mgtv.com/rider/list/pcweb/v3?platform=pcweb&channelId=51&pn=1&pc=80&hudong=1&_support=10000000&kind=101&sort=c1&callback=jsonp_1629953373090_3374"));
            titles.add(new Title("人物", "https://pianku.api.mgtv.com/rider/list/pcweb/v3?platform=pcweb&channelId=51&pn=1&pc=80&hudong=1&_support=10000000&kind=102&sort=c1&callback=jsonp_1629953393354_47481"));
            titles.add(new Title("探索", "https://pianku.api.mgtv.com/rider/list/pcweb/v3?platform=pcweb&channelId=51&pn=1&pc=80&hudong=1&_support=10000000&kind=103&sort=c1&callback=jsonp_1629953418665_41016"));
            titles.add(new Title("自然", "https://pianku.api.mgtv.com/rider/list/pcweb/v3?platform=pcweb&channelId=51&pn=1&pc=80&hudong=1&_support=10000000&kind=104&sort=c1&callback=jsonp_1629953446426_39091"));
            titles.add(new Title("其他", "https://pianku.api.mgtv.com/rider/list/pcweb/v3?platform=pcweb&channelId=51&pn=1&pc=80&hudong=1&_support=10000000&kind=105&sort=c1&callback=jsonp_1629953472466_57986"));
            map.put("纪录片", titles);
        }
        {
            List<Title> titles = new ArrayList<>();
            titles.add(new Title("饭制", "https://pianku.api.mgtv.com/rider/list/pcweb/v3?platform=pcweb&channelId=113&pn=1&pc=80&hudong=1&_support=10000000&kind=186&sort=c1&callback=jsonp_1629953535362_24054"));
            titles.add(new Title("八卦", "https://pianku.api.mgtv.com/rider/list/pcweb/v3?platform=pcweb&channelId=113&pn=1&pc=80&hudong=1&_support=10000000&kind=187&sort=c1&callback=jsonp_1629953559507_18662"));
            titles.add(new Title("明星网红", "https://pianku.api.mgtv.com/rider/list/pcweb/v3?platform=pcweb&channelId=113&pn=1&pc=80&hudong=1&_support=10000000&kind=185&sort=c1&callback=jsonp_1629953583211_80490"));
            titles.add(new Title("时尚", "https://pianku.api.mgtv.com/rider/list/pcweb/v3?platform=pcweb&channelId=113&pn=1&pc=80&hudong=1&_support=10000000&kind=190&sort=c1&callback=jsonp_1629953605450_24923"));
            titles.add(new Title("综艺", "https://pianku.api.mgtv.com/rider/list/pcweb/v3?platform=pcweb&channelId=113&pn=1&pc=80&hudong=1&_support=10000000&kind=189&sort=c1&callback=jsonp_1629953628602_61835"));
            titles.add(new Title("影视剧", "https://pianku.api.mgtv.com/rider/list/pcweb/v3?platform=pcweb&channelId=113&pn=1&pc=80&hudong=1&_support=10000000&kind=188&sort=c1&callback=jsonp_1629953651834_25297"));
            map.put("娱乐", titles);
        }
    }

    @Override
    public List<Title> types() {
        List<Title> titles = new ArrayList<>();
        for (String key : map.keySet())
            titles.add(new Title(key, key));
        return titles;
    }

    @Override
    public List<Title> titles(String url) {
        return map.get(url);
    }

    @Override
    public String name() {
        return "芒果TV";
    }

    @Override
    public ListMove list(String url) {
        Log.e(TAG, url);
        ListMove listMove = new ListMove();
        List<Detial> detials = new ArrayList<>();
        listMove.detials = detials;
        String text = Util.get(url);
        if (text == null || text.isEmpty())
            return listMove;
        text = text.split("\\(")[1];
        text.substring(0, text.length() - 1);
        try {
            JSONObject jsonObject = new JSONObject(text);
            jsonObject = jsonObject.optJSONObject("data");
            if (jsonObject == null)
                return listMove;
            JSONArray array = jsonObject.optJSONArray("hitDocs");
            if (array == null || array.length() <= 0)
                return listMove;
            for (int i = 0; i < array.length(); i++) {
                jsonObject = array.optJSONObject(i);
                Detial detial = new Detial();
                detial.img = jsonObject.optString("img");
                detial.name = jsonObject.optString("title");
                detial.text = jsonObject.optString("playPartId");
                detial.detialUrl = String.format("https://www.mgtv.com/b/%s/%s.html?lastp=list_index", jsonObject.optString("clipId"), detial.text);
                Log.e(TAG, detial.detialUrl);
                detials.add(detial);
            }
            if (detials.size() >= 80) {
                Pattern pattern = Pattern.compile("pn=([0-9]*)&");
                Matcher matcher = pattern.matcher(url);
                if (matcher.find()) {
                    int pn = Integer.valueOf(matcher.group(1));
                    listMove.next = url.replace("pn=" + pn, "pn=" + (pn + 1));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return listMove;
    }

    @Override
    public boolean playDetail(Detial detial) {
        List<Detial.DetailPlayUrl> detailPlayUrls = new ArrayList<>();
        detial.playUrls = detailPlayUrls;
        String text = String.format("https://pcweb.api.mgtv.com/episode/list?_support=10000000&version=5.5.35&video_id=%s&page=0&size=10000&&callback=jsonp_1629949202070_60875", detial.text);
        Log.e(TAG, text);
        text = Util.get(text);
        text = text.substring("jsonp_1629949202070_60875(".length(), text.length() - 1);
        Log.e(TAG, text);
        try {
            JSONObject object = new JSONObject(text);
            object = object.optJSONObject("data");
            if (object != null) {
                JSONArray array = object.optJSONArray("list");
                if (array != null && array.length() > 0) {
                    for (int i = 0; i < array.length(); i++) {
                        object = array.optJSONObject(i);
                        Detial.DetailPlayUrl detailPlayUrl = new Detial.DetailPlayUrl();
                        detailPlayUrl.href = Util.dealWithUrl(object.optString("url"), detial.detialUrl, HOST);
                        detailPlayUrl.title = object.optString("t4");
                        Log.e(TAG, detailPlayUrl.href);
                        detailPlayUrls.add(detailPlayUrl);
                    }
                }
            }
            if (detailPlayUrls.size() <= 0) {
                Detial.DetailPlayUrl detailPlayUrl = new Detial.DetailPlayUrl();
                detailPlayUrl.href = detial.detialUrl;
                detailPlayUrl.title = detial.name;
                detailPlayUrls.add(detailPlayUrl);
            }
            return true;
        } catch (JSONException e) {
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
        return null;
    }

    @Override
    public boolean hasVip() {
        return true;
    }
}
