package com.github.catvod.spider;

import android.util.Log;
import com.github.catvod.crawler.SpiderDebug;
import com.github.catvod.crawler.SpiderReq;
import com.github.catvod.crawler.SpiderUrl;
import com.github.catvod.utils.Misc;
import com.github.catvod.utils.okhttp.OKCallBack;
import com.github.catvod.utils.okhttp.OkHttpUtil;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Douban extends Spider {

    private static final String TAG = "DoubanSpider";
    private static final String SITE = "https://movie.douban.com";
    private static final String SEARCH_API = SITE + "/j/search_subjects?type=movie&tag=%s&page_limit=20&page_start=%d";

    /**
     * 获取分类列表
     * @return 分类列表的JSON字符串
     */
    @Override
    public String categoryContent(String tid, String pg, boolean filter, HashMap<String, String> extend) {
        String url = String.format(SEARCH_API, tid, (Integer.parseInt(pg) - 1) * 20);
        try {
            String json = OkHttpUtil.string(url);
            JSONObject obj = new JSONObject(json);
            JSONArray subjects = obj.getJSONArray("subjects");
            List<Map<String, String>> videos = new ArrayList<>();
            for (int i = 0; i < subjects.length(); i++) {
                JSONObject subject = subjects.getJSONObject(i);
                Map<String, String> video = new HashMap<>();
                video.put("vod_id", subject.getString("id"));
                video.put("vod_name", subject.getString("title"));
                video.put("vod_pic", subject.getString("cover"));
                video.put("vod_remarks", subject.getString("rate"));
                videos.add(video);
            }
            JSONObject result = new JSONObject();
            result.put("list", videos);
            result.put("page", pg);
            result.put("pagecount", "9999");
            result.put("limit", "20");
            result.put("total", "999999");
            return result.toString();
        } catch (Exception e) {
            SpiderDebug.log(e);
        }
        return "";
    }

    /**
     * 获取视频详情
     * @param ids 视频ID
     * @return 视频详情的JSON字符串
     */
    @Override
    public String detailContent(List<String> ids) {
        try {
            String id = ids.get(0);
            String url = SITE + "/subject/" + id;
            String html = OkHttpUtil.string(url);
            Map<String, String> video = new HashMap<>();
            video.put("vod_id", id);
            video.put("vod_name", Misc.jsoupGetText(html, "h1 span"));
            video.put("vod_pic", Misc.jsoupGetAttr(html, "a.nbg img", "src"));
            video.put("type_name", Misc.jsoupGetText(html, "span[property='v:genre']"));
            video.put("vod_year", Misc.jsoupGetText(html, "span[property='v:initialReleaseDate']"));
            video.put("vod_area", Misc.jsoupGetText(html, "span[property='v:region']"));
            video.put("vod_actor", Misc.jsoupGetText(html, "a[rel='v:starring']"));
            video.put("vod_director", Misc.jsoupGetText(html, "a[rel='v:directedBy']"));
            video.put("vod_content", Misc.jsoupGetText(html, "span[property='v:summary']"));
            video.put("vod_play_from", "豆瓣");
            video.put("vod_play_url", "播放$" + SITE + "/player?mid=" + id);
            JSONObject result = new JSONObject();
            result.put("list", new JSONArray().put(video));
            return result.toString();
        } catch (Exception e) {
            SpiderDebug.log(e);
        }
        return "";
    }

    /**
     * 搜索视频
     * @param key 关键字
     * @param quick 是否快速搜索
     * @param filter 过滤条件
     * @param extend 扩展参数
     * @return 搜索结果的JSON字符串
     */
    @Override
    public String searchContent(String key, boolean quick, boolean filter, HashMap<String, String> extend) {
        String url = String.format(SEARCH_API, "全部", 0) + "&q=" + key;
        try {
            String json = OkHttpUtil.string(url);
            JSONObject obj = new JSONObject(json);
            JSONArray subjects = obj.getJSONArray("subjects");
            List<Map<String, String>> videos = new ArrayList<>();
            for (int i = 0; i < subjects.length(); i++) {
                JSONObject subject = subjects.getJSONObject(i);
                Map<String, String> video = new HashMap<>();
                video.put("vod_id", subject.getString("id"));
                video.put("vod_name", subject.getString("title"));
                video.put("vod_pic", subject.getString("cover"));
                video.put("vod_remarks", subject.getString("rate"));
                videos.add(video);
            }
            JSONObject result = new JSONObject();
            result.put("list", videos);
            result.put("page", "1");
            result.put("pagecount", "9999");
            result.put("limit", "20");
            result.put("total", "999999");
            return result.toString();
        } catch (Exception e) {
            SpiderDebug.log(e);
        }
        return "";
    }

    /**
     * 获取播放地址
     * @param flag 播放标志
     * @param id 视频ID
     * @param flags 播放标志列表
     * @return 播放地址的JSON字符串
     */
    @Override
    public String playerContent(String flag, String id, List<String> flags) {
        try {
            String url = SITE + "/player?mid=" + id;
            String html = OkHttpUtil.string(url);
            String playUrl = Misc.jsoupGetAttr(html, "source", "src");
            JSONObject result = new JSONObject();
            result.put("parse", 0);
            result.put("playUrl", "");
            result.put("url", playUrl);
            return result.toString();
        } catch (Exception e) {
            SpiderDebug.log(e);
        }
        return "";
    }
}