package com.midas.app.models;

import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.*;

@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class AccountDto {

  private UUID id;
  private String firstName;
  private String lastName;
  private String email;
  private String providerId;
  private ProviderType providerType;
  private OffsetDateTime createdAt;
  private OffsetDateTime updatedAt;
}
