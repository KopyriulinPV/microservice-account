package microservice_account.microservice_account.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountListResponse {

    private List<AccountResponseDto> News = new ArrayList<>();

}
