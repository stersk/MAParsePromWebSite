package com.mainacad.webparser.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mainacad.webparser.model.Item;
import com.mainacad.webparser.model.ItemAviability;
import com.mainacad.webparser.model.Seller;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

public class ItemPageParser implements Runnable {
  private static Logger logger = Logger.getLogger(ItemPageParser.class.getName());
  private String url;
  private List<Item> itemsList;
  private int treadId;
  private static int treadMaximumId = 0;

  public ItemPageParser() {
    treadId = treadMaximumId++;
  }

  public ItemPageParser(String url, List<Item> itemsList) {
    treadId = treadMaximumId++;
    this.url = url;
    this.itemsList = itemsList;

    logger.info("Thread " + treadId + " created.");
  }

  private Item parse(String url) {
    Item item = new Item();

    try {
      Document document = Jsoup.connect(url).get();

      Element mainItemElement = getMainItemElement(document);

      if (mainItemElement != null) {
        item.setCode(getItemCode(mainItemElement));
        item.setName(getItemName(mainItemElement));
        item.setPrice(getItemPrice(mainItemElement));

        Integer priceWithoutDiscount = getItemPriceWithoutDiscount(mainItemElement);
        item.setPriceWithoutDiscount(priceWithoutDiscount == null ? item.getPrice() : priceWithoutDiscount);

        item.setWholesalePrices(getWholesalePrices(mainItemElement));
        item.setImageUrls(getImageUrls(mainItemElement));
        item.setItemAvailability(getItemAvailability(mainItemElement));
        item.setHasGifts(hasGifts(mainItemElement));
      }

      Element sellerElement = getSellerElement(document);
      
      if (sellerElement != null) {
        item.setSeller(getSeller(sellerElement));
      }

    } catch (IOException e) {
      e.printStackTrace();
    }

    return item;
  }

  private Seller getSeller(Element sellerInfoElement) {
    Seller seller = new Seller();
    Element sellerElement = null;

    Elements sellerElements = sellerInfoElement.getElementsByAttributeValue("data-qaid", "company_name");
    if (sellerElements != null && sellerElements.size() > 0) {
      sellerElement = sellerElements.first();

      seller.setName(sellerElement.ownText().trim());
      String url = sellerElement.attr("href");
      if (url != null) {
        seller.setSiteUrl(url);
      }
    }

    return seller;
  }

  private Element getSellerElement(Document document) {
    Element sellerElement = null;
    Elements sellerElements = document.getElementsByAttributeValue("data-qaid", "company_info");
    if (sellerElements != null && sellerElements.size() > 0) {
      sellerElement = sellerElements.first();
    }

    return sellerElement;
  }

  private Element getMainItemElement(Document document) {
    Element itemElement = null;
    Elements mainItemElements = document.getElementsByClass("x-product-page__main-content");
    if (mainItemElements != null && mainItemElements.size() > 0) {
      itemElement = mainItemElements.first();
    }

    return itemElement;
  }

  private boolean hasGifts(Element mainItemElement) {
    Elements giftsElement = mainItemElement.getElementsByAttributeValue("data-qaid", "gifts");
    return (giftsElement != null && giftsElement.size() > 0);
  }

  private ItemAviability getItemAvailability(Element mainItemElement) {
    ItemAviability itemAviability = ItemAviability.UNAVAILABLE;

    Elements itemAvailabilityType = mainItemElement.getElementsByAttributeValue("data-qaid", "product_presence");
    if (itemAvailabilityType != null && itemAvailabilityType.size() > 0) {
      if (itemAvailabilityType.first().hasClass("x-product-presence_type_ends")){
        itemAviability = ItemAviability.ENDS;
      } else if (itemAvailabilityType.first().hasClass("x-product-presence_type_pre-order")) {
        itemAviability = ItemAviability.PRE_ORDER;
      } else if (itemAvailabilityType.first().hasClass("x-product-presence_type_unavailable")) {
        itemAviability = ItemAviability.UNAVAILABLE;
      } else {
        itemAviability = ItemAviability.AVAILABLE;
      }
    }

    return itemAviability;
  }

