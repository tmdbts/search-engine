package pt.uc.dei.student.tmdbts.search_engine.webserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import java.rmi.RemoteException;

@SpringBootApplication
@ComponentScan(basePackages = "pt.uc.dei.student.tmdbts.search_engine")
public class DemoApp {
    @Bean
    public WebServerImpl webServerImpl() throws RemoteException {
        WebServerImpl webServerImpl = new WebServerImpl();
        webServerImpl.connect();
        return webServerImpl;
    }

    public static void main(String[] args) {
        SpringApplication.run(DemoApp.class, args);

    }
}
