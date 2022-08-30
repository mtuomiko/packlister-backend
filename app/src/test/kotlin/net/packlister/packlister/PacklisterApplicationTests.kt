package net.packlister.packlister

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles(profiles = ["local"])
class PacklisterApplicationTests {

    @Test
    fun contextLoads() {
        assertThat(true).isTrue
    }
}
