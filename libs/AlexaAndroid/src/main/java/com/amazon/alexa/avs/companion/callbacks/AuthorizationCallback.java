package com.amazon.alexa.avs.companion.callbacks;

/**
 * A callback to handle three states of Amazon authorization
 */
public interface AuthorizationCallback {
    void onCancel();
    void onSuccess();
    void onError(Exception error);
}
