package com.fullstackbootcamp.capstoneBackend.notifications.config;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

public final class WebsocketContextChannelInterceptor implements ChannelInterceptor {

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        if (message instanceof GenericMessage<?> genericMessage &&
                genericMessage.getHeaders().get("simpUser") instanceof PreAuthenticatedAuthenticationToken token) {
            SecurityContextHolder.getContext().setAuthentication(token);
        }
        return message;
    }
}