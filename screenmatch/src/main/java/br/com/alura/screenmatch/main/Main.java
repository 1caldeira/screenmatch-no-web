package br.com.alura.screenmatch.main;

import br.com.alura.screenmatch.model.*;
import br.com.alura.screenmatch.service.RequestAPI;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;


public class Main {

    DataConversion conversion = new DataConversion();
    private RequestAPI requestAPI = new RequestAPI();
    private final String ADDRESS = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=ad0f5b1d";
    public void showMenu() {
        System.out.println("Write the name of the series you'd like to search: ");
        Scanner sc = new Scanner(System.in);
        String search = sc.nextLine();

        var json = requestAPI.getData(ADDRESS + search.replace(" ", "+") +"&type=series"+ API_KEY);
        SeriesData seriesData = conversion.getData(json, SeriesData.class);
        System.out.println(seriesData);

        List<SeasonData> seasons = new ArrayList<>();

        for (int seasonNumber = 1; seasonNumber <= seriesData.seasons(); seasonNumber++) {
            json = requestAPI.getData(ADDRESS + search.replace(" ", "+") + "&season=" + seasonNumber +"&type=series"+"&apikey=ad0f5b1d");
            SeasonData seasonData = conversion.getData(json, SeasonData.class);
            seasons.add(seasonData);
        }

//        for (SeasonData sd:seasons) {
//            for (EpisodeData ed: sd.episodes()) {
//                System.out.println("Season: "+sd.number()+": "+ed.title());
//            }
//        }
        seasons.forEach(t -> t.episodes().forEach(e -> System.out.println(e.title())));

        List<EpisodeData> episodeDataList = seasons.stream()
                .flatMap(t -> t.episodes().stream())
                .collect(Collectors.toList());

        System.out.println("\nTop 5 Episodes:");
        episodeDataList.stream()
                .filter(e -> !e.rating().equalsIgnoreCase("N/A"))
                .sorted(Comparator.comparing(EpisodeData::rating).reversed())
                .limit(5)
                .forEach(System.out::println);
        System.out.println("\n");
        List<Episode> episodeList = seasons.stream()
                .flatMap(s -> s.episodes().stream()
                        .map(episodeData -> new Episode(s.number(), episodeData))
                ).collect(Collectors.toList());
        episodeList.forEach(System.out::println);

        System.out.println("From which year onwards do you wish to see the episodes?");
        var ano = sc.nextInt();
        sc.nextLine();
        LocalDate searchDate = LocalDate.of(ano,1,1);
        DateTimeFormatter dt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        episodeList.stream()
                .filter(e -> e.getReleaseDate()!=null && e.getReleaseDate().isAfter(searchDate))
                .forEach(e-> System.out.println(
                        "Season: "+e.getSeason()+
                                " Episode: "+e.getTitle()+
                                " Release Date: "+e.getReleaseDate().format(dt)
                ));
    }
}
