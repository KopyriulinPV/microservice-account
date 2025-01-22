package microservice_account.microservice_account.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
/*@OrderFilterValid*/
public class AccountFilter {

    private Integer pageSize;
    private Integer pageNumber;

    // не понятно, что за автор, ждем фронта
    private String author;

    private String ids;
    private String firstName;
    private String lastName;
    private Integer ageFrom;
    private Integer ageTo;

    private String country;
    private String city;

    private String statusCode;

    private Boolean deleted;

}
