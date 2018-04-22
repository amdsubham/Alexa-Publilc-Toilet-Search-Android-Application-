package com.amazon.alexa.avs.companion.interfaces.errors;

import com.amazon.alexa.avs.companion.data.Directive;
import com.amazon.alexa.avs.companion.interfaces.AvsItem;

/**
 * Created by will on 6/26/2016.
 */

public class AvsResponseException extends AvsItem {
    Directive directive;
    public AvsResponseException(Directive directive) {
        super(null);
        this.directive = directive;
    }

    public Directive getDirective() {
        return directive;
    }
}
