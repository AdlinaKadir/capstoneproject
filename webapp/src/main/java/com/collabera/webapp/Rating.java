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
public class Rating {
    @Id
    @GeneratedValue
    private int Id;
    private int userId;
    private int movieId;

    private double movierating;

    private String timestamp;
}
