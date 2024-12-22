package com.macro.mall.tiny.security.component;

import com.macro.mall.tiny.security.util.JwtTokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * JWT登录授权过滤器
 * Created by macro on 2018/4/26.
 */
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationTokenFilter.class);
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Value("${jwt.tokenHeader}")
    private String tokenHeader;
    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        // 从请求头中获取 Authorization 字段的值
        String authHeader = request.getHeader(this.tokenHeader);
        // 检查请求头是否存在且以 "Bearer " 开头
        if (authHeader != null && authHeader.startsWith(this.tokenHead)) {
            // 提取出 JWT 令牌部分
            String authToken = authHeader.substring(this.tokenHead.length());// The part after "Bearer "
            // 从 JWT 令牌中提取用户名
            String username = jwtTokenUtil.getUserNameFromToken(authToken);
            // 记录日志，显示正在检查的用户名
            LOGGER.info("checking username:{}", username);
            // 检查用户名不为空且当前安全上下文中没有已认证的用户信息
            /*
            为什么会有上下文中没有已认证的用户信息的情况？
            首次访问：用户首次访问系统时，安全上下文中自然是空的，因为还没有进行任何认证操作。
            会话过期：如果用户的会话已经过期或被手动注销，安全上下文中的认证信息会被清除。
            跨域请求：在某些情况下，跨域请求可能会导致安全上下文中的认证信息丢失。
            多线程环境：在多线程环境中，不同的线程可能会有不同的安全上下文，因此某个线程的安全上下文中可能没有认证信息。
            手动清除：系统中可能存在某些逻辑或操作，会手动清除安全上下文中的认证信息，例如在用户注销时。
            总结
                这段代码的作用是确保只有在用户名有效且当前安全上下文中没有已认证的用户信息时，才会进行用户认证。这样可以避免不必要的重复认证，提高系统的性能和安全性。
             **/
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // 根据用户名加载用户详细信息
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                // 验证 JWT 令牌的有效性
                if (jwtTokenUtil.validateToken(authToken, userDetails)) {
                    // 创建认证对象
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    // 设置认证详情
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    // 记录日志，显示已认证的用户名
                    LOGGER.info("authenticated user:{}", username);
                    // 将认证对象设置到当前的安全上下文中
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }
        chain.doFilter(request, response);
    }
}
