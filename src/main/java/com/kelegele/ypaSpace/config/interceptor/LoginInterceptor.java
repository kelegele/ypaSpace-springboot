package com.kelegele.ypaSpace.config.interceptor;

import com.kelegele.ypaSpace.config.Constant;
import com.kelegele.ypaSpace.entity.Token;
import com.kelegele.ypaSpace.mapper.TokenMapper;
import com.kelegele.ypaSpace.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

public class LoginInterceptor implements HandlerInterceptor {
    @Autowired(required = false)
    protected TokenMapper tokenMapper;

    //提供查询
    @Override
    public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
            throws Exception {
    }

    @Override
    public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
            throws Exception {
    }

    @Override
    public boolean preHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2) throws Exception {
        //此处为不需要登录的接口放行
        if (arg0.getRequestURI().contains("/user/login")
                || arg0.getRequestURI().contains("/user/register")
                || arg0.getRequestURI().contains("/error")
                || arg0.getRequestURI().contains("/static")
                || arg0.getRequestURI().contains("/hadoop")) {
            return true;
        }

        //权限路径拦截
        //PrintWriter resultWriter = arg1.getOutputStream();
        // TODO: 有时候用PrintWriter 会报 getWriter() has already been called for this response
        //换成ServletOutputStream就OK了
        arg1.setContentType("text/html;charset=utf-8");
        ServletOutputStream resultWriter = arg1.getOutputStream();
        final String headerToken = arg0.getHeader("X-Token");

        //判断请求信息
        if (null == headerToken || headerToken.trim().equals("")) {
            resultWriter.write("你没有token,需要登录".getBytes());
            resultWriter.flush();
            resultWriter.close();
            return false;
        }

        //解析Token信息
        try {
            //Claims claims = Jwts.parser().setSigningKey("preRead").parseClaimsJws(headerToken).getBody();
            Claims claims = JwtUtil.parseJWT(headerToken);
            String tokenUserId = (String) claims.get("userId");
            //根据客户Token查找数据库Token
            Token myToken = tokenMapper.findByUserId(tokenUserId);

            //数据库没有Token记录
            if (null == myToken) {
                resultWriter.write("没有token,需要登录".getBytes());
                resultWriter.flush();
                resultWriter.close();
                return false;
            }
            //数据库Token与客户Token比较
            if (!headerToken.equals(myToken.getToken())) {
                resultWriter.write("token修改过,需要登录".getBytes());
                resultWriter.flush();
                resultWriter.close();
                return false;
            }
            //判断Token过期
            Date tokenDate = claims.getExpiration();
            int overTime = (int) (new Date().getTime() - tokenDate.getTime()) / 1000;
            if (overTime > Constant.JWT_TTL) {
                resultWriter.write("token过期,需要登录".getBytes());
                resultWriter.flush();
                resultWriter.close();
                return false;
            }

        } catch (Exception e) {
            resultWriter.write(e.getMessage().getBytes());
            resultWriter.flush();
            resultWriter.close();
            return false;
        }
        //最后才放行
        return true;
    }
}
