package com.example.demo.config;

import java.security.Key;
import java.security.SignatureException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
	// Jwt Secret
	@Value("${jwt.secret}")
	private String jwtSecret;

	// Jwt Expiration in millis
	private Long jwtExpiration = 24 * 7 * 60000 * 60L;

	private Claims parseToken(String token)  {
		// Create JwtParser
		JwtParser jwtParser = Jwts.parserBuilder().setSigningKey(jwtSecret.getBytes()).build();

		try {
			return jwtParser.parseClaimsJws(token).getBody();
		} catch (UnsupportedJwtException e) {
			System.out.println(e.getMessage());
		} catch (MalformedJwtException e) {
			System.out.println(e.getMessage());
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

	public boolean validateToken(String token)  {
		return parseToken(token) != null;
	}

	public String getUsernameFromToken(String token) throws SignatureException {
		// Get claims
		Claims claims = parseToken(token);

		// Extract subject
		if (claims != null) {
			return claims.getSubject();
		}

		return null;
	}

	public String generateToken(String username) {
		// Create signing key
		Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes());

		// Generate token
		var currentDate = new Date();
		var expiration = new Date(currentDate.getTime() + jwtExpiration);

		return Jwts.builder().setSubject(username).setIssuedAt(currentDate).setExpiration(expiration).signWith(key)
				.compact();
	}

	public String generateTokenWithoutExpiration(String username) {
		Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes());

		return Jwts.builder()
				.setSubject(username)
				.setIssuedAt(new Date())
				.signWith(key)
				.compact();
	}
}
