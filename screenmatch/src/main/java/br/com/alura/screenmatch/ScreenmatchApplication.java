package br.com.alura.screenmatch;

import br.com.alura.screenmatch.model.DataConversion;
import br.com.alura.screenmatch.model.EpisodeData;
import br.com.alura.screenmatch.model.SeriesData;
import br.com.alura.screenmatch.service.RequestAPI;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		var requestAPI = new RequestAPI();
		var json = requestAPI.getData("https://www.omdbapi.com/?t=house+of+the+dragon&apikey=ad0f5b1d");

		DataConversion conversion = new DataConversion();
		SeriesData data = conversion.getData(json, SeriesData.class);
		System.out.println(data);
		json = requestAPI.getData("https://www.omdbapi.com/?t=house+of+the+dragon&season=1&episode=1&apikey=ad0f5b1d");
		EpisodeData episodeData = conversion.getData(json, EpisodeData.class);
		System.out.println(episodeData);


	}
}
