package com.revplay.revplay_catalog_service.service;

import com.revplay.revplay_catalog_service.Enum.Genre;
import com.revplay.revplay_catalog_service.Enum.Visibility;
import com.revplay.revplay_catalog_service.dto.response.ArtistCatalogDTO;
import com.revplay.revplay_catalog_service.dto.response.HomeResponse;
import com.revplay.revplay_catalog_service.dto.response.SongResponseDTO;
import com.revplay.revplay_catalog_service.model.Album;
import com.revplay.revplay_catalog_service.model.Song;
import com.revplay.revplay_catalog_service.repository.AlbumRepository;
import com.revplay.revplay_catalog_service.repository.SongRepository;
import com.revplay.revplay_catalog_service.transformer.SongTransformer;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CatalogService {

    private final AlbumRepository albumRepository;

    private final SongRepository songRepository;

    private final SongTransformer songTransformer;

    public ArtistCatalogDTO getArtistCatalog(Long artistId) {

        List<Album> albums = albumRepository.findByArtistId(artistId);

        List<Song> songs = songRepository.findByArtistIdAndVisibility(
                artistId,
                Visibility.PUBLIC
        );

        return new ArtistCatalogDTO(artistId, albums, songs);
    }

    public HomeResponse getHome() {

        List<Song> trendingSongs =
                songRepository.findTop10ByVisibilityOrderByPlayCountDesc(Visibility.PUBLIC);

        List<Album> newReleases =
                albumRepository.findTop10ByOrderByReleaseDateDesc();

        List<Album> topAlbums =
                albumRepository.findTop10ByOrderByCreatedAtDesc();

        List<String> genres =
                Arrays.stream(Genre.values())
                        .map(Enum::name)
                        .toList();

        return new HomeResponse(trendingSongs, newReleases, topAlbums, genres);
    }

    public List<Song> findAllById(List<Long> ids) {
        return songRepository.findAllById(ids);
    }

    public Page<SongResponseDTO> browseAllSongs(int page, int size, String sortBy, String direction) {

        Sort sort = direction.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return songRepository.findByVisibility(Visibility.PUBLIC, pageable)
                .map(songTransformer::songToSongResponseDTO);
    }
}
