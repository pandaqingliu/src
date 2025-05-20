package com.github.catvod.spider;

import android.util.Log;
import com.github.catvod.crawler.SpiderDebug;
import com.github.catvod.crawler.SpiderReq;
import com.github.catvod.crawler.SpiderUrl;
import com.github.catvod.utils.Misc;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class Anime1 extends FastSpider {

    // 定义日志标签
    private static final String TAG = "Anime1";

    // 定义请求头信息
    private static final Map<String, String> headers = new HashMap<>();

    static {
        headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");
        headers.put("Referer", "https://www.example.com/");
    }

    // 获取分类信息
    @Override
    public String categoryContent(String tid, String pg, boolean filter, HashMap<String, String> extend) {
        try {
            // 构建请求URL
            String url = String.format("https://api.example.com/category/%s?page=%s", tid, pg);
            // 发送HTTP请求并获取响应
            String response = SpiderReq.get(url, headers);
            // 解析JSON响应
            JSONObject jsonObject = new JSONObject(response);
            JSONArray list = jsonObject.getJSONArray("list");
            StringBuilder sb = new StringBuilder();
            // 遍历JSON数组并构建结果字符串
            for (int i = 0; i < list.length(); i++) {
                JSONObject item = list.getJSONObject(i);
                String name = item.getString("name");
                String id = item.getString("id");
                String pic = item.getString("pic");
                sb.append("{\"vod_id\":\"").append(id).append("\",\"vod_name\":\"").append(name).append("\",\"vod_pic\":\"").append(pic).append("\"},");
            }
            // 返回结果字符串
            return resultJson(extend, tid, pg, sb.toString().replaceAll(",$", ""), "true");
        } catch (Exception e) {
            // 打印异常信息
            SpiderDebug.log(e);
        }
        // 返回空字符串
        return "";
    }

    // 获取视频详情信息
    @Override
    public String detailContent(List<String> ids) {
        try {
            // 获取视频ID
            String id = ids.get(0);
            // 构建请求URL
            String url = String.format("https://api.example.com/detail/%s", id);
            // 发送HTTP请求并获取响应
            String response = SpiderReq.get(url, headers);
            // 解析JSON响应
            JSONObject jsonObject = new JSONObject(response);
            JSONObject data = jsonObject.getJSONObject("data");
            String name = data.getString("name");
            String pic = data.getString("pic");
            String plot = data.getString("plot");
            String playUrl = data.getString("play_url");
            // 构建结果字符串
            return resultJson(new String[]{"vod_id", "vod_name", "vod_pic", "vod_plot", "vod_play_from", "vod_play_url"},
                    new String[]{id, name, pic, plot, "线路1", playUrl});
        } catch (Exception e) {
            // 打印异常信息
            SpiderDebug.log(e);
        }
        // 返回空字符串
        return "";
    }

    // 搜索视频
    @Override
    public String searchContent(String keyword, boolean quick) {
        try {
            // 构建请求URL
            String url = String.format("https://api.example.com/search?q=%s", keyword);
            // 发送HTTP请求并获取响应
            String response = SpiderReq.get(url, headers);
            // 解析JSON响应
            JSONObject jsonObject = new JSONObject(response);
            JSONArray list = jsonObject.getJSONArray("list");
            StringBuilder sb = new StringBuilder();
            // 遍历JSON数组并构建结果字符串
            for (int i = 0; i < list.length(); i++) {
                JSONObject item = list.getJSONObject(i);
                String name = item.getString("name");
                String id = item.getString("id");
                String pic = item.getString("pic");
                sb.append("{\"vod_id\":\"").append(id).append("\",\"vod_name\":\"").append(name).append("\",\"vod_pic\":\"").append(pic).append("\"},");
            }
            // 返回结果字符串
            return resultJson(null, null, "1", sb.toString().replaceAll(",$", ""), "true");
        } catch (Exception e) {
            // 打印异常信息
            SpiderDebug.log(e);
        }
        // 返回空字符串
        return "";
    }
}
