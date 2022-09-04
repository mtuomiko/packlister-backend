package net.packlister.packlister.svc

import net.packlister.packlister.dao.UserItemDao
import net.packlister.packlister.model.UserItem
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class UserItemService(
    @Autowired
    private val userItemDao: UserItemDao
) {
    fun upsertUserItems(userItems: List<UserItem>): List<UserItem> {
        return userItemDao.upsertUserItems(userItems)
    }
}
