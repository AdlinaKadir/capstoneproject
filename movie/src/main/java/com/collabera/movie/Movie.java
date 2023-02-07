package com.collabera.movie;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "movie")
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
