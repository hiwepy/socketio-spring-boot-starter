/*
 * Copyright (c) 2018, hiwepy (https://github.com/easy-4-java).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.corundumstudio.socketio.spring.boot.listener;

import com.corundumstudio.socketio.AuthorizationListener;
import com.corundumstudio.socketio.AuthorizationResult;
import com.corundumstudio.socketio.HandshakeData;
import org.springframework.util.StringUtils;

/**
 * Socket.IO {@link AuthorizationListener} that extracts a JWT (or any opaque token)
 * from the handshake data.
 *
 * <p>The token is read from the {@code X-Authorization} HTTP header by default,
 * falling back to the {@code token} query parameter. When a non-empty token is
 * present it is attached to the handshake as the auth token and authorization
 * succeeds; otherwise authorization fails.</p>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public class JWTAuthorizationListener implements AuthorizationListener {

	/** Default name of the query parameter carrying the authorization token. */
	public static final String AUTHORIZATION_PARAM = "token";
	/**
	 * HTTP Authorization header, equal to <code>Authorization</code>
	 */
	public static final String AUTHORIZATION_HEADER = "X-Authorization";

	private String authorizationHeaderName = AUTHORIZATION_HEADER;
	private String authorizationParamName = AUTHORIZATION_PARAM;

	/**
	 * Decide whether the handshake is authorized by looking for a non-empty token.
	 * @param data the handshake data
	 * @return {@link AuthorizationResult#SUCCESSFUL_AUTHORIZATION} if a token is present, otherwise {@link AuthorizationResult#FAILED_AUTHORIZATION}
	 */
	@Override
	public AuthorizationResult getAuthorizationResult(HandshakeData data) {
		String token = obtainToken(data);

		if (token == null) {
			token = "";
		}

		token = token.trim();
		if(StringUtils.hasText(token)) {
			data.setAuthToken(token);
			return AuthorizationResult.SUCCESSFUL_AUTHORIZATION;
		}
		return AuthorizationResult.FAILED_AUTHORIZATION;
	}

	/**
	 * Obtain the authorization token from the request, preferring the configured
	 * HTTP header and falling back to the configured query parameter.
	 * @param data the handshake data
	 * @return the authorization token, or {@code null} if absent
	 */
	protected String obtainToken(HandshakeData data) {
		// 从header中获取token
		String token = data.getHttpHeaders().get(getAuthorizationHeaderName());
		// 如果header中不存在token，则从参数中获取token
		if (StringUtils.isEmpty(token)) {
			return data.getSingleUrlParam(getAuthorizationParamName());
		}
		return token;
	}

	/**
	 * Get the HTTP header name used to read the authorization token.
	 * @return the authorization header name
	 */
	public String getAuthorizationHeaderName() {
		return authorizationHeaderName;
	}

	/**
	 * Set the HTTP header name used to read the authorization token.
	 * @param authorizationHeaderName the authorization header name
	 */
	public void setAuthorizationHeaderName(String authorizationHeaderName) {
		this.authorizationHeaderName = authorizationHeaderName;
	}

	/**
	 * Get the query parameter name used to read the authorization token.
	 * @return the authorization parameter name
	 */
	public String getAuthorizationParamName() {
		return authorizationParamName;
	}

	/**
	 * Set the query parameter name used to read the authorization token.
	 * @param authorizationParamName the authorization parameter name
	 */
	public void setAuthorizationParamName(String authorizationParamName) {
		this.authorizationParamName = authorizationParamName;
	}

}
