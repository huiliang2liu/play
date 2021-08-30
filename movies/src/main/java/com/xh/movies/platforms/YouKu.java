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

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YouKu extends AbsPlatform {
    private static final String TAG = "YouKu";
    private static final String HOST = "https://www.youku.com";
    private static final String JSON_STRING = "[{\n" +
            "\t\t\"name\": \"电视剧\",\n" +
            "\t\t\"key\": \"97\",\n" +
            "\t\t\"type\": \"show\",\n" +
            "\t\t\"list\": [{\n" +
            "\t\t\t\"name\": \"全部\",\n" +
            "\t\t\t\"key\": \"\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"青春\",\n" +
            "\t\t\t\"key\": \"青春\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"爱情\",\n" +
            "\t\t\t\"key\": \"爱情\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"谍战\",\n" +
            "\t\t\t\"key\": \"谍战\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"冒险\",\n" +
            "\t\t\t\"key\": \"冒险\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"穿越\",\n" +
            "\t\t\t\"key\": \"穿越\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"仙侠\",\n" +
            "\t\t\t\"key\": \"仙侠\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"罪案\",\n" +
            "\t\t\t\"key\": \"罪案\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"年代\",\n" +
            "\t\t\t\"key\": \"年代\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"古装\",\n" +
            "\t\t\t\"key\": \"古装\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"武侠\",\n" +
            "\t\t\t\"key\": \"武侠\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"警匪\",\n" +
            "\t\t\t\"key\": \"警匪\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"战争\",\n" +
            "\t\t\t\"key\": \"战争\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"军旅\",\n" +
            "\t\t\t\"key\": \"军旅\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"神话\",\n" +
            "\t\t\t\"key\": \"神话\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"科幻\",\n" +
            "\t\t\t\"key\": \"科幻\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"悬疑\",\n" +
            "\t\t\t\"key\": \"悬疑\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"历史\",\n" +
            "\t\t\t\"key\": \"历史\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"儿童\",\n" +
            "\t\t\t\"key\": \"儿童\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"农村\",\n" +
            "\t\t\t\"key\": \"农村\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"都市\",\n" +
            "\t\t\t\"key\": \"都市\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"家庭\",\n" +
            "\t\t\t\"key\": \"家庭\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"喜剧\",\n" +
            "\t\t\t\"key\": \"喜剧\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"偶像\",\n" +
            "\t\t\t\"key\": \"偶像\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"时装\",\n" +
            "\t\t\t\"key\": \"时装\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"优酷出品\",\n" +
            "\t\t\t\"key\": \"优酷出品\"\n" +
            "\t\t}]\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"name\": \"电影\",\n" +
            "\t\t\"key\": \"96\",\n" +
            "\t\t\"type\": \"show\",\n" +
            "\t\t\"list\": [{\n" +
            "\t\t\t\"name\": \"全部\",\n" +
            "\t\t\t\"key\": \"\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"武侠\",\n" +
            "\t\t\t\"key\": \"武侠\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"警匪\",\n" +
            "\t\t\t\"key\": \"警匪\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"犯罪\",\n" +
            "\t\t\t\"key\": \"犯罪\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"科幻\",\n" +
            "\t\t\t\"key\": \"科幻\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"战争\",\n" +
            "\t\t\t\"key\": \"战争\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"恐怖\",\n" +
            "\t\t\t\"key\": \"恐怖\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"惊悚\",\n" +
            "\t\t\t\"key\": \"惊悚\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"纪录片\",\n" +
            "\t\t\t\"key\": \"纪录片\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"西部\",\n" +
            "\t\t\t\"key\": \"西部\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"戏曲\",\n" +
            "\t\t\t\"key\": \"戏曲\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"歌舞\",\n" +
            "\t\t\t\"key\": \"歌舞\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"奇幻\",\n" +
            "\t\t\t\"key\": \"奇幻\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"冒险\",\n" +
            "\t\t\t\"key\": \"冒险\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"悬疑\",\n" +
            "\t\t\t\"key\": \"悬疑\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"历史\",\n" +
            "\t\t\t\"key\": \"历史\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"动作\",\n" +
            "\t\t\t\"key\": \"动作\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"传记\",\n" +
            "\t\t\t\"key\": \"传记\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"动画\",\n" +
            "\t\t\t\"key\": \"动画\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"儿童\",\n" +
            "\t\t\t\"key\": \"儿童\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"喜剧\",\n" +
            "\t\t\t\"key\": \"喜剧\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"爱情\",\n" +
            "\t\t\t\"key\": \"爱情\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"剧情\",\n" +
            "\t\t\t\"key\": \"剧情\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"运动\",\n" +
            "\t\t\t\"key\": \"运动\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"短片\",\n" +
            "\t\t\t\"key\": \"短片\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"优酷出品\",\n" +
            "\t\t\t\"key\": \"优酷出品\"\n" +
            "\t\t}]\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"name\": \"综艺\",\n" +
            "\t\t\"key\": \"85\",\n" +
            "\t\t\"type\": \"show\",\n" +
            "\t\t\"list\": [{\n" +
            "\t\t\t\"name\": \"全部\",\n" +
            "\t\t\t\"key\": \"\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"相声\",\n" +
            "\t\t\t\"key\": \"相声\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"喜剧\",\n" +
            "\t\t\t\"key\": \"喜剧\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"音乐\",\n" +
            "\t\t\t\"key\": \"音乐\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"舞蹈\",\n" +
            "\t\t\t\"key\": \"舞蹈\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"偶像\",\n" +
            "\t\t\t\"key\": \"偶像\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"情感\",\n" +
            "\t\t\t\"key\": \"情感\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"婚恋\",\n" +
            "\t\t\t\"key\": \"婚恋\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"体育\",\n" +
            "\t\t\t\"key\": \"体育\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"明星访谈\",\n" +
            "\t\t\t\"key\": \"明星访谈\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"旅游\",\n" +
            "\t\t\t\"key\": \"旅游\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"文化\",\n" +
            "\t\t\t\"key\": \"文化\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"时尚\",\n" +
            "\t\t\t\"key\": \"时尚\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"美食\",\n" +
            "\t\t\t\"key\": \"美食\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"生活\",\n" +
            "\t\t\t\"key\": \"生活\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"益智\",\n" +
            "\t\t\t\"key\": \"益智\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"晚会\",\n" +
            "\t\t\t\"key\": \"晚会\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"游戏\",\n" +
            "\t\t\t\"key\": \"游戏\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"亲子\",\n" +
            "\t\t\t\"key\": \"亲子\"\n" +
            "\t\t}]\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"name\": \"动漫\",\n" +
            "\t\t\"key\": \"100\",\n" +
            "\t\t\"type\": \"show\",\n" +
            "\t\t\"list\": [{\n" +
            "\t\t\t\"name\": \"全部\",\n" +
            "\t\t\t\"key\": \"\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"热血\",\n" +
            "\t\t\t\"key\": \"热血\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"格斗\",\n" +
            "\t\t\t\"key\": \"格斗\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"恋爱\",\n" +
            "\t\t\t\"key\": \"恋爱\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"美少女\",\n" +
            "\t\t\t\"key\": \"美少女\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"校园\",\n" +
            "\t\t\t\"key\": \"校园\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"搞笑\",\n" +
            "\t\t\t\"key\": \"搞笑\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"LOLI\",\n" +
            "\t\t\t\"key\": \"LOLI\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"神魔\",\n" +
            "\t\t\t\"key\": \"神魔\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"机战\",\n" +
            "\t\t\t\"key\": \"机战\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"科幻\",\n" +
            "\t\t\t\"key\": \"科幻\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"真人\",\n" +
            "\t\t\t\"key\": \"真人\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"青春\",\n" +
            "\t\t\t\"key\": \"青春\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"魔法\",\n" +
            "\t\t\t\"key\": \"魔法\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"神话\",\n" +
            "\t\t\t\"key\": \"神话\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"冒险\",\n" +
            "\t\t\t\"key\": \"冒险\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"运动\",\n" +
            "\t\t\t\"key\": \"运动\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"竞技\",\n" +
            "\t\t\t\"key\": \"竞技\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"童话\",\n" +
            "\t\t\t\"key\": \"童话\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"亲子\",\n" +
            "\t\t\t\"key\": \"亲子\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"教育\",\n" +
            "\t\t\t\"key\": \"教育\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"励志\",\n" +
            "\t\t\t\"key\": \"励志\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"剧情\",\n" +
            "\t\t\t\"key\": \"剧情\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"社会\",\n" +
            "\t\t\t\"key\": \"社会\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"历史\",\n" +
            "\t\t\t\"key\": \"历史\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"战争\",\n" +
            "\t\t\t\"key\": \"战争\"\n" +
            "\t\t}]\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"name\": \"少儿\",\n" +
            "\t\t\"key\": \"177\",\n" +
            "\t\t\"type\": \"show\",\n" +
            "\t\t\"list\": [{\n" +
            "\t\t\t\"name\": \"全部\",\n" +
            "\t\t\t\"key\": \"\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"动画\",\n" +
            "\t\t\t\"key\": \"动画\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"儿歌\",\n" +
            "\t\t\t\"key\": \"儿歌\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"绘本故事\",\n" +
            "\t\t\t\"key\": \"绘本故事\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"玩具\",\n" +
            "\t\t\t\"key\": \"玩具\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"早教\",\n" +
            "\t\t\t\"key\": \"早教\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"艺术\",\n" +
            "\t\t\t\"key\": \"艺术\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"探索纪实\",\n" +
            "\t\t\t\"key\": \"探索纪实\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"少儿综艺\",\n" +
            "\t\t\t\"key\": \"少儿综艺\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"亲子\",\n" +
            "\t\t\t\"key\": \"亲子\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"英语\",\n" +
            "\t\t\t\"key\": \"英语\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"国学\",\n" +
            "\t\t\t\"key\": \"国学\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"课程辅导\",\n" +
            "\t\t\t\"key\": \"课程辅导\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"人际交往\",\n" +
            "\t\t\t\"key\": \"人际交往\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"情商\",\n" +
            "\t\t\t\"key\": \"情商\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"认知启蒙\",\n" +
            "\t\t\t\"key\": \"认知启蒙\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"科普\",\n" +
            "\t\t\t\"key\": \"科普\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"冒险\",\n" +
            "\t\t\t\"key\": \"冒险\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"幽默\",\n" +
            "\t\t\t\"key\": \"幽默\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"友情\",\n" +
            "\t\t\t\"key\": \"友情\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"益智\",\n" +
            "\t\t\t\"key\": \"益智\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"战斗\",\n" +
            "\t\t\t\"key\": \"战斗\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"科幻\",\n" +
            "\t\t\t\"key\": \"科幻\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"魔法\",\n" +
            "\t\t\t\"key\": \"魔法\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"亲情\",\n" +
            "\t\t\t\"key\": \"亲情\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"数学\",\n" +
            "\t\t\t\"key\": \"数学\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"动物\",\n" +
            "\t\t\t\"key\": \"动物\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"热血\",\n" +
            "\t\t\t\"key\": \"热血\"\n" +
            "\t\t}]\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"name\": \"音乐\",\n" +
            "\t\t\"key\": \"95\",\n" +
            "\t\t\"type\": \"show\",\n" +
            "\t\t\"list\": [{\n" +
            "\t\t\t\"name\": \"全部\",\n" +
            "\t\t\t\"key\": \"\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"音乐MV\",\n" +
            "\t\t\t\"key\": \"音乐MV\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"舞蹈\",\n" +
            "\t\t\t\"key\": \"舞蹈\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"现场版\",\n" +
            "\t\t\t\"key\": \"现场版\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"演唱会\",\n" +
            "\t\t\t\"key\": \"演唱会\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"电影原声\",\n" +
            "\t\t\t\"key\": \"电影原声\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"电视剧原声\",\n" +
            "\t\t\t\"key\": \"电视剧原声\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"动漫音乐\",\n" +
            "\t\t\t\"key\": \"动漫音乐\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"游戏音乐\",\n" +
            "\t\t\t\"key\": \"游戏音乐\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"广告音乐\",\n" +
            "\t\t\t\"key\": \"广告音乐\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"同人\",\n" +
            "\t\t\t\"key\": \"同人\",\n" +
            "\t\t\t\"hide\": true\n" +
            "\t\t}]\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"name\": \"教育\",\n" +
            "\t\t\"key\": \"87\",\n" +
            "\t\t\"type\": \"show\",\n" +
            "\t\t\"list\": [{\n" +
            "\t\t\t\"name\": \"全部\",\n" +
            "\t\t\t\"key\": \"\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"公开课\",\n" +
            "\t\t\t\"key\": \"公开课\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"名人名嘴\",\n" +
            "\t\t\t\"key\": \"名人名嘴\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"文化\",\n" +
            "\t\t\t\"key\": \"文化\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"艺术\",\n" +
            "\t\t\t\"key\": \"艺术\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"伦理社会\",\n" +
            "\t\t\t\"key\": \"伦理社会\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"理工\",\n" +
            "\t\t\t\"key\": \"理工\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"历史\",\n" +
            "\t\t\t\"key\": \"历史\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"心理学\",\n" +
            "\t\t\t\"key\": \"心理学\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"经济\",\n" +
            "\t\t\t\"key\": \"经济\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"政治\",\n" +
            "\t\t\t\"key\": \"政治\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"管理学\",\n" +
            "\t\t\t\"key\": \"管理学\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"外语\",\n" +
            "\t\t\t\"key\": \"外语\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"法律\",\n" +
            "\t\t\t\"key\": \"法律\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"计算机\",\n" +
            "\t\t\t\"key\": \"计算机\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"哲学\",\n" +
            "\t\t\t\"key\": \"哲学\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"职业培训\",\n" +
            "\t\t\t\"key\": \"职业培训\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"家庭教育\",\n" +
            "\t\t\t\"key\": \"家庭教育\"\n" +
            "\t\t}]\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"name\": \"纪录片\",\n" +
            "\t\t\"key\": \"84\",\n" +
            "\t\t\"type\": \"show\",\n" +
            "\t\t\"list\": [{\n" +
            "\t\t\t\"name\": \"全部\",\n" +
            "\t\t\t\"key\": \"\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"人物\",\n" +
            "\t\t\t\"key\": \"人物\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"军事\",\n" +
            "\t\t\t\"key\": \"军事\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"历史\",\n" +
            "\t\t\t\"key\": \"历史\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"自然\",\n" +
            "\t\t\t\"key\": \"自然\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"探险\",\n" +
            "\t\t\t\"key\": \"探险\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"科技\",\n" +
            "\t\t\t\"key\": \"科技\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"文化\",\n" +
            "\t\t\t\"key\": \"文化\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"刑侦\",\n" +
            "\t\t\t\"key\": \"刑侦\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"社会\",\n" +
            "\t\t\t\"key\": \"社会\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"旅游\",\n" +
            "\t\t\t\"key\": \"旅游\"\n" +
            "\t\t}]\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"name\": \"体育\",\n" +
            "\t\t\"key\": \"98\",\n" +
            "\t\t\"type\": \"show\",\n" +
            "\t\t\"list\": [{\n" +
            "\t\t\t\"name\": \"全部\",\n" +
            "\t\t\t\"key\": \"\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"奥运会\",\n" +
            "\t\t\t\"key\": \"奥运会\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"世界杯\",\n" +
            "\t\t\t\"key\": \"世界杯\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"格斗\",\n" +
            "\t\t\t\"key\": \"格斗\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"足球\",\n" +
            "\t\t\t\"key\": \"足球\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"篮球\",\n" +
            "\t\t\t\"key\": \"篮球\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"健身\",\n" +
            "\t\t\t\"key\": \"健身\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"跑步\",\n" +
            "\t\t\t\"key\": \"跑步\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"广场舞\",\n" +
            "\t\t\t\"key\": \"广场舞\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"综合\",\n" +
            "\t\t\t\"key\": \"综合\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"棋牌\",\n" +
            "\t\t\t\"key\": \"棋牌\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"电竞\",\n" +
            "\t\t\t\"key\": \"电竞\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"冰壶\",\n" +
            "\t\t\t\"key\": \"冰壶\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"冰球\",\n" +
            "\t\t\t\"key\": \"冰球\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"滑雪\",\n" +
            "\t\t\t\"key\": \"滑雪\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"滑冰\",\n" +
            "\t\t\t\"key\": \"滑冰\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"雪车雪撬\",\n" +
            "\t\t\t\"key\": \"雪车雪撬\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"射击\",\n" +
            "\t\t\t\"key\": \"射击\"\n" +
            "\t\t}]\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"name\": \"文化\",\n" +
            "\t\t\"key\": \"178\",\n" +
            "\t\t\"type\": \"show\",\n" +
            "\t\t\"list\": [{\n" +
            "\t\t\t\"name\": \"全部\",\n" +
            "\t\t\t\"key\": \"\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"谈话\",\n" +
            "\t\t\t\"key\": \"谈话\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"脱口秀\",\n" +
            "\t\t\t\"key\": \"脱口秀\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"访谈\",\n" +
            "\t\t\t\"key\": \"访谈\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"演讲辩论\",\n" +
            "\t\t\t\"key\": \"演讲辩论\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"培训课程\",\n" +
            "\t\t\t\"key\": \"培训课程\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"有声书\",\n" +
            "\t\t\t\"key\": \"有声书\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"社会\",\n" +
            "\t\t\t\"key\": \"社会\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"历史\",\n" +
            "\t\t\t\"key\": \"历史\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"文学\",\n" +
            "\t\t\t\"key\": \"文学\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"艺术\",\n" +
            "\t\t\t\"key\": \"艺术\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"人物\",\n" +
            "\t\t\t\"key\": \"人物\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"旅游\",\n" +
            "\t\t\t\"key\": \"旅游\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"政治\",\n" +
            "\t\t\t\"key\": \"政治\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"教育\",\n" +
            "\t\t\t\"key\": \"教育\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"军事\",\n" +
            "\t\t\t\"key\": \"军事\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"戏曲\",\n" +
            "\t\t\t\"key\": \"戏曲\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"演出\",\n" +
            "\t\t\t\"key\": \"演出\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"生活百科\",\n" +
            "\t\t\t\"key\": \"生活百科\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"音频节目\",\n" +
            "\t\t\t\"key\": \"音频节目\"\n" +
            "\t\t}]\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"name\": \"娱乐\",\n" +
            "\t\t\"key\": \"86\",\n" +
            "\t\t\"type\": \"show\",\n" +
            "\t\t\"list\": [{\n" +
            "\t\t\t\"name\": \"全部\",\n" +
            "\t\t\t\"key\": \"\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"明星资讯\",\n" +
            "\t\t\t\"key\": \"明星资讯\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"电影资讯\",\n" +
            "\t\t\t\"key\": \"电影资讯\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"电视资讯\",\n" +
            "\t\t\t\"key\": \"电视资讯\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"音乐资讯\",\n" +
            "\t\t\t\"key\": \"音乐资讯\"\n" +
            "\t\t}]\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"name\": \"搞笑\",\n" +
            "\t\t\"key\": \"94\",\n" +
            "\t\t\"list\": [{\n" +
            "\t\t\t\"name\": \"全部\",\n" +
            "\t\t\t\"key\": \"\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"恶搞短片\",\n" +
            "\t\t\t\"key\": \"235\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"搞笑自拍\",\n" +
            "\t\t\t\"key\": \"236\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"萌宠奇趣\",\n" +
            "\t\t\t\"key\": \"238\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"搞笑达人\",\n" +
            "\t\t\t\"key\": \"3072\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"影视剧吐槽\",\n" +
            "\t\t\t\"key\": \"3091\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"恶搞配音\",\n" +
            "\t\t\t\"key\": \"3092\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"欢乐街访\",\n" +
            "\t\t\t\"key\": \"3093\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"name\": \"鬼畜\",\n" +
            "\t\t\t\"key\": \"3094\"\n" +
            "\t\t}]\n" +
            "\t},\n" +
            "]";

    @Override
    public List<Title> types() {
        List<Title> titles = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(JSON_STRING);
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.optJSONObject(i);
                titles.add(new Title(object.optString("name"), object.optString("key")));
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
            JSONArray array = new JSONArray(JSON_STRING);
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.optJSONObject(i);
                if (url.equals(object.optString("key"))) {
                    array = object.optJSONArray("list");
                    if (array == null || array.length() <= 0)
                        return titles;
                    for (i = 0; i < array.length(); i++) {
                        object = array.optJSONObject(i);
                        String key = object.optString("key");
                        if (key == null || key.isEmpty())
                            continue;
                        String name = object.optString("name");
                        Title title = new Title(name, String.format("https://www.youku.com/category/data?c=%s&g=%s&p=1&type=show", url, URLEncoder.encode(key)));
                        Log.e(TAG, title.url);
                        titles.add(title);
                    }
                    break;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return titles;
    }

    @Override
    public String name() {
        return "优酷视频";
    }

    @Override
    public ListMove list(String url) {
        Log.e(TAG, url);
        ListMove move = new ListMove();
        List<Detial> detials = new ArrayList<>();
        move.detials = detials;
        Pattern pattern = Pattern.compile("c=([^&]*)");
        Matcher matcher = pattern.matcher(url);
        if (matcher.find()) {
            String referer = String.format("https://www.youku.com/category/show/c_%s.html?theme=dark", matcher.group(1));
            Map<String, String> map = new HashMap<>();
            map.put("referer", referer);
            String text = Util.get(url, map);
            Log.e(TAG, text);
            try {
                JSONObject object = new JSONObject(text);
                object = object.optJSONObject("data");
                if (object == null)
                    return move;
                JSONArray array = object.optJSONArray("categoryVideos");
                if (array == null || array.length() <= 0)
                    return move;
                for (int i = 0; i < array.length(); i++) {
                    object = array.optJSONObject(i);
                    Detial detial = new Detial();
                    detial.img = Util.dealWithUrl(object.optString("img"), url, HOST);
                    detial.name = object.optString("title");
                    detial.detialUrl = Util.dealWithUrl(object.optString("videoLink"), url, HOST);
                    detials.add(detial);
                }
                Log.e(TAG, "" + detials.size());
                if (detials.size() >= 84) {
//                    https://www.youku.com/category/data?c=%s&g=%s&p=1&type=show"
                    pattern = Pattern.compile("p=([0-9]*)");
                    matcher = pattern.matcher(url);
                    if (matcher.find()) {
                        int p = Integer.valueOf(matcher.group(1));
                        move.next = url.replace("p=" + p, "p=" + (p + 1));
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return move;
    }

    @Override
    public boolean playDetail(Detial detial) {
        List<IVip> vips = PlatformsManager.vips;
        List<Detial.DetailPlayUrl> detailPlayUrls = new ArrayList<>();
        Detial.DetailPlayUrl playUrl = new Detial.DetailPlayUrl();
        playUrl.title = detial.name;
        playUrl.href = detial.detialUrl;
        detailPlayUrls.add(playUrl);
        detial.playUrls = detailPlayUrls;
        return true;
    }

    private static class Play {
        String url = "";
    }

    @Override
    public String play(Detial.DetailPlayUrl playUrl) {
        IVip vip = PlatformsManager.vip;
        Log.e("playda", playUrl.href);
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
