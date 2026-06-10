package com.simbirsoft.helpers.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.openqa.selenium.Cookie;

import java.util.Date;

/**
 * CookieDto.java
 * <p>
 * DTO для сериализации/десериализации Cookie через Jackson
 * <p>
 * Copyright (c) 2026 Way2Automation. All Rights Reserved.
 * <p>
 * Author: Sheimukhov Andrei
 * <p>
 * Date: 09.06.2026
 */
public class CookieDto {
    private String name;

    private String value;

    private String path;

    private String domain;

    private Date expiry;

    private boolean secure;

    private boolean httpOnly;

    private String sameSite;

    @JsonProperty("name")
    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    @JsonProperty("value")
    public String getValue() { return value; }

    public void setValue(String value) { this.value = value; }

    @JsonProperty("path")
    public String getPath() { return path; }

    public void setPath(String path) { this.path = path; }

    @JsonProperty("domain")
    public String getDomain() { return domain; }

    public void setDomain(String domain) { this.domain = domain; }

    @JsonProperty("expiry")
    public Date getExpiry() { return expiry; }

    public void setExpiry(Date expiry) { this.expiry = expiry; }

    @JsonProperty("secure")
    public boolean isSecure() { return secure; }

    public void setSecure(boolean secure) { this.secure = secure; }

    @JsonProperty("httpOnly")
    public boolean isHttpOnly() { return httpOnly; }

    public void setHttpOnly(boolean httpOnly) { this.httpOnly = httpOnly; }

    @JsonProperty("sameSite")
    public String getSameSite() { return sameSite; }

    public void setSameSite(String sameSite) { this.sameSite = sameSite; }

    /**
     * Конвертирует DTO в Selenium Cookie
     */
    public Cookie toSeleniumCookie() {
        Cookie.Builder builder = new Cookie.Builder(name, value)
                .path(path != null ? path : "/")
                .domain(domain)
                .isSecure(secure)
                .isHttpOnly(httpOnly);

        if (expiry != null) {
            builder.expiresOn(expiry);
        }

        if (sameSite != null) {
            builder.sameSite(sameSite);
        }

        return builder.build();
    }

    /**
     * Создаёт DTO из Selenium Cookie
     */
    public static CookieDto fromSeleniumCookie(Cookie cookie) {
        CookieDto dto = new CookieDto();

        dto.setName(cookie.getName());
        dto.setValue(cookie.getValue());
        dto.setPath(cookie.getPath());
        dto.setDomain(cookie.getDomain());
        dto.setExpiry(cookie.getExpiry());
        dto.setSecure(cookie.isSecure());
        dto.setHttpOnly(cookie.isHttpOnly());
        dto.setSameSite(cookie.getSameSite());

        return dto;
    }
}
