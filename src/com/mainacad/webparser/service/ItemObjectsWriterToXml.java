package com.mainacad.webparser.service;

import com.mainacad.webparser.model.Item;
import com.mainacad.webparser.model.ItemsList;
import com.mainacad.webparser.model.Seller;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import java.io.FileWriter;
import java.io.StringWriter;

public class ItemObjectsWriterToXml implements ItemObjectsWriter {
  @Override
  public void writeObjectsToFile(ItemsList itemsList, String fileName) {
    StringWriter writer = new StringWriter();

    try (StringWriter stringWriter = new StringWriter();
         FileWriter fileWriter = new FileWriter(fileName);
    ) {
      JAXBContext context = JAXBContext.newInstance(Seller.class, Item.class, ItemsList.class);
      Marshaller marshaller = null;

      marshaller = context.createMarshaller();
      marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
      marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_ENCODING, "UTF-8");
      marshaller.marshal(itemsList, writer);

      fileWriter.write(writer.toString());
      fileWriter.flush();

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
