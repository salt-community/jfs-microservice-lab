package se.saltcode.inventory.model.cache;

import java.time.ZonedDateTime;
import java.util.UUID;

public record OrderCacheDto(UUID id, ZonedDateTime createdAt) {}
