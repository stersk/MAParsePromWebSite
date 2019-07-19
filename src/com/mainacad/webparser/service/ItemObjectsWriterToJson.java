package com.mainacad.webparser.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mainacad.webparser.model.ItemsList;

import java.io.File;
import java.io.IOException;

public class ItemObjectsWriterToJson implements ItemObjectsWriter {
  @Override
  public void writeObjectsToFile(ItemsList itemsList, String fileName) {
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      objectMapper.writeValue(new File(fileName), itemsList.getItems());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
