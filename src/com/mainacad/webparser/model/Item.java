package com.mainacad.webparser.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class Item {
  private Integer code;
  private String name;
  private Integer price;
  private Integer priceWithoutDiscount;
  private Map<Integer, Integer> wholesalePrices;
  private List<String> imageUrls;
  private boolean itemAvailable;
  private String seller;
  private String sellerSiteUrl;
  private boolean hasGifts;

  public Item() {
    wholesalePrices = new HashMap<>();
    imageUrls = new ArrayList<>();
  }
}
