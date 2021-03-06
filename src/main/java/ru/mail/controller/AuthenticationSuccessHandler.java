package ru.mail.controller;

import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Properties;

/**
 * @author Denis Monich
 * this class send json object to UI if was happened success user authentication
 */
@Component("authenticationSuccessHandler")
public class AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private static final Logger logger = Logger.getLogger(AuthenticationSuccessHandler.class);

    /**
     * @param request        default parameter for override method "onAuthenticationFailure"
     * @param response       for creating and sending json object
     * @param authentication default parameter for override method "onAuthenticationFailure"
     * @throws IOException      can be throw during reading the {@link Properties} file
     * @throws ServletException to indicate that servlet is permanently or temporarily unavailable.
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        /*
          public static method for creating and sending json object
         */
        response.sendRedirect(request.getContextPath() + "/admin");
    }
}
