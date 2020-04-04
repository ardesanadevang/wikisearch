package com.example.wikisearch.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public final class WebServices_Url {

    public static String getSearchUrl(String searchText) {
        try {
            String text = URLEncoder.encode(searchText, "UTF-8");
            return "https://en.wikipedia.org//w/api.php?action=query&format=json&prop=pageimages%7Cpageterms&generator=prefixsearch&redirects=1&formatversion=2&piprop=thumbnail&pithumbsize=50&pilimit=10&wbptterms=description&gpssearch=" + text + "&gpslimit=10";
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getPageDetailUrl(String pageId) {
        try {
            String text = URLEncoder.encode(pageId, "UTF-8");
            return "https://en.wikipedia.org/w/api.php?action=query&prop=info&pageids=" + text + "&inprop=url&format=json";
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

}
