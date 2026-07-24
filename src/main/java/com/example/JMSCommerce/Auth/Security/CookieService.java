package com.example.JMSCommerce.Auth.Security;


import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

// I am making it service so that if i need it somewhere we can autowire it
@Service
@Getter
public class CookieService {


    private final String refreshTokenCookieName;
    private final boolean cookieHttpOnly;
    private final boolean cookieSecure;
    private final String cookieDomain;
    private final String cookieSameSite;
    private final Logger logger= LoggerFactory.getLogger(CookieService.class);


    public CookieService(@Value("${security.jwt.refresh-token-cookie-name}") String refreshTokenCookieName,
                         @Value("${security.jwt.cookie-http-only}")boolean cookieHttpOnly,
                         @Value("${security.jwt.cookie-secure}")boolean cookieSecure,
                         @Value("${security.jwt.refresh-token-cookie-domain}")String cookieDomain,
                         @Value("${security.jwt.cookie-same-site}")String cookieSameSite) {
        this.refreshTokenCookieName = refreshTokenCookieName;
        this.cookieHttpOnly = cookieHttpOnly;
        this.cookieSecure = cookieSecure;
        this.cookieDomain = cookieDomain;
        this.cookieSameSite = cookieSameSite;
    }

    // method to attach cookie to response
    public void attachRefreshTokenCookie(HttpServletResponse res, String value, int maxAge){

       ResponseCookie.ResponseCookieBuilder responseCookieBuilder
               =ResponseCookie.from(refreshTokenCookieName,value)
//               .domain(cookieDomain)
               .maxAge(maxAge)
               .httpOnly(cookieHttpOnly)
               .sameSite(cookieSameSite)
               .path("/")
               .secure(cookieSecure);
       if(cookieDomain!=null&&!cookieDomain.isBlank()){
           responseCookieBuilder.domain(cookieDomain);
       }

       logger.info("attaching token to cookie in response "+responseCookieBuilder+" "+value);
       ResponseCookie responseCookie=responseCookieBuilder.build();
       //now attach this to http header
        res.setHeader(HttpHeaders.SET_COOKIE,responseCookie.toString());

    }

    // method to remove cookie to response
    public void clearRefreshTokenCookie(HttpServletResponse res){

        ResponseCookie.ResponseCookieBuilder responseCookieBuilder
                =ResponseCookie.from(refreshTokenCookieName,"")
//                .domain(cookieDomain)
                .maxAge(0)
                .httpOnly(cookieHttpOnly)
                .sameSite(cookieSameSite)
                .path("/")
                .secure(cookieSecure);
        if(cookieDomain!=null&&!cookieDomain.isBlank()){
            responseCookieBuilder.domain(cookieDomain);
        }
        ResponseCookie responseCookie=responseCookieBuilder.build();
        //now attach this to http header
        res.setHeader(HttpHeaders.SET_COOKIE,responseCookie.toString());

    }

//it helps to stop caching
    public void NoStoreHeader(HttpServletResponse response){
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
        response.setHeader("Pragma", "no-cache");
    }


}
