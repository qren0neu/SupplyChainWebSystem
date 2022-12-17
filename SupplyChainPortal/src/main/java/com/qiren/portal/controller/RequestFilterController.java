package com.qiren.portal.controller;

import java.io.IOException;

import org.springframework.stereotype.Component;

import com.qiren.common.tools.Logger;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class RequestFilterController implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        // TODO Auto-generated method stub
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        Logger.log("Received {" + httpRequest.getMethod() + "} request to {" + httpRequest.getRequestURI() + "}");

        chain.doFilter(request, response);

        // Logger.log("Sent {} response with status {}", httpRequest.getMethod(),
        // httpResponse.getStatus());
    }

}
