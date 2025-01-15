package pl.diamondleague.app.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import pl.diamondleague.app.domain.enumeration.Position;

/**
 * A DTO for the {@link pl.diamondleague.app.domain.Player} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PlayerDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 2, max = 40)
    private String firstName;

    @NotNull
    @Size(min = 2, max = 40)
    private String lastName;

    @NotNull
    @Size(min = 3, max = 40)
    private String nickname;

    @Min(value = 100)
    @Max(value = 250)
    private Integer height;

    @Min(value = 1900)
    @Max(value = 2050)
    private Integer yearOfBirth;

    @NotNull
    private Position preferredPosition;

    private UserDTO appUser;

    private ClubDTO favouriteClub;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getYearOfBirth() {
        return yearOfBirth;
    }

    public void setYearOfBirth(Integer yearOfBirth) {
        this.yearOfBirth = yearOfBirth;
    }

    public Position getPreferredPosition() {
        return preferredPosition;
    }

    public void setPreferredPosition(Position preferredPosition) {
        this.preferredPosition = preferredPosition;
    }

    public UserDTO getAppUser() {
        return appUser;
    }

    public void setAppUser(UserDTO appUser) {
        this.appUser = appUser;
    }

    public ClubDTO getFavouriteClub() {
        return favouriteClub;
    }

    public void setFavouriteClub(ClubDTO favouriteClub) {
        this.favouriteClub = favouriteClub;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PlayerDTO)) {
            return false;
        }

        PlayerDTO playerDTO = (PlayerDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, playerDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PlayerDTO{" +
            "id=" + getId() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", nickname='" + getNickname() + "'" +
            ", height=" + getHeight() +
            ", yearOfBirth=" + getYearOfBirth() +
            ", preferredPosition='" + getPreferredPosition() + "'" +
            ", appUser=" + getAppUser() +
            ", favouriteClub=" + getFavouriteClub() +
            "}";
    }
}
