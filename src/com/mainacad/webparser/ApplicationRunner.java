package com.mainacad.webparser;

import com.mainacad.webparser.model.Item;
import com.mainacad.webparser.service.ItemObjectsWriter;
import com.mainacad.webparser.service.ItemObjectsWriterToJson;
import com.mainacad.webparser.service.ItemPageParser;
import com.mainacad.webparser.service.ItemsListPageParser;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class ApplicationRunner {
  static Logger logger = Logger.getLogger(ApplicationRunner.class.getName());


  public static void main(String[] args) {
    List<String> itemsUrlList = ItemsListPageParser.parse("https://prom.ua/ua/Klaviatury-dlya-noutbukov-i-netbukov");
    List<Item> items = new CopyOnWriteArrayList<>();

    ExecutorService executorService = Executors.newFixedThreadPool(5);
    for (String itemUrl: itemsUrlList) {
      ItemPageParser pageParser = new ItemPageParser(itemUrl, items);
      executorService.submit(pageParser);
    }

    executorService.shutdown();
    try {
      executorService.awaitTermination(5, TimeUnit.MINUTES);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    ItemObjectsWriter itemsWriter = new ItemObjectsWriterToJson();
    itemsWriter.writeObjectsToFile(items, "files/items.json");
  }
}
