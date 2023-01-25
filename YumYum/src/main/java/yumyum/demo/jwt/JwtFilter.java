package yumyum.demo.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * JWT를 위한 Custom Filter를 만들기 위해 GenericFilterBean을 Extends한다.
 */
public class JwtFilter extends GenericFilterBean {
    //do filter 내부에서 실제 필터링됨 : jWT 토큰의 인증 여부를
    // security context에 저장하는 역할 수행
    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);
    public static final String ACCESS_TOKEN = "Authorization";
    public static final String REFRESH_TOKEN = "REFRESH-TOKEN";
    private TokenProvider tokenProvider;

    /**
     * tokenProvider를 이용하여 빈을 주입받는다.
     * @param tokenProvider
     */
    public JwtFilter(TokenProvider tokenProvider){
        this.tokenProvider = tokenProvider;
    }

    /**
     * 실제 필터링 로직
     * @param request
     * @param response
     * @param chain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String accessToken = resolveToken(httpServletRequest); // request에서 토큰 받음
        String requestURI = httpServletRequest.getRequestURI();

        if(StringUtils.hasText(accessToken) && tokenProvider.isValidAccessToken(accessToken)){
            Authentication authentication = tokenProvider.getAuthenticationByAccessToken(accessToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            logger.debug("Security Context에 '{}' 인증 정보를 저장했습니다. URL : {}", authentication, requestURI);
        }
        else{
            logger.debug("유효한 JWT 토큰이 없습니다. URL : {}", requestURI);
        }
        chain.doFilter(request, response);
    }

    /**
     * HttpServletRequest 객체의 Header에서 token을 꺼내는 역할을 수행
     * @param request
     * @return
     */
    private String resolveToken(HttpServletRequest request){
        String accessToken = request.getHeader(ACCESS_TOKEN);
        if(StringUtils.hasText(accessToken) && accessToken.startsWith("Bearer "))
            return accessToken.substring(7);
        return null;
    }
}
