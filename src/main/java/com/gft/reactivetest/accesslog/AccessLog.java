package com.gft.reactivetest.accesslog;

import com.gft.reactivetest.security.User;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.message.StringMapMessage;
import org.springframework.http.HttpStatus;

import java.net.InetSocketAddress;

@Log4j2
public class AccessLog {

    public static final String DURATION = "duration";
    public static final String REMOTE_ADDRESS = "remoteAddress";
    public static final String METHOD = "method";
    public static final String URI = "uri";
    public static final String STATUS = "status";
    public static final String USERNAME = "username";
    public static final String USER_TYPE = "userType";
    public static final String TRACE_ID = "traceId";

    private long startTime;
    private StringMapMessage mapMessage;

    public AccessLog(long startTime) {
        this.startTime = startTime;
        init();
    }

    private void init() {
        mapMessage = new StringMapMessage();
        addParameter(DURATION, null);
        addParameter(REMOTE_ADDRESS, null);
        addParameter(METHOD, null);
        addParameter(URI, null);
        addParameter(STATUS, null);
        addParameter(USERNAME, null);
        addParameter(USER_TYPE, null);
        addParameter(TRACE_ID, null);
    }

    public void log() {
        addParameter(DURATION, String.valueOf(System.currentTimeMillis() - startTime));
        log.info(mapMessage);
    }

    public AccessLog remoteAddress(InetSocketAddress address) {
        if (address != null)
            addParameter(REMOTE_ADDRESS, address.getHostString());
        return this;
    }

    public AccessLog method(String method) {
        addParameter(METHOD, method);
        return this;
    }

    public AccessLog uri(java.net.URI uri) {
        if (uri != null)
            addParameter(URI, uri.getPath());
        return this;
    }

    public AccessLog status(HttpStatus status) {
        if (status != null)
            addParameter(STATUS, String.valueOf(status.value()));
        return this;
    }

    public AccessLog user(User user) {
        if (user != null) {
            addParameter(USERNAME, user.getUsername());
            addParameter(USER_TYPE, user.getType());
        }
        return this;
    }

    public AccessLog traceId(String traceId) {
        addParameter(TRACE_ID, traceId);
        return this;
    }

    private void addParameter(String key, String parameter) {
        String value = StringUtils.defaultIfBlank(parameter, key + "-unavailable");
        mapMessage.put(key, value);
    }
}
