import net.packlister.packlister.api.UserItemController
import net.packlister.packlister.api.model.APIUpsertItemsRequest
import net.packlister.packlister.api.model.APIUserItem
import net.packlister.packlister.svc.UserItemService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.util.UUID

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserItemControllerTest {
    private val userItemService = mock<UserItemService>()
    private val controller = UserItemController(userItemService)

    @BeforeAll
    fun setup() {
        whenever(userItemService.upsertUserItems(any())).thenAnswer { it.arguments[0] }
    }

    @Test
    fun upsertItemsSuccessReturnsSameList() {
        val initial = listOf(
            APIUserItem(
                id = UUID.randomUUID(),
                name = "foo",
                description = "bar",
                weight = 300,
                publicVisibility = true
            )
        )
        val request = APIUpsertItemsRequest(userItems = initial)
        val result = controller.upsertItems(request)

        assertThat(result.body!!.userItems).containsExactlyInAnyOrderElementsOf(initial)
    }
}
