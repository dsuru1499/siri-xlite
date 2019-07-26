package siri_xlite.service.common;

import lombok.Data;
import siri_xlite.common.Configuration;
import siri_xlite.common.DateTimeUtils;
import siri_xlite.common.Parameters;

import java.time.LocalDateTime;

@Data
public abstract class DefaultParameters implements Parameters {

    public static final String ENVELOPE = "Envelope";
    public static final String BODY = "Body";
    public static final String REQUEST = "Request";
    public static final String SEP = ".";

    public static final String PRODUCER_NAME = "ProducerName";
    public static final String PRODUCER_DOMAIN = "ProducerDomain";
    public static final String VERSION = "version";
    public static final String REQUEST_TIMESTAMP = "RequestTimestamp";
    public static final String ACCOUNT_ID = "AccountId";
    public static final String ACCOUNT_KEY = "AccountKey";
    public static final String REQUESTOR_REF = "RequestorRef";
    public static final String MESSAGE_IDENTIFIER = "MessageIdentifier";
    public static final String OPERATOR_REF = "OperatorRef";

    private LocalDateTime now = LocalDateTime.now();
    private String producerName;
    private String producerDomain;
    private String version = "2.0:FR-IDF-2.4";
    private LocalDateTime requestTimestamp;
    private String accountId;
    private String accountKey;
    private String requestorRef;
    private String messageIdentifier;
    private String operatorRef;

    public DefaultParameters() {
    }

    @Override
    public void configure(Configuration properties) throws SiriException {

        String requestTimestamp = properties.getFirst(REQUEST_TIMESTAMP);
        String accountId = properties.getFirst(ACCOUNT_ID);
        String accountKey = properties.getFirst(ACCOUNT_KEY);
        String requestorRef = properties.getFirst(REQUESTOR_REF);
        String messageIdentifier = properties.getFirst(MESSAGE_IDENTIFIER);
        String operatorRef = properties.getFirst(OPERATOR_REF);

        try {
            setProducerDomain(properties.getFirst(PRODUCER_DOMAIN));
            setProducerName(properties.getFirst(PRODUCER_NAME));
            setRequestTimestamp((requestTimestamp != null) ? DateTimeUtils.toLocalDateTime(requestTimestamp) : null);
            setAccountId(accountId);
            setAccountKey(accountKey);
            setRequestorRef(requestorRef);
            setMessageIdentifier(messageIdentifier);
            setOperatorRef(operatorRef);
        } catch (Exception e) {
            throw SiriException.createInvalidDataReferencesError(e.getMessage());
        }
    }

    @Override
    public void validate() throws SiriException {
    }

}
