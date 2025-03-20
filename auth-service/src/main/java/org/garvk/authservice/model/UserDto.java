package org.garvk.authservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
public class UserDto {
    private Long id;
    private String username;
    private String email;
    private User.Role role;
    private ProfileDto profile;

    public UserDto(User aInUser){
        this.id = aInUser.getId();
        this.username = aInUser.getUsername();
        this.email = aInUser.getEmail();
        this.role = aInUser.getRole();
        this.profile = new ProfileDto(aInUser.getProfile());
    }

}
