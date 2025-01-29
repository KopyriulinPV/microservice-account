package microservice_account.microservice_account.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountListResponse {

    private List<AccountResponseDto> News = new ArrayList<>();

}
