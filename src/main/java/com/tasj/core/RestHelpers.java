package com.tasj.core;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import org.jboss.resteasy.util.Base64;

public class RestHelpers {
    public static Invocation.Builder requestTo(String uri) {
        return ClientBuilder.newClient().target(uri).request();
    }

    public static Invocation.Builder authorized(Invocation.Builder requestBuilder, String login, String password) {
        return requestBuilder.header("Authorization", "Basic " + Base64.encodeBytes((login + ":" + password).getBytes()));
    }


}
