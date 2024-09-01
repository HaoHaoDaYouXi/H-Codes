package com.haohaodayouxi.freemarker.config.freemarker;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.view.freemarker.FreeMarkerView;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.Map;
import java.util.Properties;

/**
 * 自定义FreeMarkerView 统一配置静态资源路径，ftl页面使用 ${static}直接使用
 * 如：<script type="text/javascript" src="${static}/js/jquery.min.js"></script>
 *
 * @author TONE
 * @date 2024/9/01
 */
@Slf4j
public class MyFreemarkerView extends FreeMarkerView {

    @Override
    public void exposeHelpers(Map<String, Object> model, HttpServletRequest request) throws Exception {
        InputStream is = this.getClass().getResourceAsStream("/static.properties");
        Properties properties = new Properties();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            properties.load(br);
            model.put("static", properties.getProperty("static"));
        } catch (FileNotFoundException e) {
            log.error("未找到该文件", e);
        } catch (IOException e) {
            log.error("读取文件异常", e);
        } finally {
            is.close();
        }
        super.exposeHelpers(model, request);
    }
}
