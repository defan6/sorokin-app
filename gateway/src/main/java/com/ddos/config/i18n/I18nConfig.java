package com.ddos.config.i18n;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContext;
import org.springframework.context.i18n.SimpleLocaleContext;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.i18n.AcceptHeaderLocaleContextResolver;
import org.springframework.web.server.i18n.LocaleContextResolver;

import java.util.Locale;

@Configuration
public class I18nConfig {

    @Bean
    public LocaleContextResolver localeContextResolver() {
        return new LocaleContextResolver() {
            @Override
            public LocaleContext resolveLocaleContext(ServerWebExchange exchange) {
                String lang = exchange.getRequest().getQueryParams().getFirst("lang");
                Locale locale;
                if(lang != null){
                    locale = Locale.forLanguageTag(lang);
                } else {
                    locale = exchange.getRequest()
                            .getHeaders()
                            .getAcceptLanguageAsLocales()
                            .stream()
                            .findFirst()
                            .orElse(Locale.ENGLISH);
                }
                return new SimpleLocaleContext(locale);
            }

            @Override
            public void setLocaleContext(ServerWebExchange exchange, LocaleContext localeContext) {

            }
        };
    }


    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource ms = new ResourceBundleMessageSource();
        ms.setBasename("i18n/messages"); // путь к messages.properties
        ms.setDefaultEncoding("UTF-8");
        return ms;
    }
}
