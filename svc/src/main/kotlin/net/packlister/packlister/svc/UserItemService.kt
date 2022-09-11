package net.packlister.packlister.svc

import net.packlister.packlister.TokenReader
import net.packlister.packlister.dao.UserItemDao
import net.packlister.packlister.model.UserItem
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class UserItemService(
    @Autowired
    private val userItemDao: UserItemDao,
    @Autowired
    private val tokenReader: TokenReader
) {
    fun upsertUserItems(userItems: List<UserItem>): List<UserItem> {
        return userItemDao.upsertUserItems(userItems, tokenReader.getUserId())
    }

    fun deleteUserItems(userItemIds: List<UUID>) {
        userItemDao.deleteUserItems(userItemIds, tokenReader.getUserId())
    }

    fun getUserItems(): List<UserItem> {
        return userItemDao.getUserItems(tokenReader.getUserId())
    }
}
