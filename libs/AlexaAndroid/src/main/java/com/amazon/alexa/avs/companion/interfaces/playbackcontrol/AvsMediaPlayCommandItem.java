package com.amazon.alexa.avs.companion.interfaces.playbackcontrol;

import com.amazon.alexa.avs.companion.interfaces.AvsItem;

/**
 * {@link com.amazon.alexa.avs.companion.data.Directive} to send a play command to any app playing media
 *
 * This directive doesn't seem applicable to mobile applications
 *
 * @author will on 5/31/2016.
 */

public class AvsMediaPlayCommandItem extends AvsItem {
    public AvsMediaPlayCommandItem(String token) {
        super(token);
    }
}
