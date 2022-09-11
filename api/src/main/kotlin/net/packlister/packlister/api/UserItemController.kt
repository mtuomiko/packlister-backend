package net.packlister.packlister.api

import net.packlister.packlister.generated.api.ItemsApiDelegate
import net.packlister.packlister.generated.model.DeleteItemsRequest
import net.packlister.packlister.generated.model.UpsertItemsRequest
import net.packlister.packlister.generated.model.UserItemsResponse
import net.packlister.packlister.model.UserItem
import net.packlister.packlister.svc.UserItemService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import net.packlister.packlister.generated.model.UserItem as APIUserItem

@RestController
class UserItemController(
    @Autowired
    private val userItemService: UserItemService
) : ItemsApiDelegate {
    override fun deleteItems(deleteItemsRequest: DeleteItemsRequest): ResponseEntity<Void> {
        userItemService.deleteUserItems(deleteItemsRequest.userItemIds)
        return ResponseEntity.ok().build()
    }

    override fun getItems(): ResponseEntity<UserItemsResponse> {
        val apiUserItems = userItemService.getUserItems().map { it.toApiModel() }
        return ResponseEntity.ok(UserItemsResponse().userItems(apiUserItems))
    }

    override fun upsertItems(upsertItemsRequest: UpsertItemsRequest): ResponseEntity<UserItemsResponse> {
        val userItems = upsertItemsRequest.userItems.map { it.toModel() }
        val upsertedItems = userItemService.upsertUserItems(userItems)
        val apiItems = upsertedItems.map { it.toApiModel() }
        val response = UserItemsResponse().userItems(apiItems)
        return ResponseEntity.ok(response)
    }

    fun APIUserItem.toModel() = with(this) {
        UserItem(
            id,
            name,
            description,
            weight,
            publicVisibility
        )
    }

    fun UserItem.toApiModel() = APIUserItem().also {
        it.id = id
        it.name = name
        it.description = description
        it.weight = weight
        it.publicVisibility = publicVisibility
    }
}
