package org.garvk.authservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class ProfileDto {
    private String firstName;
    private String lastName;
    private String bio;
    private String portfolioUrl;
    private String avatarUrl;

    public ProfileDto(Profile aInProfile){
        this.firstName = aInProfile.getFirstName();
        this.lastName = aInProfile.getLastName();
        this.bio = aInProfile.getBio();
        this.portfolioUrl = aInProfile.getPortfolioUrl();
        this.avatarUrl = aInProfile.getAvatarUrl();
    }

}
