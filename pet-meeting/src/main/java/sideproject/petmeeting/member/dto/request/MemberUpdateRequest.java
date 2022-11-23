package sideproject.petmeeting.member.dto.request;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MemberUpdateRequest {
    private String originEmail;
    private String nickname;
    private String password;
    private String email;
    private String image;
}
