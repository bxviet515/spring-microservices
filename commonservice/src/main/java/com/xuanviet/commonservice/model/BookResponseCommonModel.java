package com.xuanviet.commonservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookResponseCommonModel {
    private String id;

    private String name;
    private String author;

    private Boolean isReady;
}