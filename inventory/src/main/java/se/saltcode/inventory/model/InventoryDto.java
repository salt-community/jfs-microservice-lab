package se.saltcode.inventory.model;

import java.util.UUID;

public record InventoryDto (UUID id, String product, int quantity, int reservedQuantity){}
