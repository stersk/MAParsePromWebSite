package com.mainacad.webparser.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.*;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class Item {
  private String code;
  private String name;
  private Integer price;
  private Integer priceWithoutDiscount;
  private Map<Integer, Integer> wholesalePrices;
  private Set<String> imageUrls;
  private ItemAviability itemAvailability;
  private boolean hasGifts;
  private Seller seller;

  public Item() {
    wholesalePrices = new HashMap<>();
    imageUrls = new HashSet<>();
  }
}
