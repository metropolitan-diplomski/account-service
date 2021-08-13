package com.met.account.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class UserResponse {

    private String id;
    private Integer index;
    private String username;
    private String email;
    private String fullName;
    private String address;
    private String jmbg;
    @JsonFormat(
            pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS"
    )
    @JsonDeserialize(
            using = LocalDateTimeDeserializer.class
    )
    private LocalDateTime createdDate;
    private List<String> roles = new ArrayList<>();
}
