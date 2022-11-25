package net.packlister.packlister.api

import jakarta.validation.Valid
import net.packlister.packlister.api.model.APIPacklist
import net.packlister.packlister.api.model.APIPacklistsLimitedResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import java.util.UUID

@RequestMapping("/api/packlists")
interface PacklistApi {
    @PostMapping("/{id}")
    fun createPacklist(
        @PathVariable("id") id: UUID,
        @Valid @RequestBody
        packlist: APIPacklist
    ): ResponseEntity<APIPacklist>

    @GetMapping
    fun getAllUserPacklists(): ResponseEntity<APIPacklistsLimitedResponse>

    @GetMapping("/{id}")
    fun getPacklist(@PathVariable("id") id: UUID): ResponseEntity<APIPacklist>

    @PutMapping("/{id}")
    fun updatePacklist(
        @PathVariable("id") id: UUID,
        @Valid @RequestBody
        packlist: APIPacklist
    ): ResponseEntity<APIPacklist>
}
