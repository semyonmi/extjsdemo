package ru.semyonmi.extjsdemo.security;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;

public class EDClientDetailsService implements ClientDetailsService {

    private ClientDetails clientDetails;

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        if (!StringUtils.equals(clientDetails.getClientId(), clientId)) {
            throw new ClientRegistrationException("Client \"" + clientId + "\" not found");
        }
        return clientDetails;
    }

    public void setClientDetails(ClientDetails clientDetails) {
        this.clientDetails = clientDetails;
    }
}