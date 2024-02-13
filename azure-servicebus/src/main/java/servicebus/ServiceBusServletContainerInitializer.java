package servicebus;

import jakarta.servlet.ServletContainerInitializer;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import java.util.Set;

import org.apache.qpid.protonj2.engine.Connection;
import org.apache.qpid.protonj2.engine.Engine;
import org.apache.qpid.protonj2.engine.EngineFactory;

public class ServiceBusServletContainerInitializer implements ServletContainerInitializer {

    @Override
    public void onStartup(Set<Class<?>> c, ServletContext ctx) throws ServletException {
        Engine engine = EngineFactory.PROTON.createEngine();
        Connection connection = engine.start();
        System.out.println(connection.getHostname());
    }
    
    public static void main(String[] arguments) throws Exception {
        ServiceBusServletContainerInitializer initializer = new ServiceBusServletContainerInitializer();
        initializer.onStartup(null, null);
        Thread.sleep(600000);
    }
}
