package siri_xlite.common;

import siri_xlite.service.common.SiriException;

public interface Parameters {

    void configure(Configuration properties) throws SiriException;

    void validate() throws SiriException;

}