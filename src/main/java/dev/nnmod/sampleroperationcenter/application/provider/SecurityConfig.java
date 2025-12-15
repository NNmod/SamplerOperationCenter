package dev.nnmod.sampleroperationcenter.application.provider;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.security.enterprise.authentication.mechanism.http.FormAuthenticationMechanismDefinition;
import jakarta.security.enterprise.authentication.mechanism.http.LoginToContinue;
import jakarta.security.enterprise.identitystore.DatabaseIdentityStoreDefinition;
import jakarta.security.enterprise.identitystore.Pbkdf2PasswordHash;

@ApplicationScoped
@FormAuthenticationMechanismDefinition(
        loginToContinue = @LoginToContinue(
                loginPage = "/login.jsp",       // Changed to .jsp
                errorPage = "/login_error.jsp"  // Changed to .jsp
        )
)
@DatabaseIdentityStoreDefinition(
        dataSourceLookup = "jdbc/PostgresPool",
        callerQuery = "SELECT password FROM operators WHERE login = ?",
        groupsQuery = "SELECT 'USER' FROM operators WHERE login = ?",
        hashAlgorithm = Pbkdf2PasswordHash.class,
        priority = 10
)
public class SecurityConfig {
}
