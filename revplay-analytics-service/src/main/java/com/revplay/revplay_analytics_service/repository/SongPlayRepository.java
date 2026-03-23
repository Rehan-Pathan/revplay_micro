package com.revplay.revplay_analytics_service.repository;

import com.revplay.revplay_analytics_service.dto.response.PlayTrendResponse;
import com.revplay.revplay_analytics_service.dto.response.TopListenerProjection;
import com.revplay.revplay_analytics_service.dto.response.TopSongProjection;
import com.revplay.revplay_analytics_service.dto.response.TopSongResponse;
import com.revplay.revplay_analytics_service.model.SongPlay;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SongPlayRepository extends JpaRepository<SongPlay, Long> {
    long countByArtistId(Long artistId);

    @Query("""
            SELECT new com.revplay.revplay_analytics_service.dto.response.TopSongProjection(
                    sp.songId,
                    COUNT(sp)
            )
            FROM SongPlay sp
            WHERE sp.artistId = :artistId
            GROUP BY sp.songId
            ORDER BY COUNT(sp) DESC
            """)
    List<TopSongProjection> findTopSongsByArtist(Long artistId);

    @Query("""
            SELECT new com.revplay.revplay_analytics_service.dto.response.TopListenerProjection(
                    sp.userId,
                    COUNT(sp)
            )
            FROM SongPlay sp
            WHERE sp.artistId = :artistId
            GROUP BY sp.userId
            ORDER BY COUNT(sp) DESC
            """)
    List<TopListenerProjection> findTopListenersByArtist(Long artistId);

    @Query("""
                SELECT new com.revplay.revplay_analytics_service.dto.response.TopListenerProjection(
                    sp.userId,
                    COUNT(sp.id)
                )
                FROM SongPlay sp
                WHERE sp.artistId = :artistId
                GROUP BY sp.userId
                ORDER BY COUNT(sp.id) DESC
            """)
    Page<TopListenerProjection> findTopListenersByArtistId(@Param("artistId") Long artistId, Pageable pageable);

    @Query("""
            SELECT new com.revplay.revplay_analytics_service.dto.response.PlayTrendResponse(
                    CAST(DATE(sp.playedAt) AS string),
                    COUNT(sp)
            )
            FROM SongPlay sp
            WHERE sp.artistId = :artistId
            GROUP BY CAST(DATE(sp.playedAt) AS string)
            ORDER BY CAST(DATE(sp.playedAt) AS string)
            """)
    List<PlayTrendResponse> getDailyTrends(@Param("artistId") Long artistId);

    @Query("""
            SELECT new com.revplay.revplay_analytics_service.dto.response.PlayTrendResponse(
                    CONCAT(YEAR(sp.playedAt), '-W', WEEK(sp.playedAt)),
                    COUNT(sp)
            )
            FROM SongPlay sp
            WHERE sp.artistId = :artistId
            GROUP BY CONCAT(YEAR(sp.playedAt), '-W', WEEK(sp.playedAt))
            ORDER BY CONCAT(YEAR(sp.playedAt), '-W', WEEK(sp.playedAt))
            """)
    List<PlayTrendResponse> getWeeklyTrends(@Param("artistId") Long artistId);

    @Query("""
            SELECT new com.revplay.revplay_analytics_service.dto.response.PlayTrendResponse(
                    CONCAT(YEAR(sp.playedAt), '-', MONTH(sp.playedAt)),
                    COUNT(sp)
            )
            FROM SongPlay sp
            WHERE sp.artistId = :artistId
            GROUP BY CONCAT(YEAR(sp.playedAt), '-', MONTH(sp.playedAt))
            ORDER BY CONCAT(YEAR(sp.playedAt), '-', MONTH(sp.playedAt))
            """)
    List<PlayTrendResponse> getMonthlyTrends(@Param("artistId") Long artistId);

    @Query("""
            SELECT COUNT(sp)
            FROM SongPlay sp
            WHERE sp.artistId = :artistId
            """)
    Long getTotalPlays(@Param("artistId") Long artistId);

    @Query("""
            SELECT COUNT(DISTINCT sp.userId)
            FROM SongPlay sp
            WHERE sp.artistId = :artistId
            """)
    Long getTotalListeners(@Param("artistId") Long artistId);
}