package net.packlister.packlister.api

import io.swagger.v3.oas.annotations.tags.Tag
import net.packlister.packlister.api.model.APIDeleteItemsRequest
import net.packlister.packlister.api.model.APIUpsertItemsRequest
import net.packlister.packlister.api.model.APIUserItemsResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping

@RequestMapping("/api/items")
@Tag(
    name = "auth",
    description = "Operations for handling user items. Packlist items (user items part of a packlist) are handled " +
        "within packlist operations."
)
interface UserItemApi {
    @PostMapping("/batchDelete")
    fun deleteItems(@RequestBody deleteItemsRequest: APIDeleteItemsRequest): ResponseEntity<Void>

    @GetMapping
    fun getAllUserItems(): ResponseEntity<APIUserItemsResponse>

    @PutMapping("/batch")
    fun upsertItems(@RequestBody upsertItemsRequest: APIUpsertItemsRequest): ResponseEntity<APIUserItemsResponse>
}
