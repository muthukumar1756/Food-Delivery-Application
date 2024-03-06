package org.swiggy;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import org.apache.cxf.BusFactory;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.swiggy.controller.RestRestaurantController;

@Component
public class RestaurantActivator {

    private Server server;

    @Activate
    public void start() {
        final JAXRSServerFactoryBean bean = new JAXRSServerFactoryBean();

        bean.setAddress("/restaurant");
        bean.setProvider(new JacksonJsonProvider());
        bean.setBus(BusFactory.getDefaultBus());
        bean.setServiceBean(RestRestaurantController.getInstance());
        server = bean.create();

        System.out.println("Restaurant Bundle Is Started");
    }

    @Deactivate
    public void stop() {
        System.out.println("Restaurant Bundle Is Stopped");

        if (null != server) {
            server.destroy();
        }
    }
}
