package com.csn.soap_server;

// Імпортуємо згенеровані класи
import com.csn.soap_server.countries.GetCountryRequest;
import com.csn.soap_server.countries.GetCountryResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint // Позначає клас як SOAP Endpoint
public class CountryEndpoint {
    // Цей простір імен МАЄ ЗБІГАТИСЯ з targetNamespace у файлі countries.xsd
    public static final String NAMESPACE_URI = "http://soap_server.csn.com/countries";

    private final CountryRepository countryRepository;

    @Autowired // Автоматично підключаємо наш репозиторій
    public CountryEndpoint(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    // Цей метод буде викликатися, коли прийде запит з елементом "getCountryRequest"
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getCountryRequest")
    @ResponsePayload // Вказує, що результат методу треба перетворити на SOAP-відповідь
    public GetCountryResponse getCountry(@RequestPayload GetCountryRequest request) {
        GetCountryResponse response = new GetCountryResponse();
        // Шукаємо країну в репозиторії за ім'ям, отриманим із запиту
        response.setCountry(countryRepository.findCountry(request.getName()));

        return response;
    }
}