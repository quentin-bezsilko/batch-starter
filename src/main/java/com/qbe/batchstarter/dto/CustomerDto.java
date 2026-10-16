package com.qbe.batchstarter.dto;

import java.time.LocalDate;

public record CustomerDto(Long id, String firstName, String lastName, LocalDate birthDate, String country) {}
