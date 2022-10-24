package net.packlister.packlister.svc.validation

object Validators {
    const val alphaNumericRegexString = """^[a-zA-Z0-9._-]*$"""

    /**
     * Not attempting any complex validation, simply disallows whitespace chars and expects @, and . in domain.
     *
     * Examples:
     * foo@bar.baz        -> okay
     * fooest bar@baz.com -> fail
     * baz@bar            -> fail
     */
    const val simpleEmailRegexString = """^\S+@\S+\.\S+$"""
}
