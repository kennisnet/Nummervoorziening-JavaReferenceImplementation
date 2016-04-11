package nl.kennisnet.nummervoorziening.client.schoolid;

import java.util.Collections;
import java.util.Set;
import javax.xml.namespace.QName;
import javax.xml.soap.*;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

public class AuthorizedSoapHeaderOinInterceptor implements SOAPHandler<SOAPMessageContext> {

    // The Namespace of the WS-Addressing version
    protected static final String ADDRESSING_NS = "http://www.w3.org/2005/08/addressing";

    // The suffix used in the Address elements as defined by Edukoppeling
    protected static final String ANONYMOUS_OIN = ADDRESSING_NS + "/anonymous?oin=";

    @Override
    public boolean handleMessage(SOAPMessageContext context) {
        Boolean isRequest = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);

        // True for outbound messages, false for inbound
        if (isRequest) {
            try {
                SOAPMessage soapMsg = context.getMessage();
                SOAPEnvelope soapEnv = soapMsg.getSOAPPart().getEnvelope();
                SOAPHeader soapHeader = soapEnv.getHeader();

                SOAPHeaderElement soapHeaderElement = soapHeader.addHeaderElement(new QName(ADDRESSING_NS, "From"));
                SOAPElement addressElement = soapHeaderElement.addChildElement(new QName(ADDRESSING_NS, "Address"));

                String fromValue = ANONYMOUS_OIN + SchoolIDServiceUtil.getInstanceOin();
                addressElement.addTextNode(fromValue);

            } catch(SOAPException e) {
                System.err.println(e);
            }
        }

        return true;
    }

    @Override
    public boolean handleFault(SOAPMessageContext context) {
        return true;
    }

    @Override
    public void close(MessageContext context) { }

    @Override
    public Set<QName> getHeaders() {
        return Collections.emptySet();
    }
}
