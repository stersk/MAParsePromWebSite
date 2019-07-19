package com.mainacad.webparser.service;

import com.mainacad.webparser.model.ItemsList;

public interface ItemObjectsWriter {
  void writeObjectsToFile(ItemsList itemsList, String fileName);
}
