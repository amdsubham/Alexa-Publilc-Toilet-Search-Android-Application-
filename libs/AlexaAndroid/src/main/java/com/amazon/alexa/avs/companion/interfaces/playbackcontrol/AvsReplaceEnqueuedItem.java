package com.amazon.alexa.avs.companion.interfaces.playbackcontrol;

import com.amazon.alexa.avs.companion.interfaces.AvsItem;

/**
 * Directive to replace all items in the queue, but leave the playing item
 *
 * {@link com.amazon.alexa.avs.companion.data.Directive} response item type parsed so we can properly
 * deal with the incoming commands from the Alexa server.
 *
 * @author will on 5/21/2016.
 */
public class AvsReplaceEnqueuedItem extends AvsItem {
    public AvsReplaceEnqueuedItem(String token) {
        super(token);
    }
}
