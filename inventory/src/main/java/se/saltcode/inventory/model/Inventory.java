package se.saltcode.inventory.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name = "inventory")
public class Inventory {

  @UuidGenerator @Id private UUID id;

  @Column(name = "product")
  private String product;

  @Column(name = "quantity")
  private int quantity;

  @Column(name = "reserved_quantity")
  private int reservedQuantity;

  public Inventory(InventoryDto inventoryDTO) {}

  public Inventory(AddInventoryDto addInventoryDTO) {
    this.product = addInventoryDTO.product();
    this.quantity = addInventoryDTO.quantity();
    this.reservedQuantity = 0;
  }

  public InventoryDto toResponseObject() {
    return new InventoryDto(id, product, quantity, reservedQuantity);
  }

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
