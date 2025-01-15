package com.odsi.be.security.validation;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.stereotype.Service;

@Service
public class HtmlParser {
    public String sanitizeContent(String content) {
        return Jsoup.clean(content, Safelist.basicWithImages());
    }
}