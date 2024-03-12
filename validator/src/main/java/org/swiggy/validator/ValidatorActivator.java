package org.swiggy.validator;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class ValidatorActivator implements BundleActivator {

    @Override
    public void start(final BundleContext context) {
        System.out.println("Validator Bundle Is Started");
    }

    @Override
    public void stop(final BundleContext context) {
        System.out.println("Validator Bundle Is Stopped");
    }
}
