package com.revplay.revplay_analytics_service.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlayTrendResponse {

    private String period;
    private Long playCount;

}
