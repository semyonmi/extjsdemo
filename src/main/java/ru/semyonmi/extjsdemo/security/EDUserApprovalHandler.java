package ru.semyonmi.extjsdemo.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.security.oauth2.provider.approval.ApprovalStoreUserApprovalHandler;

public class EDUserApprovalHandler extends ApprovalStoreUserApprovalHandler {

    /**
     * Allows automatic approval for a white list of clients in the implicit grant case.
     *
     * @param authorizationRequest The authorization request.
     * @param userAuthentication   the current user authentication
     * @return An updated request if it has already been approved by the current user.
     */
    @Override
    public AuthorizationRequest checkForPreApproval(AuthorizationRequest authorizationRequest,
                                                    Authentication userAuthentication) {
        if (authorizationRequest.isApproved()) {
            return authorizationRequest;
        }
        authorizationRequest = super.checkForPreApproval(authorizationRequest, userAuthentication);

        return authorizationRequest;
    }
}
