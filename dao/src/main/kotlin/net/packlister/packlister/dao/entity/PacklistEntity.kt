package net.packlister.packlister.dao.entity

import net.packlister.packlister.model.Category
import net.packlister.packlister.model.Packlist
import org.hibernate.annotations.Type
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "packlists")
@EntityListeners(AuditingEntityListener::class)
class PacklistEntity(
    @Id
    val id: UUID,
    val name: String?,
    val description: String?,
    @Type(type = "com.vladmihalcea.hibernate.type.json.JsonType")
    @Column(name = "content", columnDefinition = "jsonb")
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
