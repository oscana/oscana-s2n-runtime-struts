package oscana.s2n.sample.support.message;

import java.util.HashMap;
import java.util.Map;

import nablarch.core.message.BasicStringResource;
import nablarch.core.message.MessageNotFoundException;
import nablarch.core.message.StringResource;
import nablarch.core.message.StringResourceHolder;

public class MockStringResourceHolder extends StringResourceHolder {

    private Map<String, StringResource> messages = new HashMap<String, StringResource>();

    public void setMessages(String[][] messages) {
        for (String[] params : messages) {
            String msgId = params[0];
            Map<String, String> formats = new HashMap<String, String>();
            for (int i = 0; i * 2 + 2 <= params.length; i++) {
                formats.put(params[i * 2 + 1], params[i * 2 + 2]);
            }

            this.messages.put(msgId, new BasicStringResource(msgId, formats));
        }

    }

    @Override
    public StringResource get(String messageId) {
        StringResource message = messages.get(messageId);

        if (message == null) {
            throw new MessageNotFoundException("message was not found. message id = " + messageId);
        }

        return message;
    }
}
