package com.midas.app.models;

import lombok.*;

@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateAccountRequest {
  private String firstName;
  private String lastName;
  private String email;
}
