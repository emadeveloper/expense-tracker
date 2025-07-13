package com.EmaDeveloper.ExpenseTracker.exceptions.dto;

import java.time.LocalDateTime;

public record ApiError (LocalDateTime timestamp, int status, String message){}
