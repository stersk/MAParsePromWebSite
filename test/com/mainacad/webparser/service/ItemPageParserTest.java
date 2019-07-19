package com.mainacad.webparser.service;

import com.mainacad.webparser.model.Item;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ItemPageParserTest {
  @Test
  void testRun() {
    List<Item> items = new ArrayList<>();
    ItemPageParser itemPageParser = new ItemPageParser("https://prom.ua/ua/p952556215-original-klaviatura-macbook.html", items);
    itemPageParser.run();

    assertNotNull(items);
    assertEquals(1, items.size());
    assertEquals(items.get(0).toString(), "Item(code=Я8408, name=Original клавиатура MacBook Air 13\" 2011-2017 A1369/A1466 US, Original клавіатура MacBook Air 13 \"2011-2017 A1369 / A1466 US, price=94500, priceWithoutDiscount=111200, wholesalePrices={}, imageUrls=[https://images.ua.prom.st/1739541015_w640_h640_original-klaviatura-macbook.jpg], itemAvailability=ENDS, hasGifts=false, seller=Seller(name=Интернет-магазин «Джимарт», siteUrl=https://gmart.in.ua/))");
  }
}