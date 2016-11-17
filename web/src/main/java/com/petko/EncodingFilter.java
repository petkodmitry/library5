package com.petko;

import javax.servlet.*;
import java.io.IOException;

public class EncodingFilter implements Filter{
//    private String encoding;
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        /*encoding = filterConfig.getInitParameter("requestEncoding");
        if (encoding == null) {
            encoding = "UTF-8";
        }*/
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        servletRequest.setCharacterEncoding("UTF-8");
        servletResponse.setCharacterEncoding("UTF-8");
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {}
}
