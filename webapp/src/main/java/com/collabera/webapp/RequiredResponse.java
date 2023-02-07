package com.collabera.webapp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RequiredResponse {

    private User user;

    private link link;

    private Movie movie;
}
