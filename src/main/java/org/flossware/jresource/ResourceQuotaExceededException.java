package org.flossware.jresource;

/**
 * Thrown when a resource quota is exceeded.
 */
public class ResourceQuotaExceededException extends RuntimeException {
    public ResourceQuotaExceededException(String message) {
        super(message);
    }
}
