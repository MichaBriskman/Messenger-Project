package hac;

import hac.repo.User;
import org.springframework.web.servlet.HandlerInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * This class intercepts all requests and displays statistics: request processing duration
 */
public class LoggingInterceptor implements HandlerInterceptor {
    public LoggingInterceptor() {}

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
       User user = (User)request.getSession().getAttribute("scopedTarget.loggedUser");
        if(user.getUserName() == null) {
            response.sendRedirect("/");
            return false;
        }
        return true;
    }
}