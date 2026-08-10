package com.corundumstudio.socketio.spring.boot.listener;

import com.corundumstudio.socketio.AuthorizationResult;
import com.corundumstudio.socketio.HandshakeData;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpHeaders;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link JWTAuthorizationListener}.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
class JWTAuthorizationListenerTest {

    private JWTAuthorizationListener listener;

    @BeforeEach
    void setUp() {
        listener = new JWTAuthorizationListener();
    }

    @Test
    void authorizationResultShouldFailWhenNoTokenPresent() {
        HandshakeData data = createHandshakeData(null, null);
        AuthorizationResult result = listener.getAuthorizationResult(data);
        assertThat(result).isEqualTo(AuthorizationResult.FAILED_AUTHORIZATION);
    }

    @Test
    void authorizationResultShouldFailWhenEmptyTokenInHeader() {
        HandshakeData data = createHandshakeData("", null);
        AuthorizationResult result = listener.getAuthorizationResult(data);
        assertThat(result).isEqualTo(AuthorizationResult.FAILED_AUTHORIZATION);
    }

    @Test
    void authorizationResultShouldFailWhenNullTokenAndNullParam() {
        HandshakeData data = createHandshakeData(null, null);
        AuthorizationResult result = listener.getAuthorizationResult(data);
        assertThat(result).isEqualTo(AuthorizationResult.FAILED_AUTHORIZATION);
    }

    @Test
    void authorizationResultShouldSucceedWhenTokenInHeader() {
        HandshakeData data = createHandshakeData("my-jwt-token", null);
        AuthorizationResult result = listener.getAuthorizationResult(data);
        assertThat(result).isEqualTo(AuthorizationResult.SUCCESSFUL_AUTHORIZATION);
    }

    @Test
    void authorizationResultShouldSucceedWhenTokenInParam() {
        HandshakeData data = createHandshakeData(null, "param-token");
        AuthorizationResult result = listener.getAuthorizationResult(data);
        assertThat(result).isEqualTo(AuthorizationResult.SUCCESSFUL_AUTHORIZATION);
    }

    @Test
    void headerTokenShouldTakePrecedenceOverParam() {
        HandshakeData data = createHandshakeData("header-token", "param-token");
        AuthorizationResult result = listener.getAuthorizationResult(data);
        assertThat(result).isEqualTo(AuthorizationResult.SUCCESSFUL_AUTHORIZATION);
    }

    @Test
    void defaultHeaderNameShouldBeXAuthorization() {
        assertThat(listener.getAuthorizationHeaderName()).isEqualTo("X-Authorization");
    }

    @Test
    void defaultParamNameShouldBeToken() {
        assertThat(listener.getAuthorizationParamName()).isEqualTo("token");
    }

    @Test
    void setAuthorizationHeaderNameShouldStoreValue() {
        listener.setAuthorizationHeaderName("Custom-Header");
        assertThat(listener.getAuthorizationHeaderName()).isEqualTo("Custom-Header");
    }

    @Test
    void setAuthorizationParamNameShouldStoreValue() {
        listener.setAuthorizationParamName("customParam");
        assertThat(listener.getAuthorizationParamName()).isEqualTo("customParam");
    }

    @Test
    void shouldUseCustomHeaderName() {
        listener.setAuthorizationHeaderName("Auth-Token");
        HttpHeaders headers = new DefaultHttpHeaders();
        headers.set("Auth-Token", "custom-jwt");
        HandshakeData data = new HandshakeData(headers, Collections.emptyMap(), InetSocketAddress.createUnresolved("localhost", 8080), "/socket.io", false);
        AuthorizationResult result = listener.getAuthorizationResult(data);
        assertThat(result).isEqualTo(AuthorizationResult.SUCCESSFUL_AUTHORIZATION);
    }

    @Test
    void shouldUseCustomParamName() {
        listener.setAuthorizationParamName("accessToken");
        Map<String, List<String>> urlParams = Map.of("accessToken", List.of("custom-param-token"));
        HandshakeData data = new HandshakeData(new DefaultHttpHeaders(), urlParams, InetSocketAddress.createUnresolved("localhost", 8080), "/socket.io", false);
        AuthorizationResult result = listener.getAuthorizationResult(data);
        assertThat(result).isEqualTo(AuthorizationResult.SUCCESSFUL_AUTHORIZATION);
    }

    @Test
    void constantsShouldBeCorrect() {
        assertThat(JWTAuthorizationListener.AUTHORIZATION_PARAM).isEqualTo("token");
        assertThat(JWTAuthorizationListener.AUTHORIZATION_HEADER).isEqualTo("X-Authorization");
    }

    private HandshakeData createHandshakeData(String headerToken, String paramToken) {
        HttpHeaders headers = new DefaultHttpHeaders();
        if (headerToken != null) {
            headers.set(JWTAuthorizationListener.AUTHORIZATION_HEADER, headerToken);
        }
        Map<String, List<String>> urlParams;
        if (paramToken != null) {
            urlParams = Map.of(JWTAuthorizationListener.AUTHORIZATION_PARAM, List.of(paramToken));
        } else {
            urlParams = Collections.emptyMap();
        }
        return new HandshakeData(headers, urlParams, InetSocketAddress.createUnresolved("localhost", 8080), "/socket.io", false);
    }
}
