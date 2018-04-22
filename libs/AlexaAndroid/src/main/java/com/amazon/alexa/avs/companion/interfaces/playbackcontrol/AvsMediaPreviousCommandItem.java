package com.amazon.alexa.avs.companion.interfaces.playbackcontrol;

import com.amazon.alexa.avs.companion.interfaces.AvsItem;

/**
 * {@link com.amazon.alexa.avs.companion.data.Directive} to send a previous command to any app playing media
 *
 * This directive doesn't seem applicable to mobile applications
 *
 * @author will on 5/31/2016.
 */

public class AvsMediaPreviousCommandItem extends AvsItem {
    public AvsMediaPreviousCommandItem(String token) {
        super(token);
    }
}
