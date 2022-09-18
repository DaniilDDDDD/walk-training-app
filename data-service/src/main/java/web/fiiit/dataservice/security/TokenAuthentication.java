package web.fiiit.dataservice.security;


import lombok.Setter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import web.fiiit.dataservice.document.Token;



@Setter
public class TokenAuthentication extends AbstractAuthenticationToken {

    private final String credentials;

    private final Object principal;

    public TokenAuthentication(String token) {
        super(AuthorityUtils.NO_AUTHORITIES);
        credentials = token;
        principal = token;
    }

    public TokenAuthentication(String credentials, Token token) {
        super(AuthorityUtils.NO_AUTHORITIES);
        this.credentials = credentials;
        this.principal = token;
    }

    @Override
    public Object getCredentials() {
        return credentials;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }
}
