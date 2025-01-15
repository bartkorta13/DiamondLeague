package pl.diamondleague.app.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link pl.diamondleague.app.domain.Stadium} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class StadiumDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 2, max = 50)
    private String name;

    private String imagePath;

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

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StadiumDTO)) {
            return false;
        }

        StadiumDTO stadiumDTO = (StadiumDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, stadiumDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StadiumDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", imagePath='" + getImagePath() + "'" +
            "}";
    }
}
