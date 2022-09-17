package net.packlister.packlister.api

import net.packlister.packlister.generated.api.PacklistsApiDelegate
import net.packlister.packlister.generated.model.GetAllPacklists200Response
import net.packlister.packlister.generated.model.PacklistLimitedView
import net.packlister.packlister.model.Category
import net.packlister.packlister.model.CategoryItem
import net.packlister.packlister.model.Packlist
import net.packlister.packlister.model.ValidationError
import net.packlister.packlister.svc.PacklistService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.util.UUID
import net.packlister.packlister.generated.model.Category as APICategory
import net.packlister.packlister.generated.model.CategoryItem as APICategoryItem
import net.packlister.packlister.generated.model.Packlist as APIPacklist

@RestController
class PacklistController(
    @Autowired
    private val packlistService: PacklistService
) : PacklistsApiDelegate {
    override fun getAllPacklists(): ResponseEntity<GetAllPacklists200Response> {
        val packlists = packlistService.getPacklists()
        val apiPacklists = packlists.map {
            PacklistLimitedView().apply {
                id = it.id
                name = it.name
            }
        }
        return ResponseEntity.ok(GetAllPacklists200Response().packlists(apiPacklists))
    }

    override fun getPacklist(id: UUID): ResponseEntity<APIPacklist> {
        val packlist = packlistService.getOnePacklist(id)
        val apiPacklist = packlist.toApiModel()
        return ResponseEntity.ok(apiPacklist)
    }

    override fun updatePacklist(id: UUID, packlist: APIPacklist): ResponseEntity<APIPacklist> {
        if (id != packlist.id) {
            throw ValidationError("inconsistent id in request")
        }
        val packlist = packlistService.upsertPacklist(packlist.toModel())
        return ResponseEntity.ok(packlist.toApiModel())
    }

    private fun Packlist.toApiModel() = APIPacklist().also {
        it.id = id
        it.name = name
        it.description = description
        it.categories = categories.map { category -> category.toApiModel() }
    }

    private fun Category.toApiModel() = APICategory().also {
        it.id = id
        it.name = name
        it.items = categoryItems.map { item -> item.toApiModel() }
    }

    private fun CategoryItem.toApiModel() = APICategoryItem().also {
        it.userItemId = userItemId
        it.quantity = quantity
    }

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
