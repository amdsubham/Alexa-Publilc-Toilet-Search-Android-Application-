package com.amazon.alexa.avs.companion.interfaces.speechrecognizer;

import com.amazon.alexa.avs.companion.interfaces.AvsItem;

/**
 * {@link com.amazon.alexa.avs.companion.data.Directive} to prompt the user for a speech input
 *
 * {@link com.amazon.alexa.avs.companion.data.Directive} response item type parsed so we can properly
 * deal with the incoming commands from the Alexa server.
 *
 * @author will on 5/21/2016.
 */
public class AvsExpectSpeechItem extends AvsItem {
    long timeoutInMiliseconds;

    public AvsExpectSpeechItem(){
        this(null, 2000);
    }

    public AvsExpectSpeechItem(String token, long timeoutInMiliseconds){
        super(token);
        this.timeoutInMiliseconds = timeoutInMiliseconds;
    }

    public long getTimeoutInMiliseconds() {
        return timeoutInMiliseconds;
    }
}
