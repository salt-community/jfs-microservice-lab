package se.saltcode.inventory.model.inventory;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name = "inventory")
public class Inventory {

  @UuidGenerator @Id private UUID id;

  @NotNull(message = "product cant be null")
  private String product;

  private int quantity;

  private int reservedQuantity;

  public Inventory(InventoryDto inventoryDTO) {
    this.id = inventoryDTO.id();
    this.product = inventoryDTO.product();
    this.quantity = inventoryDTO.quantity();
    this.reservedQuantity = inventoryDTO.reservedQuantity();
  }

  public Inventory(AddInventoryDto addInventoryDTO) {
    this.product = addInventoryDTO.product();
    this.quantity = addInventoryDTO.quantity();
    this.reservedQuantity = 0;
  }

  public Inventory() {}

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
