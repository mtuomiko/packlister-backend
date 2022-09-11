package net.packlister.packlister.model

import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.UUID

data class User(
    val id: UUID,
    private val username: String, // prevent clash with UserDetails
    val email: String,
    val passwordHash: String
) : UserDetails {
    override fun getPassword(): String = passwordHash
    override fun getUsername(): String = username

    // improve: possibly use these
    override fun isAccountNonExpired() = true
    override fun isAccountNonLocked() = true
    override fun isCredentialsNonExpired() = true
    override fun isEnabled() = true

    // no distinct authorities handled
    override fun getAuthorities() = listOf(SimpleGrantedAuthority("USER"))
}
