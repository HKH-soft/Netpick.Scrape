package ir.netpick.mailmine.scrape.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@Component
@ConfigurationProperties(prefix = "web")
public class WebProperties {
    private String chromeLocation;
    private String driverLocation;

    public String getChromeLocation() {
        return chromeLocation;
    }

    public void setChromeLocation(String chromeLocation) {
        this.chromeLocation = chromeLocation;
    }

    public String getDriverLocation() {
        return driverLocation;
    }

    public void setDriverLocation(String driverLocation) {
        this.driverLocation = driverLocation;
    }
}
