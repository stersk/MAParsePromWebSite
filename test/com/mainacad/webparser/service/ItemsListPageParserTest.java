package com.mainacad.webparser.service;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ItemsListPageParserTest {

  @Test
  void testParse() {
    List<String> itemsUrlList = ItemsListPageParser.parse("https://prom.ua/ua/Klaviatury-dlya-noutbukov-i-netbukov");
    assertNotNull(itemsUrlList);
    assertFalse(itemsUrlList.isEmpty());;
  }
}