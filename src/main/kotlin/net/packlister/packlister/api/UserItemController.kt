package net.packlister.packlister.api

import net.packlister.packlister.generated.api.ItemsApiDelegate
import net.packlister.packlister.generated.model.UserItem
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
class UserItemController : ItemsApiDelegate {
    override fun deleteItems(UUID: List<UUID>): ResponseEntity<Void> {
        println("my delete")
        return ResponseEntity(HttpStatus.NOT_IMPLEMENTED)
    }

    override fun upsertItems(userItem: List<UserItem>): ResponseEntity<List<UserItem>> {
        println("my upsert")
        return ResponseEntity(HttpStatus.NOT_IMPLEMENTED)
    }
}
