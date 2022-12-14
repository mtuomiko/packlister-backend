package net.packlister.packlister.dao

import org.springframework.boot.SpringBootConfiguration
import org.springframework.boot.autoconfigure.EnableAutoConfiguration

/**
 * This configuration is used to work around the fact that the main application (@SpringBootApplication and the
 * autoconfiguration it makes) is not visible to this subproject. Autoconfiguration here allows us to easily pickup
 * entities, repositories and so on, and use @DataJpaTest in test classes.
 *
 * Improve: Declare properties only in single place (see resources/application.yml for this module)
 */
@SpringBootConfiguration
@EnableAutoConfiguration
class DaoTestConfiguration
