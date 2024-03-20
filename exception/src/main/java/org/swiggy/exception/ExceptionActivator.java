package org.swiggy.exception;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * <p>
 *  Customizes the starting and stopping of a bundle.
 * </p>
 *
 * @author Muthu kumar V
 * @version 1.0
 */
public class ExceptionActivator implements BundleActivator{

    /**
     * <p>
     * Invoked when the bundle is started.
     * </p>
     *
     * @param context The execution context of the bundle being started.
     */
    @Override
    public void start(final BundleContext context) {
        System.out.println("Exception Bundle Is Started");
    }

    /**
     * <p>
     * Invoked when the bundle is stopped.
     * </p>
     *
     * @param context The execution context of the bundle being stopped.
     */
    @Override
    public void stop(final BundleContext context) {
        System.out.println("Exception Bundle Is Stopped");
    }
}
