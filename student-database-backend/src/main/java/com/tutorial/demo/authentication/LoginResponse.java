package com.tutorial.demo.authentication;

public record LoginResponse(String jwtToken, String role, String id) {}
