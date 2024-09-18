package se.saltcode.inventory.model;

import java.util.UUID;

public class InventoryDTO {

  private UUID id;

  private String product;

  private int quantity;

  private int reservedQuantity;

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getProduct() {
    return product;
  }

  public void setProduct(String product) {
    this.product = product;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  public int getReservedQuantity() {
    return reservedQuantity;
  }

  public void setReservedQuantity(int reservedQuantity) {
    this.reservedQuantity = reservedQuantity;
  }
}
