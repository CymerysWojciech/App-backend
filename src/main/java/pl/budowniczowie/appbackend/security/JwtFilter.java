package pl.budowniczowie.appbackend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    @Override
    protected void doFilterInternal(
               @NonNull
            HttpServletRequest request,
                @NonNull
            HttpServletResponse response,
                @NonNull
            FilterChain filterChain
    ) throws ServletException, IOException {
        // jeśli jesteśmy na ścieżce /api/v1/auth to nie sprawdzamy tokenu
        if (request.getServletPath().contains("/api/v1/auth")) {
            filterChain.doFilter(request, response);
            return;
        }
        String  authorization =request.getHeader("Authorization");
        // jeśli nie ma nagłówka Authorization lub nie zaczyna się od Bearer to nie sprawdzamy tokenu
        if (authorization == null ||!authorization.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        String jwt = authorization.substring(7);

        if (SecurityContextHolder.getContext().getAuthentication() == null && jwtService.isTokenValid(jwt)){
                SecurityContextHolder.getContext().setAuthentication(jwtService.getUsernamePasswordAuthenticationToken(jwt));
                filterChain.doFilter(request,response);

        }else {
            filterChain.doFilter(request,response);
        }
    }
}
