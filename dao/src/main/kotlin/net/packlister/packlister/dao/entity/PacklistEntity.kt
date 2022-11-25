package net.packlister.packlister.dao.entity

import com.vladmihalcea.hibernate.type.json.JsonType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.Id
import jakarta.persistence.Table
import net.packlister.packlister.model.Category
import net.packlister.packlister.model.Packlist
import org.hibernate.annotations.Type
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "packlists")
@EntityListeners(AuditingEntityListener::class)
class PacklistEntity(
    @Id
    val id: UUID,
    val name: String?,
    val description: String?,
    @Type(JsonType::class)
    @Column(name = "content")
    val categories: List<Category>,
    @Column(name = "account_id")
    val userId: UUID
) {
    @field:LastModifiedDate
    lateinit var modifiedAt: Instant

    @field:CreatedDate
    @Column(updatable = false)
    lateinit var createdAt: Instant

    fun toModel() = with(this) { Packlist(id, name, description, categories) }
}
