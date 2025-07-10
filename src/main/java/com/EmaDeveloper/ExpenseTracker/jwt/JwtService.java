package com.EmaDeveloper.ExpenseTracker.jwt;

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
import java.util.HashMap; // Para la sobrecarga de generateToken
import java.util.Map;   // Para la sobrecarga de generateToken
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration; // Tiempo de expiración en milisegundos

    /**
     * Extrae el nombre de usuario (subject) del token JWT.
     * @param token El token JWT.
     * @return El nombre de usuario (subject).
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
     * Genera un token JWT para un usuario con claims por defecto (solo el subject).
     * @param userDetails Detalles del usuario.
     * @return El token JWT generado.
     */
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    /**
     * Genera un token JWT con claims extra.
     * @param extraClaims Claims adicionales a incluir en el token.
     * @param userDetails Detalles del usuario.
     * @return El token JWT generado.
     */
    public String generateToken(
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
     * Valida si un token JWT es válido para un UserDetails dado.
     * Verifica que el nombre de usuario del token coincida con el UserDetails
     * y que el token no haya expirado.
     * @param token El token JWT a validar.
     * @param userDetails El UserDetails con el que se compara el token.
     * @return true si el token es válido, false en caso contrario.
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token); // Extrae el username/subject del token
        // Compara el username extraído del token con el username del UserDetails,
        // y verifica que el token no haya expirado.
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
                .setSigningKey(getSignInKey()) // Establece la clave de firma para la verificación
                .build()
                .parseClaimsJws(token) // Parsea el token JWT
                .getBody(); // Obtiene todos los claims del token
    }

    /**
     * Obtiene la clave de firma utilizada para firmar los tokens JWT.
     * @return La clave de firma como un objeto Key.
     */
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}