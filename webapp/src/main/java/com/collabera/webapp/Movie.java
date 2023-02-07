package com.collabera.webapp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Movie {
    @Id
    @GeneratedValue
    private int movieId;
    private String title;
    private String genres;
    private String description;
    private int tmdbId;

    private String poster;

//    @Lob
//    @Column(columnDefinition="BLOB")
//    private byte[] poster;
//
//    private String posterImageString;

}
