package com.example.domain.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Accountテーブルのエンティティ
 * 
 * @author notar_000
 *
 */
@Data
@Builder
@NoArgsConstructor // これがないと怒られる！
@AllArgsConstructor
public class Account implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String name;
    private String password;
    private String role;
    private String token;

    public Account regularOf() {
	return builder().name(name).password(password).role(role).token("").build();
    }
}
