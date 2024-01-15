package project.booker.controller.FollowController.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FollowCount {

    private Long followerCount;
    private Long followingCount;

}
