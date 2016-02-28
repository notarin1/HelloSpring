package com.example.domain.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor // これがないと怒られる！
@AllArgsConstructor
public class AuthorizationToken implements Serializable {
    private static final long serialVersionUID = 1L;
    private String email;
    private LocalDateTime from;
}
