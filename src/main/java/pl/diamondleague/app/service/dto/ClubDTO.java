package pl.diamondleague.app.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link pl.diamondleague.app.domain.Club} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ClubDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 3, max = 50)
    private String name;

    private String logoPath;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogoPath() {
        return logoPath;
    }

    public void setLogoPath(String logoPath) {
        this.logoPath = logoPath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ClubDTO)) {
            return false;
        }

        ClubDTO clubDTO = (ClubDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, clubDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClubDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", logoPath='" + getLogoPath() + "'" +
            "}";
    }
}
