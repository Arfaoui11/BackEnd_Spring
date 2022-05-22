package com.spring.pidev.sentimentAnalyzer;


public class ResourceNotFoundException extends RuntimeException {

  public ResourceNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}
