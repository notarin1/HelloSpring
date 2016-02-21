package com.example.domain.entity;

import java.io.Serializable;

import lombok.Data;

/**
 * Accountテーブルのエンティティ
 * @author notar_000
 *
 */
@Data
public class Account implements Serializable {
	private static final long serialVersionUID = 1L;

	private int id;
	private String name;
	private String password;
	private String role;
}
