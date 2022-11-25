package net.packlister.packlister.api

import net.packlister.packlister.api.model.APICategory
import net.packlister.packlister.api.model.APICategoryItem
import net.packlister.packlister.api.model.APIPacklist
import net.packlister.packlister.api.model.APIPacklistLimitedView
import net.packlister.packlister.api.model.APIPacklistsLimitedResponse
import net.packlister.packlister.model.Category
import net.packlister.packlister.model.CategoryItem
import net.packlister.packlister.model.Packlist
import net.packlister.packlister.model.ValidationError
import net.packlister.packlister.svc.PacklistService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
class PacklistController(
    @Autowired
    private val packlistService: PacklistService
) : PacklistApi {
    override fun getAllUserPacklists(): ResponseEntity<APIPacklistsLimitedResponse> {
        val packlists = packlistService.getPacklists()
        val apiPacklists = packlists.map {
            APIPacklistLimitedView(
                id = it.id,
                name = it.name
            )
        }
        return ResponseEntity.ok(APIPacklistsLimitedResponse(packlists = apiPacklists))
    }

    override fun getPacklist(id: UUID): ResponseEntity<APIPacklist> {
        val packlist = packlistService.getOnePacklist(id)
        val apiPacklist = packlist.toApiModel()
        return ResponseEntity.ok(apiPacklist)
    }

    override fun createPacklist(id: UUID, packlist: APIPacklist): ResponseEntity<APIPacklist> {
        if (id != packlist.id) {
            throw ValidationError("inconsistent packlist ID in request")
        }
        val createdPacklist = packlistService.createPacklist(packlist.toModel())
        return ResponseEntity.ok(createdPacklist.toApiModel())
    }

    override fun updatePacklist(id: UUID, packlist: APIPacklist): ResponseEntity<APIPacklist> {
        if (id != packlist.id) {
            throw ValidationError("inconsistent packlist ID in request")
        }
        val updatedPacklist = packlistService.updatePacklist(packlist.toModel())
        return ResponseEntity.ok(updatedPacklist.toApiModel())
    }

    private fun Packlist.toApiModel() = APIPacklist(
        id, name, description, categories = categories.map { category -> category.toApiModel() }
    )

    private fun Category.toApiModel() = APICategory(
        id, name, items = categoryItems.map { item -> item.toApiModel() }
    )

    private fun CategoryItem.toApiModel() = APICategoryItem(userItemId, quantity)

    private fun APIPacklist.toModel() = Packlist(
        this.id,
        this.name,
        this.description,
        categories = this.categories.map { it.toModel() }
    )

    private fun APICategory.toModel() = Category(
        this.id,
        this.name,
        categoryItems = this.items.map { it.toModel() }
    )

    private fun APICategoryItem.toModel() = CategoryItem(
        this.userItemId,
        this.quantity
    )
}
