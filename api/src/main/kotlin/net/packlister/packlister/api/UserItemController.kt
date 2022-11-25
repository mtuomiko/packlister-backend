package net.packlister.packlister.api

import net.packlister.packlister.api.model.APIDeleteItemsRequest
import net.packlister.packlister.api.model.APIUpsertItemsRequest
import net.packlister.packlister.api.model.APIUserItem
import net.packlister.packlister.api.model.APIUserItemsResponse
import net.packlister.packlister.model.UserItem
import net.packlister.packlister.svc.UserItemService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class UserItemController(
    @Autowired
    private val userItemService: UserItemService
) : UserItemApi {
    override fun deleteItems(deleteItemsRequest: APIDeleteItemsRequest): ResponseEntity<Void> {
        userItemService.deleteUserItems(deleteItemsRequest.userItemIds)
        return ResponseEntity.ok().build()
    }

    override fun getAllUserItems(): ResponseEntity<APIUserItemsResponse> {
        val apiUserItems = userItemService.getUserItems().map { it.toApiModel() }
        return ResponseEntity.ok(APIUserItemsResponse(userItems = apiUserItems))
    }

    override fun upsertItems(upsertItemsRequest: APIUpsertItemsRequest): ResponseEntity<APIUserItemsResponse> {
        val userItems = upsertItemsRequest.userItems.map { it.toModel() }
        val upsertedItems = userItemService.upsertUserItems(userItems)
        val apiItems = upsertedItems.map { it.toApiModel() }
        val response = APIUserItemsResponse(userItems = apiItems)
        return ResponseEntity.ok(response)
    }

    fun APIUserItem.toModel() = UserItem(id, name, description, weight, publicVisibility)

    fun UserItem.toApiModel() = APIUserItem(id, name, description, weight, publicVisibility)
}
