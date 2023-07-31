package io.openliberty.deepdive.rest.client;

import java.util.logging.Logger;

import org.eclipse.microprofile.rest.client.ext.ResponseExceptionMapper;

import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;

public class UnknownUriExceptionMapper
    implements ResponseExceptionMapper<UnknownUriException> {
    Logger LOG = Logger.getLogger(UnknownUriExceptionMapper.class.getName());

    @Override
    public boolean handles(int status, MultivaluedMap<String, Object> headers) {
        LOG.info("status = " + status);
        return status == 404;
    }

    @Override
    public UnknownUriException toThrowable(Response response) {
        return new UnknownUriException();
    }
}

