package com.example.util;

import org.springframework.stereotype.Component;

@Component
public class UrlBuilder {
    // TODO build by secured string
    public String editAccountUrl(int id) {
	return "/admin/accounts/" + id + "/edit";
    }

    public String updateAccountUrl(int id) {
	return "/admin/accounts/" + id + "/update";
    }

}
