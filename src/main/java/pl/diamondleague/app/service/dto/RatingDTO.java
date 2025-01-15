package pl.diamondleague.app.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link pl.diamondleague.app.domain.Rating} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RatingDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant date;

    @NotNull
    @Min(value = 1)
    @Max(value = 99)
    private Integer attack;

    @NotNull
    @Min(value = 1)
    @Max(value = 99)
    private Integer defense;

    @NotNull
    @Min(value = 1)
    @Max(value = 99)
    private Integer engagement;

    @NotNull
    @Min(value = 70)
    @Max(value = 99)
    private Integer overall;

    @NotNull
    private PlayerDTO player;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public Integer getAttack() {
        return attack;
    }

    public void setAttack(Integer attack) {
        this.attack = attack;
    }

    public Integer getDefense() {
        return defense;
    }

    public void setDefense(Integer defense) {
        this.defense = defense;
    }

    public Integer getEngagement() {
        return engagement;
    }

    public void setEngagement(Integer engagement) {
        this.engagement = engagement;
    }

    public Integer getOverall() {
        return overall;
    }

    public void setOverall(Integer overall) {
        this.overall = overall;
    }

    public PlayerDTO getPlayer() {
        return player;
    }

    public void setPlayer(PlayerDTO player) {
        this.player = player;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RatingDTO)) {
            return false;
        }

        RatingDTO ratingDTO = (RatingDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, ratingDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RatingDTO{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", attack=" + getAttack() +
            ", defense=" + getDefense() +
            ", engagement=" + getEngagement() +
            ", overall=" + getOverall() +
            ", player=" + getPlayer() +
            "}";
    }
}
