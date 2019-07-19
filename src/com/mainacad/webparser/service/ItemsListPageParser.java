package com.mainacad.webparser.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ItemsListPageParser {
  public static List<String> parse(String url) {
    List<String> items = new ArrayList<>();

    try {
      Document document = Jsoup.connect(url).get();
      Element itemsCatalog = getItemsCatalogElement(document);
      if (itemsCatalog != null) {
        Elements itemsElements = itemsCatalog.getElementsByClass("x-gallery-tile");
        for (Element itemElement: itemsElements) {
          if (itemElement.hasClass("x-gallery-tile") && itemElement.hasAttr("data-advtracking-click-url")) {
            items.add(itemElement.attr("data-advtracking-click-url"));
          }
        }
      }

    } catch (IOException e) {
      e.printStackTrace();
    }

    return items;
  }

  private static Element getItemsCatalogElement(Document document) {
    Element catalogElement = null;

    Elements catalogElements = document.getElementsByAttributeValue("data-qaid", "product_gallery");
    if (catalogElements != null && catalogElements.size() > 0) {
      catalogElement = catalogElements.first();
    }

    return catalogElement;
  }
}
