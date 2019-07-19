package com.mainacad.webparser.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mainacad.webparser.model.Item;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class ItemObjectsWriterToJson implements ItemObjectsWriter {
  @Override
  public void writeObjectsToFile(List<Item> itemsList, String fileName) {
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      objectMapper.writeValue(new File(fileName), itemsList);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
