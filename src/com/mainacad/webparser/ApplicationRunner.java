package com.mainacad.webparser;

import com.mainacad.webparser.model.Item;
import com.mainacad.webparser.service.ItemPageParser;

import java.util.logging.Logger;

public class ApplicationRunner {
  static Logger logger = Logger.getLogger(ApplicationRunner.class.getName());

  public static void main(String[] args) {
//    Item item = ItemPageParser.parse("https://prom.ua/ua/p712140849-klaviatura-asus-g750.html");
//    Item item = ItemPageParser.parse("https://prom.ua/ua/p964869451-arduino-uno-nabor.html");
    Item item = ItemPageParser.parse("https://prom.ua/ua/p952556215-original-klaviatura-macbook.html");

    logger.info("\n" + item.toString());
  }
}
