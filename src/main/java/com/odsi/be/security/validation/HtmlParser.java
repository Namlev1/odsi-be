package com.odsi.be.security.validation;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.stereotype.Service;

@Service
public class HtmlParser {
    public String sanitizeContent(String content) {
        Safelist safelist = Safelist.basicWithImages()
                .addTags("h1", "h2", "h3", "br");
        return Jsoup.clean(content, safelist);
    }
}
