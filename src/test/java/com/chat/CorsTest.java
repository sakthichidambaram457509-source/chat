package com.chat;

import com.chat.security.JwtFilter;
import com.chat.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CorsTest {

    private JwtFilter jwtFilter;

    @BeforeEach
    public void setUp() {
        JwtUtil jwtUtil = new JwtUtil();
        jwtFilter = new JwtFilter(jwtUtil);
    }

    @Test
    public void testPreflightCorsFromVercel() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("OPTIONS", "/auth/login");
        request.addHeader("Origin", "https://pesuda.vercel.app");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();

        jwtFilter.doFilter(request, response, filterChain);

        assertEquals("https://pesuda.vercel.app", response.getHeader("Access-Control-Allow-Origin"));
        assertEquals("true", response.getHeader("Access-Control-Allow-Credentials"));
        assertEquals(200, response.getStatus());
    }

    @Test
    public void testPreflightCorsFromLocalhost() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("OPTIONS", "/auth/login");
        request.addHeader("Origin", "http://localhost:5173");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();

        jwtFilter.doFilter(request, response, filterChain);

        assertEquals("http://localhost:5173", response.getHeader("Access-Control-Allow-Origin"));
        assertEquals("true", response.getHeader("Access-Control-Allow-Credentials"));
        assertEquals(200, response.getStatus());
    }
}