  private Set<String> getImageUrls(Element mainItemElement) {
    Set<String> images = new HashSet<>();
    String jsonData = "";

    Elements imagesDataElement = mainItemElement.getElementsByAttributeValue("data-qaid", "image_block");
    if (imagesDataElement != null && imagesDataElement.size() == 1) {
      jsonData = imagesDataElement.first().attr("data-bazooka-props").trim();
    }

    if (!jsonData.isEmpty()) {
      images = getImageUrlsFromJsonString(jsonData);
    }

    return images;
  }

  private Map<Integer, Integer> getWholesalePrices(Element mainItemElement) {
    Map<Integer, Integer> wholesalePrices = new HashMap<>();

    Element pricesTable = mainItemElement.getElementById("wholesale_prices");
    if (pricesTable!= null) {
      Elements prices = pricesTable.getElementsByAttributeValue("data-qaid", "wholesale_prices");
      for (Element rowElement: prices) {
        Integer price = 0;
        Integer fromQuantity = 0;

        Elements rowElements = rowElement.getElementsByAttribute("data-qaid");
        for (Element rowDataElement: rowElements) {
          if (rowDataElement.attr("data-qaid").equalsIgnoreCase("wholesale-price")) {
            String elementData = rowDataElement.text().trim().replaceAll("[^0-9]", "");
            price = Integer.parseInt(elementData) * 100;
          } else if (rowDataElement.attr("data-qaid").equalsIgnoreCase("wholesale-quantity")) {
            String elementData = rowDataElement.text().trim().replaceAll("[^0-9]", "");
            fromQuantity = Integer.parseInt(elementData);
          }
        }

        wholesalePrices.put(fromQuantity, price);
      }
    }

    return wholesalePrices;
  }

  private Integer getItemPriceWithoutDiscount(Element mainItemElement) {
    Integer price = null;

    Elements skuPriceElements = mainItemElement.getElementsByAttributeValue("data-qaid", "price_without_discount");
    if (skuPriceElements != null && skuPriceElements.size() > 0) {
      price = (int) Double.parseDouble(skuPriceElements.first().attr("data-qaprice")) * 100;
    }

    return price;
  }

  private Integer getItemPrice(Element mainItemElement) {
    Integer price = null;

    Elements skuPriceElements = mainItemElement.getElementsByAttributeValue("data-qaid", "product_price");
    if (skuPriceElements != null && skuPriceElements.size() > 0) {
      price = (int) Double.parseDouble(skuPriceElements.first().attr("data-qaprice")) * 100;
    }

    return price;
  }

  private String getItemName(Element mainItemElement) {
    String name = "";

    Elements skuNameElements = mainItemElement.getElementsByAttributeValue("data-qaid", "product_name");
    if (skuNameElements != null && skuNameElements.size() > 0) {
     name = skuNameElements.first().text().trim();
    }

    return name;
  }

  private String getItemCode(Element mainItemElement) {
    String code = null;

    Elements skuIds = mainItemElement.getElementsByAttributeValue("data-qaid", "product-sku");
    if (skuIds != null && skuIds.size() > 0) {
      code = skuIds.first().text().trim();
    }

    return code;
  }

  private Set<String> getImageUrlsFromJsonString(String jsonString) {
    Set<String> images = new HashSet<>();

    ObjectMapper mapper = new ObjectMapper();
    try {
      JsonNode actualObj = mapper.readTree(jsonString);
      JsonNode imagesNode = actualObj.get("images");
      if (imagesNode.isArray() && imagesNode.size() > 0) {
        for (JsonNode imageData : imagesNode) {
          if (imageData.hasNonNull("image_url_640x640")) {
            images.add(imageData.get("image_url_640x640").asText(""));
          } else if (imageData.hasNonNull("image_url_200x200")) {
            images.add(imageData.get("image_url_200x200").asText(""));
          } else if (imageData.hasNonNull("image_url_640x640")) {
            images.add(imageData.get("image_url_100x100").asText(""));
          }
        }
      } else {
        JsonNode imageNode = actualObj.get("mainImage");
        if (imageNode.hasNonNull("url")) {
          images.add(imageNode.get("url").asText(""));
        }
      }

    } catch (IOException e) {
      e.printStackTrace();
    }

    return images;
  }

  @Override
  public void run() {
    logger.info("Thread " + treadId + " started.");
    itemsList.add(parse(url));
    logger.info("Thread " + treadId + " finished.");
  }
}
