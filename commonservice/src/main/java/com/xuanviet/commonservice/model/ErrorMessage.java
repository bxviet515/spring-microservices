package com.xuanviet.commonservice.model;

import lombok.*;
import org.springframework.http.HttpStatus;
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorMessage {
    private String code;
    private String message;
    private HttpStatus status;
}
