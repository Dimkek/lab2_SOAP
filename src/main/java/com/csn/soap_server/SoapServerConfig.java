package com.csn.soap_server;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurer; // Цей імпорт є в Лістингу [cite: 1101]
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

@EnableWs // Вмикає підтримку Spring Web Services (SOAP)
@Configuration // Позначає клас як конфігураційний
// Клас реалізує інтерфейс, як вказано у Лістингу 2.13 [cite: 1109]
public class SoapServerConfig implements WsConfigurer {

    // Цей бін реєструє спеціальний сервлет для обробки SOAP-запитів
    @Bean
    public ServletRegistrationBean<MessageDispatcherServlet> messageDispatcherServlet(ApplicationContext applicationContext) {
        MessageDispatcherServlet servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(applicationContext);
        servlet.setTransformWsdlLocations(true); // Дозволяє автоматично генерувати WSDL
        // Вказуємо, що SOAP-запити будуть приходити на адресу /ws/*
        return new ServletRegistrationBean<>(servlet, "/ws/*");
    }

    // Цей бін створює WSDL-файл на основі нашої XSD-схеми
    @Bean(name = "countries") // Ім'я біна "countries" буде частиною URL до WSDL
    public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema countriesSchema) {
        DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
        wsdl11Definition.setPortTypeName("CountriesPort");
        wsdl11Definition.setLocationUri("/ws"); // Вказуємо базову адресу сервісу
        wsdl11Definition.setTargetNamespace(CountryEndpoint.NAMESPACE_URI); // Простір імен з Endpoint
        wsdl11Definition.setSchema(countriesSchema); // Пов'язуємо з XSD-схемою
        return wsdl11Definition;
    }

    // Цей бін завантажує нашу XSD-схему з файлу
    @Bean
    public XsdSchema countriesSchema() {
        return new SimpleXsdSchema(new ClassPathResource("countries.xsd"));
    }
}