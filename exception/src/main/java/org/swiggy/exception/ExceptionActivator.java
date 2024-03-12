package org.swiggy.exception;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class ExceptionActivator implements BundleActivator{

    @Override
    public void start(final BundleContext context) {
        System.out.println("Exception Bundle Is Started");
    }

    @Override
    public void stop(final BundleContext context) {
        System.out.println("Exception Bundle Is Stopped");
    }
}
