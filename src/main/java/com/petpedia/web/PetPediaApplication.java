package com.petpedia.web;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.petpedia.web.model.Pet;
import com.petpedia.web.model.PetDataRepository;
import org.apache.tomcat.websocket.ReadBufferOverflowException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

@SpringBootApplication
public class PetPediaApplication {

    @Autowired
    private PetDataRepository petDataRepository;

    public static void main(String[] args) {
        SpringApplication.run(PetPediaApplication.class, args);
    }

    private static void recordApiData(PetDataRepository repository, HttpClient client, ObjectMapper mapper, String species, String breedsUrl, String imageUrl, String apiKey) throws IOException, InterruptedException {
        TypeReference<ArrayList<HashMap<String, Object>>> typeRefList = new TypeReference<ArrayList<HashMap<String, Object>>>() {};
        TypeReference<HashMap<String, Object>> typeRefMap = new TypeReference<HashMap<String, Object>>() {};
        HttpRequest catBreedRequest = HttpRequest.newBuilder()
                .uri(URI.create(breedsUrl))
                .header("x-api-key", apiKey)
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> catBreedResponse = client.send(catBreedRequest, HttpResponse.BodyHandlers.ofString());
        ArrayList<HashMap<String, Object>> pets = mapper.readValue(catBreedResponse.body(), typeRefList);

        for (HashMap<String, Object> pet : pets)
        {
            Pet p = new Pet();
            p.setBreed((String) pet.get("name"));
            p.setSpecies(species);
            p.setDescription((String) pet.get("description"));
            if (species.equalsIgnoreCase("cat")) {
                p.setInfoUrl((String) pet.get("wikipedia_url"));
            } else {
                p.setInfoUrl("https://en.wikipedia.org/wiki/" + p.getBreed());
            }

            if (repository.existsByBreed(p.getBreed()))
                continue;

            try {
                HttpRequest imgRequest = HttpRequest.newBuilder()
                        .uri(URI.create(imageUrl + "/" + pet.get("reference_image_id")))
                        .header("x-api-key", apiKey)
                        .method("GET", HttpRequest.BodyPublishers.noBody())
                        .build();
                HttpResponse<String> imgResponse = client.send(imgRequest, HttpResponse.BodyHandlers.ofString());
                HashMap<String, Object> img = mapper.readValue(imgResponse.body(), typeRefMap);
                p.setImgUrl((String) img.get("url"));
            } catch (JsonParseException e) {
                System.out.println("Could not find image of " + pet.get("name"));
                p.setImgUrl("");
            }

            HashMap<String, Object> weights = (HashMap<String, Object>) pet.get("weight");
            HashMap<String, Object> heights = (HashMap<String, Object>) pet.get("height");
            String weight = (String) weights.get("imperial");
            String tableInfo = "{";
            tableInfo += "\"Weight\": \"" + weight + "\", ";
            if (heights != null) {
                String height = (String) heights.get("imperial");
                tableInfo += "\"Height\": \"" + height + "\", ";
            }
            tableInfo += "\"Lifespan\": \"" + (String) pet.get("life_span") + "\", ";
            tableInfo += "\"Temperament\": \"" + (String) pet.get("temperament") + "\", ";
            if (pet.get("bred_for") != null)
                tableInfo += "\"Bred for\": \"" + pet.get("bred_for") + "\", ";
            tableInfo += "\"Origin\": \"" + (String) pet.get("origin") + "\"}";

            p.setTableInfo(tableInfo);
            repository.save(p);
        }
    }

    @Bean
    InitializingBean populateDatabase() {
        return () -> {

            String catBreedsUrl = "https://api.thecatapi.com/v1/breeds";
            String dogBreedsUrl = "https://api.thedogapi.com/v1/breeds";

            String catImageUrl = "https://api.thecatapi.com/v1/images";
            String dogImageUrl = "https://api.thedogapi.com/v1/images";

            String catApiKey = "live_gV2BbWyIAPtYEYqvvtjBmrpOvt5ntQ56pY79wDhD5EDG0MaoBzZantEmIUvp9ir5";
            String dogApiKey = "live_H4Ju1crGTZBGoy56AH0H9pACGoI4adjHB0jkJHvY8uuMWyOOBGpM940qClww1BQx";

            HttpClient client = HttpClient.newHttpClient();
            ObjectMapper mapper = new ObjectMapper();

            recordApiData(this.petDataRepository, client, mapper, "Cat", catBreedsUrl, catImageUrl, catApiKey);
            recordApiData(this.petDataRepository, client, mapper, "Dog", dogBreedsUrl, dogImageUrl, dogApiKey);

            TypeReference<ArrayList<HashMap<String, Object>>> typeRefList = new TypeReference<ArrayList<HashMap<String, Object>>>() {};
            TypeReference<HashMap<String, Object>> typeRefMap = new TypeReference<HashMap<String, Object>>() {};

            ArrayList<HashMap<String, Object>> otherPetInfo = mapper.readValue(new File("src/main/resources/static/table.json"), typeRefList);
            ArrayList<HashMap<String, Object>> otherPetPages = mapper.readValue(new File("src/main/resources/static/pages.json"), typeRefList);
            for (HashMap<String, Object> pet : otherPetInfo) {
                Pet p = new Pet();

                p.setSpecies((String) pet.get("species"));
                p.setBreed((String) pet.get("name"));
                p.setDescription("");
                p.setImgUrl((String) pet.get("img-src"));

                String searchTerm = p.getBreed()
                        .replace(' ', '-')
                        .replaceAll("’", "")
                        .toLowerCase();
                Optional<HashMap<String, Object>> optUrlPath = otherPetPages.stream()
                        .filter(item -> ((String)item.get("url")).contains(searchTerm))
                        .findFirst();
                String urlPath = "";
                if (optUrlPath.isPresent())
                    urlPath = (String) optUrlPath.get().get("url");
                p.setInfoUrl("https://www.petguide.com" + urlPath);
                System.out.println(p.getInfoUrl());

                if (petDataRepository.existsByBreed(p.getBreed()))
                    continue;

                ArrayList<String> usedKeys = new ArrayList<String>();
                usedKeys.add("species");
                usedKeys.add("name");
                usedKeys.add("img-src");

                String tableInfo = "{";

                for (String key : pet.keySet()) {
                    if (!usedKeys.contains(key) && !((String) pet.get(key)).trim().isEmpty()) {
                        tableInfo += "\"" + key + "\": \"" + pet.get(key) + "\", ";
                    }
                }
                tableInfo = tableInfo.substring(0, tableInfo.length() - 2);
                tableInfo += "}";
                p.setTableInfo(tableInfo);
                petDataRepository.save(p);
            }

        };
    }

}
