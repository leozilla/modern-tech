package com.leonhart.borred.dto;

public class CreateUserRequest {
   private String name;

   public CreateUserRequest() {
   }

   public CreateUserRequest(final String name) {
      this.name = name;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }
}
