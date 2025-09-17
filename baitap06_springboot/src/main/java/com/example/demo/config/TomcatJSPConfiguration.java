package com.example.demo.config;

import org.apache.catalina.Context;
import org.apache.catalina.Lifecycle;
import org.apache.catalina.LifecycleEvent;
import org.apache.catalina.LifecycleListener;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TomcatJSPConfiguration {
    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> staticResourceCustomizer() {
        return factory -> factory.addContextCustomizers(context -> context.addLifecycleListener(new JSPStaticResourceConfigurer(context)));
    }
}

class JSPStaticResourceConfigurer implements LifecycleListener {
    private final Context context;
    private final String subPath = "/META-INF/resources";

    public JSPStaticResourceConfigurer(Context context) {
        this.context = context;
    }

    @Override
    public void lifecycleEvent(LifecycleEvent event) {
        if (event.getType().equals(Lifecycle.CONFIGURE_START_EVENT)) {
            context.getResources().createWebResourceSet(
                org.apache.catalina.WebResourceRoot.ResourceSetType.RESOURCE_JAR,
                "/",
                getUrl(),
                subPath
            );
        }
    }

    private java.net.URL getUrl() {
        java.net.URL location = this.getClass().getProtectionDomain().getCodeSource().getLocation();
        if (location == null) throw new IllegalStateException("Cannot determine code source location");
        return location;
    }
}