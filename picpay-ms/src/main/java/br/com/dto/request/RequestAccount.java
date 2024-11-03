package br.com.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestAccount {
    private String name;
    private String cpf;
    private String email;
    private String agencyNumber;
    private String type;
}
