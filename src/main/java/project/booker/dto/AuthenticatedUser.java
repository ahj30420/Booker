package project.booker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticatedUser {

    private String profileId;
    private String name;
    private String nickname;

}
