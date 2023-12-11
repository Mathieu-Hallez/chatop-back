package com.chatop.chatopback.payload.rental;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.web.multipart.MultipartFile;

@Data
@EqualsAndHashCode(callSuper = false)
public class CreateRentalDto extends UpdateRentalDto{
    private MultipartFile picture;
}
