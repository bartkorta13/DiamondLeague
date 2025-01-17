package pl.diamondleague.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * A PlayerGame.
 */
@Entity
@Table(name = "player_game")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PlayerGame implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Min(value = 0)
    @Max(value = 100)
    @Column(name = "goals", nullable = false)
    private Integer goals;

    @NotNull
    @Min(value = 0)
    @Max(value = 100)
    @Column(name = "assists", nullable = false)
    private Integer assists;

    @Column(name = "attack_score")
    private Double attackScore;

    @Column(name = "defense_score")
    private Double defenseScore;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "appUser", "favouriteClub", "ratings", "games", "teams" }, allowSetters = true)
    private Player player;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "captain", "game", "playerGames" }, allowSetters = true)
    private GameTeam gameTeam;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PlayerGame id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getGoals() {
        return this.goals;
    }

    public PlayerGame goals(Integer goals) {
        this.setGoals(goals);
        return this;
    }

    public void setGoals(Integer goals) {
        this.goals = goals;
    }

    public Integer getAssists() {
        return this.assists;
    }

    public PlayerGame assists(Integer assists) {
        this.setAssists(assists);
        return this;
    }

    public void setAssists(Integer assists) {
        this.assists = assists;
    }

    public Double getAttackScore() {
        return this.attackScore;
    }

    public PlayerGame attackScore(Double attackScore) {
        this.setAttackScore(attackScore);
        return this;
    }

    public void setAttackScore(Double attackScore) {
        this.attackScore = attackScore;
    }

    public Double getDefenseScore() {
        return this.defenseScore;
    }

    public PlayerGame defenseScore(Double defenseScore) {
        this.setDefenseScore(defenseScore);
        return this;
    }

    public void setDefenseScore(Double defenseScore) {
        this.defenseScore = defenseScore;
    }

    public Player getPlayer() {
        return this.player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public PlayerGame player(Player player) {
        this.setPlayer(player);
        return this;
    }

    public GameTeam getGameTeam() {
        return this.gameTeam;
    }

    public void setGameTeam(GameTeam gameTeam) {
        this.gameTeam = gameTeam;
    }

    public PlayerGame gameTeam(GameTeam gameTeam) {
        this.setGameTeam(gameTeam);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PlayerGame)) {
            return false;
        }
        return getId() != null && getId().equals(((PlayerGame) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PlayerGame{" +
            "id=" + getId() +
            ", goals=" + getGoals() +
            ", assists=" + getAssists() +
            ", attackScore=" + getAttackScore() +
            ", defenseScore=" + getDefenseScore() +
            "}";
    }
}
