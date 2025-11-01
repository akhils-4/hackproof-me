package com.hackproofme.service;


import com.hackproofme.model.BreachData;
import com.hackproofme.model.BreachResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Service
@Slf4j
public class BreachLookupService {

    private static final List<BreachData> MOCK_BREACHES = Arrays.asList(
            BreachData.builder()
                    .name("LinkedIn 2021")
                    .date("2021-06-22")
                    .dataClasses(Arrays.asList("Email addresses", "Full names", "Job titles", "Phone numbers"))
                    .build(),
            BreachData.builder()
                    .name("Facebook 2019")
                    .date("2019-04-03")
                    .dataClasses(Arrays.asList("Email addresses", "Names", "Phone numbers", "Locations"))
                    .build(),
            BreachData.builder()
                    .name("Adobe 2013")
                    .date("2013-10-04")
                    .dataClasses(Arrays.asList("Email addresses", "Passwords", "Usernames"))
                    .build(),
            BreachData.builder()
                    .name("Dropbox 2012")
                    .date("2012-07-01")
                    .dataClasses(Arrays.asList("Email addresses", "Passwords"))
                    .build(),
            BreachData.builder()
                    .name("Twitter 2022")
                    .date("2022-01-01")
                    .dataClasses(Arrays.asList("Email addresses", "Usernames", "Names"))
                    .build(),
            BreachData.builder()
                    .name("Marriott 2018")
                    .date("2018-11-30")
                    .dataClasses(Arrays.asList("Email addresses", "Phone numbers", "Passport numbers", "Credit cards"))
                    .build(),
            BreachData.builder()
                    .name("Yahoo 2014")
                    .date("2014-09-01")
                    .dataClasses(Arrays.asList("Email addresses", "Passwords", "Security questions"))
                    .build(),
            BreachData.builder()
                    .name("Equifax 2017")
                    .date("2017-07-29")
                    .dataClasses(Arrays.asList("Social Security numbers", "Birth dates", "Addresses", "Credit cards"))
                    .build(),
            BreachData.builder()
                    .name("Canva 2019")
                    .date("2019-05-24")
                    .dataClasses(Arrays.asList("Email addresses", "Usernames", "Names", "Cities"))
                    .build(),
            BreachData.builder()
                    .name("MyFitnessPal 2018")
                    .date("2018-02-01")
                    .dataClasses(Arrays.asList("Email addresses", "Passwords", "Usernames"))
                    .build()
    );

    public BreachResponse checkBreaches(String identifierHash) {
        log.info("Checking breaches for hash: {}", identifierHash.substring(0, 8) + "...");

        int hashCode = Math.abs(identifierHash.hashCode());
        Random deterministicRandom = new Random(hashCode);

        int breachCount = deterministicRandom.nextInt(5);

        List<BreachData> affectedBreaches = new ArrayList<>();

        if (breachCount > 0) {
            List<BreachData> availableBreaches = new ArrayList<>(MOCK_BREACHES);

            for (int i = 0; i < breachCount && !availableBreaches.isEmpty(); i++) {
                int index = deterministicRandom.nextInt(availableBreaches.size());
                affectedBreaches.add(availableBreaches.remove(index));
            }
        }

        log.info("Found {} breach(es) for identifier", breachCount);

        return BreachResponse.builder()
                .identifier(identifierHash)
                .breachCount(breachCount)
                .breaches(affectedBreaches)
                .build();
    }
}
