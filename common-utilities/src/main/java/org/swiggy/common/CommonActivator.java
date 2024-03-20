package org.swiggy.common;

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
public class CommonActivator implements BundleActivator {

    /**
     * <p>
     * Invoked when the bundle is started.
     * </p>
     *
     * @param context The execution context of the bundle being started.
     */
    @Override
    public void start(final BundleContext context) {
        System.out.println("Shared Bundle Is Started");
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
        System.out.println("Shared Bundle Is Stopped");
    }
}
