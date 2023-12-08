package com.chatop.chatopback.payload.rental;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Data
public class RentalDto extends CreateRentalDto {

    private Long id;
    @JsonAlias("owner_id")
    @JsonProperty("owner_id")
    private Long ownerId;
    @JsonAlias("created_at")
    @JsonProperty("created_at")
    private String createdAt;
    @JsonAlias("updated_at")
    @JsonProperty("updated_at")
    private String updatedAt;

    public void setCreatedAt(String date) {
        String PATTERN_FORMAT = "yyyy/MM/dd";
        this.createdAt = DateTimeFormatter.ofPattern(PATTERN_FORMAT).withZone( ZoneId.systemDefault() ).format(Timestamp.valueOf(date).toLocalDateTime());
    }

    public void setUpdatedAt(String date) {
        String PATTERN_FORMAT = "yyyy/MM/dd";
        this.updatedAt = DateTimeFormatter.ofPattern(PATTERN_FORMAT).withZone( ZoneId.systemDefault() ).format(Timestamp.valueOf(date).toLocalDateTime());
    }

}
