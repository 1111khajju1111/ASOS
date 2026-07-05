package com.asos.demo.dto.auth;

public class AuthResponse {

    private Long founderId;
    private String fullName;
    private String email;
    private String companyName;

    public AuthResponse() {
    }

    public AuthResponse(Long founderId, String fullName, String email, String companyName) {
        this.founderId = founderId;
        this.fullName = fullName;
        this.email = email;
        this.companyName = companyName;
    }

    public Long getFounderId() {
        return founderId;
    }

    public void setFounderId(Long founderId) {
        this.founderId = founderId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
