package net.packlister.packlister.dao.entity

import net.packlister.packlister.model.Category
import net.packlister.packlister.model.Packlist
import org.hibernate.annotations.Type
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "packlists")
class PacklistEntity(
    @Id
    val id: UUID,
    val name: String?,
    val description: String?,
    @Type(type = "com.vladmihalcea.hibernate.type.json.JsonType")
    @Column(name = "content", columnDefinition = "jsonb")
    val categories: List<Category>,
    @Column(name = "account")
    val userId: UUID
) {
    fun toModel() = with(this) { Packlist(id, name, description, categories) }
}
