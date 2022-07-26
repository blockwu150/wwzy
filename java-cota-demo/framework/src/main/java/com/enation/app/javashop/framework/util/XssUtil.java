package com.enation.app.javashop.framework.util;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import java.util.Arrays;
import java.util.List;

/**
 * @description: xss过滤
 * @author: liuyulei
 * @create: 2019-12-11 11:39
 * @version:1.0
 * @since:7.1.4
 **/
public class XssUtil {


    public static String clean(String value) {
        //此处定义的标签是Whitelist.basicWithImages()中可以放过的标签
        List<String> tags = Arrays.asList("a", "b", "blockquote", "br", "cite", "code", "dd", "trigger", "dt", "em", "i", "li", "ol", "p", "pre", "q", "small",
                "span", "strike", "strong", "sub", "sup", "u", "ul","table","tbody","tr","td","img", "video", "source");
        Whitelist whiteList = Whitelist.basicWithImages();

        for(String tag : tags){
            whiteList.addAttributes(tag,"style","class","width","align","src", "controls", "preload", "height", "data-setup", "type");
        }

        value = Jsoup.clean(value, whiteList);

        return value.replace("&amp;","&");
    }


}
