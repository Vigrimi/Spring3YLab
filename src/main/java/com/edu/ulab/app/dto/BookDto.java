package com.edu.ulab.app.dto;

import lombok.Data;

@Data
public class BookDto {
    private Integer id;
    private int userId;
    private String title;
    private String author;
    private long pageCount;
}
