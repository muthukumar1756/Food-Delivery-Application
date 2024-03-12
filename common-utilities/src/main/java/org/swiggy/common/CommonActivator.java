package org.swiggy.common;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class CommonActivator implements BundleActivator {

    @Override
    public void start(final BundleContext context) {
        System.out.println("Shared Bundle Is Started");
    }

    @Override
    public void stop(final BundleContext context) {
        System.out.println("Shared Bundle Is Stopped");
    }
}
