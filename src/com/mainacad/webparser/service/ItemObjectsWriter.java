package com.mainacad.webparser.service;

import com.mainacad.webparser.model.Item;

import java.util.List;

public interface ItemObjectsWriter {
  public void writeObjectsToFile(List<Item> itemsList, String fileName);
}
