package com.gft.reactivetest.security;

import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class User {

    private Long id;
    private String username;
    private String type;
}
