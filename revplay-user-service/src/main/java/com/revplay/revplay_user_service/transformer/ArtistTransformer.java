package com.revplay.revplay_user_service.transformer;

import com.revplay.revplay_user_service.dto.request.ArtistProfileRequest;
import com.revplay.revplay_user_service.dto.request.RegisterRequest;
import com.revplay.revplay_user_service.dto.response.ArtistDTO;
import com.revplay.revplay_user_service.dto.response.ArtistResponse;
import com.revplay.revplay_user_service.model.Artist;
import com.revplay.revplay_user_service.model.User;

import java.util.Optional;

public class ArtistTransformer {

    public static ArtistResponse artistToArtistResponse(Artist artist) {
        return ArtistResponse.builder()
                .id(artist.getId())
                .artistName(artist.getArtistName())
                .genre(artist.getGenre())
                .bio(artist.getBio())
                .bannerImage(artist.getBannerImage())
                .profilePicture(artist.getProfilePicture())
                .instagram(artist.getInstagram())
                .spotify(artist.getSpotify())
                .twitter(artist.getTwitter())
                .youtube(artist.getYoutube())
                .website(artist.getWebsite())
                .build();
    }

    public static Artist setArtistProfile(Artist artist, User user, ArtistProfileRequest request) {

        return artist.builder()
                .id(artist.getId())
                .user(user)
                .artistName(request.getArtistName())
                .bio(request.getBio())
                .genre(request.getGenre())
                .bannerImage(artist.getBannerImage())
                .profilePicture(artist.getProfilePicture())
                .instagram(request.getInstagram())
                .twitter(request.getTwitter())
                .youtube(request.getYoutube())
                .spotify(request.getSpotify())
                .website(request.getWebsite())
                .build();
    }

    public static Artist artistRequestToArtist(RegisterRequest artistRequest, User user) {
        return Artist.builder()
                .artistName(artistRequest.getUsername())
                .profilePicture(user.getProfilePicture())
                .user(user)
                .build();
    }

    public static ArtistDTO artistToArtistDTO(Artist artist) {

        return ArtistDTO.builder()
                .id(artist.getId())
                .artistName(artist.getArtistName())
                .bio(artist.getBio())
                .profilePicture(artist.getProfilePicture())
                .genre(String.valueOf(artist.getGenre()))
                .build();
    }
}
