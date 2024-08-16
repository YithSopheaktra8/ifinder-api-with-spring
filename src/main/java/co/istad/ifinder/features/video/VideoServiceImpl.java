package co.istad.ifinder.features.video;

import co.istad.ifinder.domain.Video;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.VideoListResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.typesense.api.Client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class VideoServiceImpl implements VideoService {

    private final YouTube youtube;
    private final Client client;
    private final Map<Integer, String> nextPageTokenMap = new HashMap<>();
    @Value("${YOUTUBE_API_KEY}")
    private String apiKey;

    /**
     * @param page
     * @param size
     * @param search
     * @return Page<Video>
     * @throws IOException This method searches for videos on YouTube
     */
    public Page<Video> searchVideo(int page, int size, String search) throws IOException {

        List<Video> videos = new ArrayList<>();
        String pageToken = page > 1 ? nextPageTokenMap.get(page - 1) : null;
        long totalResults = 0;
        String youtubeLink = "https://www.youtube.com/watch?v=";

        // Loop to fetch results until the desired page size is meet
        do {
            // Create the search request
            YouTube.Search.List searchList = youtube.search().list("snippet");
            searchList.setKey(apiKey);
            searchList.setQ(search);
            searchList.setType("video");
            searchList.setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url),nextPageToken,pageInfo");
            searchList.setMaxResults((long) size);
            searchList.setOrder("relevance");
            searchList.setPageToken(pageToken);

            // Execute the search request
            SearchListResponse searchListResponse = searchList.execute();
            pageToken = searchListResponse.getNextPageToken();

            // Process the search results
            for (SearchResult searchResult : searchListResponse.getItems()) {
                if (videos.size() >= size) break;

                Video video = new Video();
                YouTube.Videos.List videoRequest = youtube.videos().list("snippet,statistics,contentDetails");
                videoRequest.setKey(apiKey);
                videoRequest.setId(searchResult.getId().getVideoId());

                VideoListResponse videoListResponse = videoRequest.execute();
                com.google.api.services.youtube.model.Video video1 = videoListResponse.getItems().get(0);

                video.setVideoId(video1.getId());
                video.setTitle(video1.getSnippet().getTitle());
                video.setThumbnail(video1.getSnippet().getThumbnails().getDefault().getUrl());
                video.setPublishedAt(video1.getSnippet().getPublishedAt().toString());
                video.setChanel(video1.getSnippet().getChannelTitle());
                video.setViewCount(video1.getStatistics().getViewCount().toString());
                video.setDescription(video1.getSnippet().getDescription());
                video.setDuration(video1.getContentDetails().getDuration());
                video.setYoutubeLink(youtubeLink + video1.getId());
                videos.add(video);
            }

            totalResults = searchListResponse.getPageInfo() != null ? searchListResponse.getPageInfo().getTotalResults() : totalResults;

        } while (videos.size() < size && pageToken != null);

        // Store the nextPageToken for the next page
        nextPageTokenMap.put(page, pageToken);

        return new PageImpl<>(videos, PageRequest.of(page, size), totalResults);
    }

}
