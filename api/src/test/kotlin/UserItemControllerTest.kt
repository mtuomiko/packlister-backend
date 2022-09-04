import net.packlister.packlister.api.CustomError
import net.packlister.packlister.api.UserItemController
import net.packlister.packlister.generated.model.UpsertItemsRequest
import net.packlister.packlister.generated.model.UserItem
import net.packlister.packlister.svc.UserItemService
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
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
    fun deleteItemsThrows() {
        val thrown = catchThrowable {
            controller.deleteItems(listOf())
        }
        assertThat(thrown)
            .isInstanceOf(CustomError::class.java)
            .hasMessageContaining("nope")
    }

    @Test
    fun upsertItemsSuccessReturnsSameList() {
        val initial = listOf(
            UserItem().apply {
                id = UUID.randomUUID()
                name = "foo"
                description = "bar"
                weight = 300
                publicVisibility = true
            }
        )
        val request = UpsertItemsRequest().userItems(initial)
        val result = controller.upsertItems(request)

        assertThat(result.body!!.userItems).containsExactlyInAnyOrderElementsOf(initial)
    }
}
