package com.chat.app.service.impl;

import java.security.Key;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.chat.app.service.IJwtService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtServiceImpl implements IJwtService {

	public String generateToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("role", userDetails.getAuthorities().toString());

		return Jwts.builder().setClaims(claims).setSubject(userDetails.getUsername())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
				.signWith(getSigninKey(), SignatureAlgorithm.HS256).compact();
	}

	public String generateRefreshToken(Map<String, Object> extraClaims, UserDetails userDetails) {
		return Jwts.builder().setClaims(extraClaims).setSubject(userDetails.getUsername())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 604800000))
				.signWith(getSigninKey(), SignatureAlgorithm.HS256).compact();
	}

	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
		final Claims claims = extractAllClaims(token);
		return claimsResolvers.apply(claims);
	}

	private Claims extractAllClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(getSigninKey()).build().parseClaimsJws(token).getBody();
	}

	public List<String> extractRoles(String token) {
		String rolesString = extractClaim(token, claims -> claims.get("role", String.class));
		return parseRoles(rolesString);
	}

	private List<String> parseRoles(String rolesString) {
		if (rolesString == null || rolesString.isEmpty()) {
			return new ArrayList<>();
		}
		rolesString = rolesString.replace("[", "").replace("]", "");
		String[] rolesArray = rolesString.split(",\\s*");
		return Arrays.asList(rolesArray);
	}

	private Key getSigninKey() {
		byte[] key = Decoders.BASE64.decode("d2e6c0FeuifzD2Ly5dy4P39zHRRxJRr1Eh1W08b69ZM=");
		return Keys.hmacShaKeyFor(key);
	}

	public boolean isTokenValid(String token, UserDetails userDetails) {
		try {
			final String username = extractUsername(token);
			// return (username.equals(userDetails.getUsername()) &&
			// !isTokenExpired(token));
			if (username.equals(userDetails.getUsername()) && !isTokenExpired(token)) {
				return true;
			}

			throw new RuntimeException("Token is expired");

		} catch (Exception e) {
			return false;
		}
	}

	private boolean isTokenExpired(String token) {
		return extractClaim(token, Claims::getExpiration).before(new Date());
	}

	public Date isTokenExpiredTime(String token) {
		// return extractClaim(token, Claims::getExpiration);
		Date expiration = extractClaim(token, Claims::getExpiration);

		// Adjust expiration time to UTC+7
		long expirationTimeMillis = expiration.getTime();
		long adjustedExpirationTimeMillis = expirationTimeMillis; // 7 hours in milliseconds:
		Date adjustedExpiration = new Date(adjustedExpirationTimeMillis);

		return adjustedExpiration;
	}
}
