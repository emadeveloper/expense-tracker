package com.EmaDeveloper.ExpenseTracker.Jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    /**
     * Clave secreta para firmar los tokens JWT.
     * Esta clave debe ser segura y no debe ser expuesta públicamente.
     */
    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration; // Tiempo de expiración en milisegundos

    /**
     * Extrae el nombre de usuario del token JWT.
     * @param token El token JWT.
     * @return El nombre de usuario.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extrae un 'claim' específico del token JWT.
     * Un 'claim' es una pieza de información dentro del token (ej. subject, expiration, roles).
     * @param token El token JWT.
     * @param claimsResolver Función para resolver el claim deseado.
     * @param <T> Tipo del claim.
     * @return El claim extraído.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Genera un token JWT para un usuario.
     * @param userDetails Detalles del usuario.
     * @return El token JWT generado.
     */
    public String generateTokenExtraClaims(UserDetails userDetails) {
        return generateTokenExtraClaims(new HashMap<>(), userDetails);
    }

    /**
     * Genera un token JWT con claims extra.
     * @param extraClaims Claims adicionales a incluir en el token.
     * @param userDetails Detalles del usuario.
     * @return El token JWT generado.
     */
    public String generateTokenExtraClaims(
            Map<String, Object> extraClaims,
            UserDetails userDetails
    ) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Valida un token JWT.
     * @param token El token JWT a validar.
     * @param userDetails Detalles del usuario para comparar con el token.
     * @return true si el token es válido, false en caso contrario.
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    /**
     * Verifica si el token ha expirado.
     * @param token El token JWT.
     * @return true si el token ha expirado, false en caso contrario.
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Extrae la fecha de expiración del token.
     * @param token El token JWT.
     * @return La fecha de expiración.
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extrae todos los claims del token JWT.
     * @param token El token JWT.
     * @return Un objeto Claims que contiene todos los claims.
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey()) // Establece la clave de firma
                .build()
                .parseClaimsJws(token) // Parsea el token JWT
                .getBody(); // Obtiene los claims
    }
    /**
     * Obtiene la clave de firma (Key) a partir de la clave secreta en Base64.
     * @return La clave de firma.
     */
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}