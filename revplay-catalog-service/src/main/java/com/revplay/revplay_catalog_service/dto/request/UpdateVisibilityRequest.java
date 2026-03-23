package com.revplay.revplay_catalog_service.dto.request;


import com.revplay.revplay_catalog_service.Enum.Visibility;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateVisibilityRequest {

    private Visibility visibility;

}