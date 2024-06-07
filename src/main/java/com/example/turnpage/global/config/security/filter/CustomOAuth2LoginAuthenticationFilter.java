package com.example.turnpage.global.config.security.filter;

import com.example.turnpage.global.error.BusinessException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2LoginAuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationExchange;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationResponse;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.function.Consumer;

import static com.example.turnpage.global.error.code.MemberErrorCode.CLIENT_REGISTRATION_NOT_FOUND;

public class CustomOAuth2LoginAuthenticationFilter extends OAuth2LoginAuthenticationFilter {
    public static final String TURNPAGE_FILTER_PROCESSES_URI = "/auth/login/*";

    private static final String CLIENT_REGISTRATION_NOT_FOUND_ERROR_CODE = "client_registration_not_found";
    private ClientRegistrationRepository clientRegistrationRepository;
    private OAuth2AuthorizedClientRepository authorizedClientRepository;
    private AuthorizationRequestRepository<OAuth2AuthorizationRequest> authorizationRequestRepository;
    private Converter<OAuth2LoginAuthenticationToken, OAuth2AuthenticationToken> authenticationResultConverter = this::createAuthenticationResult;

    private Consumer<OAuth2AuthorizationRequest.Builder> authorizationRequestCustomizer = (customizer) -> {
    };

    public CustomOAuth2LoginAuthenticationFilter(ClientRegistrationRepository clientRegistrationRepository, OAuth2AuthorizedClientRepository authorizedClientRepository,
                                                 AuthenticationManager authenticationManager) {
        super(clientRegistrationRepository, authorizedClientRepository, TURNPAGE_FILTER_PROCESSES_URI);
        this.setAuthenticationManager(authenticationManager);
        this.clientRegistrationRepository = clientRegistrationRepository;
        this.authorizedClientRepository = authorizedClientRepository;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        MultiValueMap<String, String> params = toMultiMap(request.getParameterMap());

        if (!isAuthorizationResponseSuccess(params)) {
            OAuth2Error oauth2Error = new OAuth2Error(OAuth2ErrorCodes.INVALID_REQUEST);
            throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString());
        }

        String registrationId = getRegistrationId(request.getRequestURI());
        ClientRegistration clientRegistration = this.clientRegistrationRepository.findByRegistrationId(registrationId);
        if (clientRegistration == null) {
            OAuth2Error oauth2Error = new OAuth2Error(CLIENT_REGISTRATION_NOT_FOUND_ERROR_CODE,
                    "Client Registration not found with Id: " + registrationId, null);
            throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString());
        }

        final OAuth2AuthorizationResponse authorizationResponse;
        if (registrationId.equals("kakao")) {
            authorizationResponse = convert(params, clientRegistration.getRedirectUri());
        } else if (registrationId.equals("google")) {
            authorizationResponse = convert(params, clientRegistration.getRedirectUri());
        } else {
            throw new BusinessException(CLIENT_REGISTRATION_NOT_FOUND);
        }

        Object authenticationDetails = this.authenticationDetailsSource.buildDetails(request);
        OAuth2AuthorizationRequest authorizationRequest = createAuthorizationRequest(clientRegistration, String.valueOf(params.get("state").get(0)));

        OAuth2LoginAuthenticationToken authenticationRequest = new OAuth2LoginAuthenticationToken(clientRegistration,
                new OAuth2AuthorizationExchange(authorizationRequest, authorizationResponse));

        authenticationRequest.setDetails(authenticationDetails);
        OAuth2LoginAuthenticationToken authenticationResult = (OAuth2LoginAuthenticationToken) this
                .getAuthenticationManager()
                .authenticate(authenticationRequest);

        OAuth2AuthenticationToken oauth2Authentication = this.authenticationResultConverter
                .convert(authenticationResult);
        Assert.notNull(oauth2Authentication, "authentication result cannot be null");
        oauth2Authentication.setDetails(authenticationDetails);
        OAuth2AuthorizedClient authorizedClient = new OAuth2AuthorizedClient(
                authenticationResult.getClientRegistration(), oauth2Authentication.getName(),
                authenticationResult.getAccessToken(), authenticationResult.getRefreshToken());

        this.authorizedClientRepository.saveAuthorizedClient(authorizedClient, oauth2Authentication, request, response);
        return oauth2Authentication;
    }

    private OAuth2AuthorizationRequest createAuthorizationRequest(ClientRegistration clientRegistration, String state) {
        OAuth2AuthorizationRequest.Builder builder = OAuth2AuthorizationRequest.authorizationCode();
        builder.clientId(clientRegistration.getClientId())
                .authorizationUri(clientRegistration.getProviderDetails().getAuthorizationUri())
                .redirectUri(clientRegistration.getRedirectUri())
                .scopes(clientRegistration.getScopes())
                .state(state);
        this.authorizationRequestCustomizer.accept(builder);

        return builder.build();
    }

    private static MultiValueMap<String, String> toMultiMap(Map<String, String[]> map) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>(map.size());
        map.forEach((key, values) -> {
            if (values.length > 0) {
                for (String value : values) {
                    params.add(key, value);
                }
            }
        });
        return params;
    }

    private static boolean isAuthorizationResponseSuccess(MultiValueMap<String, String> request) {
        return StringUtils.hasText(request.getFirst(OAuth2ParameterNames.CODE))
                && StringUtils.hasText(request.getFirst(OAuth2ParameterNames.STATE));
    }

    private static String getRegistrationId(String path) throws IndexOutOfBoundsException {
        String[] pathList = path.split("/");
        return pathList[pathList.length - 1];
    }

    static OAuth2AuthorizationResponse convert(MultiValueMap<String, String> request, String redirectUri) {
        String code = request.getFirst(OAuth2ParameterNames.CODE);
        String errorCode = request.getFirst(OAuth2ParameterNames.ERROR);
        String state = request.getFirst(OAuth2ParameterNames.STATE);
        if (StringUtils.hasText(code)) {
            return OAuth2AuthorizationResponse.success(code).redirectUri(redirectUri).state(state).build();
        }
        String errorDescription = request.getFirst(OAuth2ParameterNames.ERROR_DESCRIPTION);
        String errorUri = request.getFirst(OAuth2ParameterNames.ERROR_URI);
        // @formatter:off
        return OAuth2AuthorizationResponse.error(errorCode)
                .redirectUri(redirectUri)
                .errorDescription(errorDescription)
                .errorUri(errorUri)
                .state(state)
                .build();
        // @formatter:on
    }

    private OAuth2AuthenticationToken createAuthenticationResult(OAuth2LoginAuthenticationToken authenticationResult) {
        return new OAuth2AuthenticationToken(authenticationResult.getPrincipal(), authenticationResult.getAuthorities(),
                authenticationResult.getClientRegistration().getRegistrationId());
    }
}
