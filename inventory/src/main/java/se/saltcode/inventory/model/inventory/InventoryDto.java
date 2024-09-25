package se.saltcode.inventory.model.inventory;

import java.util.UUID;

public record InventoryDto(UUID id, String product, int quantity, int reservedQuantity) {}
