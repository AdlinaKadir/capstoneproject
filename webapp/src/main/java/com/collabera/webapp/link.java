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
public class link {

    @Id
    @GeneratedValue
    private int id;
    private int movieId;

    private int imdbId;
    private int tmdbId;
}
