package com.bat.configurations;

import com.bat.webdrivers.ChromeWebDriver;
import org.springframework.context.annotation.*;

@Configuration
@ComponentScan(basePackages = "com.bat")
@PropertySources({
        @PropertySource(value = "classpath:browser_config.properties"),
        @PropertySource(value = "classpath:selenium.properties")
})
public class SpringConfig {
}
