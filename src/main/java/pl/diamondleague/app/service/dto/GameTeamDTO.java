package pl.diamondleague.app.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link pl.diamondleague.app.domain.GameTeam} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class GameTeamDTO implements Serializable {

    private Long id;

    @Min(value = 0)
    @Max(value = 100)
    private Integer goals;

    @NotNull
    private PlayerDTO captain;

    @NotNull
    private GameDTO game;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getGoals() {
        return goals;
    }

    public void setGoals(Integer goals) {
        this.goals = goals;
    }

    public PlayerDTO getCaptain() {
        return captain;
    }

    public void setCaptain(PlayerDTO captain) {
        this.captain = captain;
    }

    public GameDTO getGame() {
        return game;
    }

    public void setGame(GameDTO game) {
        this.game = game;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GameTeamDTO)) {
            return false;
        }

        GameTeamDTO gameTeamDTO = (GameTeamDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, gameTeamDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GameTeamDTO{" +
            "id=" + getId() +
            ", goals=" + getGoals() +
            ", captain=" + getCaptain() +
            ", game=" + getGame() +
            "}";
    }
}
