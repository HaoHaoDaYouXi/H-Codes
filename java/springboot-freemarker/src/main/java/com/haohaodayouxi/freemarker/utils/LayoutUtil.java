package com.haohaodayouxi.freemarker.utils;

import javax.servlet.http.HttpServletRequest;

/**
 * 定义模板页面
 *
 * @author TONE
 * @date 2024/9/01
 */
public class LayoutUtil {

    /**
     * freemarker 布局视图解析
     *
     * @param url
     * @param request
     */
    public static String freeMarkerIndexResult(String url, HttpServletRequest request) {
        // 布局模板
        String layout = "/layout/LayoutIndex.ftl";
        // 页面参数 url需要补充一个 / ，否则会在layout目录下寻找对应文件
        request.setAttribute("body_file_path", "/" + url);
        // 使用布局页
        return layout.replace(".ftl", "");
    }
}
