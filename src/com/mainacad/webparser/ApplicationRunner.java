package com.mainacad.webparser;

import com.mainacad.webparser.model.Item;
import com.mainacad.webparser.model.ItemsList;
import com.mainacad.webparser.service.*;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class ApplicationRunner {
  static Logger logger = Logger.getLogger(ApplicationRunner.class.getName());

  public static void main(String[] args) {
    List<String> itemsUrlList = ItemsListPageParser.parse("https://prom.ua/ua/Klaviatury-dlya-noutbukov-i-netbukov");
    ItemsList itemList = new ItemsList();
    List<Item> items = itemList.getItems();

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
    itemsWriter.writeObjectsToFile(itemList, "files/items.json");

    itemsWriter = new ItemObjectsWriterToXml();
    itemsWriter.writeObjectsToFile(itemList, "files/items.xml");
  }
}
