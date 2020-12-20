package com.blog.template.interceptor;

import com.blog.template.common.annotation.PassToken;
import com.blog.template.common.annotation.UserLoginToken;
import com.blog.template.common.constants.Constant;
import com.blog.template.common.utils.UserUtil;
import com.blog.template.dao.UserDao;
import com.blog.template.exceptions.CustomerException;
import com.blog.template.models.userinfo.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.util.Optional;

@Component
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private UserDao userDao;

    private Long getUserId(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Object userIdObj = session.getAttribute(Constant.SessionKey.SESSION_USERID);
        if (userIdObj != null) {
            return (Long) userIdObj;
        }
        return null;
    }

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object
            object) throws Exception {
        Long userId = getUserId(httpServletRequest);
        Optional<UserInfo> user = Optional.empty();
        if (!(object instanceof HandlerMethod)) {
            return true;
        }
        if (userId != null) {
            user = userDao.findById(userId);
            user.ifPresent(UserUtil::setUser);
        }
        HandlerMethod handlerMethod = (HandlerMethod) object;
        Method method = handlerMethod.getMethod();
        if (method.isAnnotationPresent(PassToken.class)) {
            PassToken passToken = method.getAnnotation(PassToken.class);
            if (passToken.required()) {
                return true;
            }
        }
        if (method.isAnnotationPresent(UserLoginToken.class)) {
            UserLoginToken userLoginToken = method.getAnnotation(UserLoginToken.class);
            if (userLoginToken.required()) {
                if (userId == null) {
                    throw new CustomerException("please login first");
                }

                if (!user.isPresent()) {
                    throw new CustomerException("user not find ,please register");
                }
                return true;
            }
        }
        return true;
    }


    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest,
                                HttpServletResponse httpServletResponse,
                                Object o, Exception e) throws Exception {
        UserUtil.removeUser();
    }
}


