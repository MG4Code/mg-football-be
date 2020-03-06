package com.mg.backend.payload.response;

public class MessageResponse {
  private String message;

  public MessageResponse(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  public MessageResponse setMessage(String message) {
    this.message = message;
    return this;
  }
}
