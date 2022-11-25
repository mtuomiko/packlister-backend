package net.packlister.packlister.api.model

import jakarta.validation.constraints.Size
import java.util.UUID

class APIDeleteItemsRequest(
    val userItemIds: List<UUID>
)
