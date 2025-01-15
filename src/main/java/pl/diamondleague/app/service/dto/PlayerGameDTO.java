package pl.diamondleague.app.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link pl.diamondleague.app.domain.PlayerGame} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PlayerGameDTO implements Serializable {

    private Long id;

    @NotNull
    @Min(value = 0)
    @Max(value = 100)
    private Integer goals;

    @NotNull
    @Min(value = 0)
    @Max(value = 100)
    private Integer assists;

    private Double attackScore;

    private Double defenseScore;

    @NotNull
    private PlayerDTO player;

    @NotNull
    private GameTeamDTO gameTeam;

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

    public Integer getAssists() {
        return assists;
    }

    public void setAssists(Integer assists) {
        this.assists = assists;
    }

    public Double getAttackScore() {
        return attackScore;
    }

    public void setAttackScore(Double attackScore) {
        this.attackScore = attackScore;
    }

    public Double getDefenseScore() {
        return defenseScore;
    }

    public void setDefenseScore(Double defenseScore) {
        this.defenseScore = defenseScore;
    }

    public PlayerDTO getPlayer() {
        return player;
    }

    public void setPlayer(PlayerDTO player) {
        this.player = player;
    }

    public GameTeamDTO getGameTeam() {
        return gameTeam;
    }

    public void setGameTeam(GameTeamDTO gameTeam) {
        this.gameTeam = gameTeam;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PlayerGameDTO)) {
            return false;
        }

        PlayerGameDTO playerGameDTO = (PlayerGameDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, playerGameDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PlayerGameDTO{" +
            "id=" + getId() +
            ", goals=" + getGoals() +
            ", assists=" + getAssists() +
            ", attackScore=" + getAttackScore() +
            ", defenseScore=" + getDefenseScore() +
            ", player=" + getPlayer() +
            ", gameTeam=" + getGameTeam() +
            "}";
    }
}
