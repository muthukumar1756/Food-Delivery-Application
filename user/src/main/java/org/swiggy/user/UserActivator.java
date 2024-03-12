package org.swiggy.user;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.swiggy.restaurant.controller.RestRestaurantController;
import org.swiggy.user.controller.RestCartController;
import org.swiggy.user.controller.RestOrderController;
import org.swiggy.user.controller.RestUserController;

import java.util.ArrayList;
import java.util.List;

public class UserActivator implements BundleActivator {

    private Server server;

    @Override
    public void start(final BundleContext context) {
        final JAXRSServerFactoryBean bean = new JAXRSServerFactoryBean();

        bean.setAddress("/");
        bean.setProvider(new JacksonJsonProvider());
        bean.setServiceBean(getBeans());
        server = bean.create();

        System.out.println("User Bundle Is Started");
    }

    /**
     * <p>
     * Gets all the items in the user cart.
     * </p>
     *
     * @return The List of object n
     */
    private List<Object> getBeans() {
        List<Object> beans = new ArrayList<>();

        beans.add(RestRestaurantController.getInstance());
        beans.add(RestUserController.getInstance());
        beans.add(RestCartController.getInstance());
        beans.add(RestOrderController.getInstance());

        return beans;
    }

    @Override
    public void stop(final BundleContext context) {
        System.out.println("User Bundle Is Stopped");

        if (null != server) {
            server.destroy();
        }
    }
}
