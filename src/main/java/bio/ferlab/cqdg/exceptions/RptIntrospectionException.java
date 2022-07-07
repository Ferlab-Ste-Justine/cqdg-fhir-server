package bio.ferlab.cqdg.exceptions;

import ca.uhn.fhir.rest.server.exceptions.AuthenticationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RptIntrospectionException extends AuthenticationException {
    private static final Logger log = LoggerFactory.getLogger(RptIntrospectionException.class);

    public RptIntrospectionException(String msg) {
        super(msg);
        log.error(msg); // log.error(msg, this); to have the stacktrace
    }
}
