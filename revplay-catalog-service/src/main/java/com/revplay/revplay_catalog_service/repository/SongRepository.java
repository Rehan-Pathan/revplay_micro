package com.revplay.revplay_catalog_service.repository;

import com.revplay.revplay_catalog_service.Enum.Genre;
import com.revplay.revplay_catalog_service.Enum.Visibility;
import com.revplay.revplay_catalog_service.model.Song;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {
    List<Song> findByArtistId(Long artistId);

    List<Song> findByAlbumId(Long albumId);

    Optional<Song> findByIdAndAlbumIdAndArtistId(Long id, Long albumId, Long artistId);

    List<Song> findByGenreAndVisibility(Genre genre, Visibility visibility);

    Page<Song> findByGenreAndVisibility(Genre genre, Visibility visibility, Pageable pageable);

    @Query("""
            SELECT s FROM Song s
            WHERE LOWER(s.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
            AND s.visibility = 'PUBLIC'
            """)
    List<Song> searchSongs(String keyword);

    List<Song> findByAlbumIdAndVisibility(Long albumId, Visibility visibility);

    List<Song> findByArtistIdAndVisibility(Long artistId, Visibility visibility);

    List<Song> findTop10ByVisibilityOrderByPlayCountDesc(Visibility visibility);

    long countByArtistId(Long artistId);

    Page<Song> findByVisibility(Visibility visibility, Pageable pageable);

    @Query("""
            SELECT s FROM Song s
            WHERE s.visibility = :visibility
            AND LOWER(s.title) LIKE LOWER(CONCAT('%', :q, '%'))
            """)
    Page<Song> searchPublicSongs(
            String q,
            Visibility visibility,
            Pageable pageable
    );

    @Query("""
            SELECT s FROM Song s
            WHERE (:genre IS NULL OR s.genre = :genre)
            AND (:artistId IS NULL OR s.artistId = :artistId)
            AND (:year IS NULL OR YEAR(s.releaseDate) = :year)
            AND s.visibility = 'PUBLIC'
            """)
    List<Song> filterSongs(
            Genre genre,
            Long artistId,
            Integer year
    );

}
