package pl.diamondleague.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A GameTeam.
 */
@Entity
@Table(name = "game_team")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class GameTeam implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Min(value = 0)
    @Max(value = 100)
    @Column(name = "goals")
    private Integer goals;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "gameTeam")
    @JsonIgnoreProperties(value = { "player", "gameTeam" }, allowSetters = true)
    private Set<PlayerGame> playerGames = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "appUser", "ratings", "playerGames", "gameTeams", "favouriteClub" }, allowSetters = true)
    private Player captain;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "gameTeams", "stadium" }, allowSetters = true)
    private Game game;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public GameTeam id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getGoals() {
        return this.goals;
    }

    public GameTeam goals(Integer goals) {
        this.setGoals(goals);
        return this;
    }

    public void setGoals(Integer goals) {
        this.goals = goals;
    }

    public Set<PlayerGame> getPlayerGames() {
        return this.playerGames;
    }

    public void setPlayerGames(Set<PlayerGame> playerGames) {
        if (this.playerGames != null) {
            this.playerGames.forEach(i -> i.setGameTeam(null));
        }
        if (playerGames != null) {
            playerGames.forEach(i -> i.setGameTeam(this));
        }
        this.playerGames = playerGames;
    }

    public GameTeam playerGames(Set<PlayerGame> playerGames) {
        this.setPlayerGames(playerGames);
        return this;
    }

    public GameTeam addPlayerGame(PlayerGame playerGame) {
        this.playerGames.add(playerGame);
        playerGame.setGameTeam(this);
        return this;
    }

    public GameTeam removePlayerGame(PlayerGame playerGame) {
        this.playerGames.remove(playerGame);
        playerGame.setGameTeam(null);
        return this;
    }

    public Player getCaptain() {
        return this.captain;
    }

    public void setCaptain(Player player) {
        this.captain = player;
    }

    public GameTeam captain(Player player) {
        this.setCaptain(player);
        return this;
    }

    public Game getGame() {
        return this.game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public GameTeam game(Game game) {
        this.setGame(game);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GameTeam)) {
            return false;
        }
        return getId() != null && getId().equals(((GameTeam) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GameTeam{" +
            "id=" + getId() +
            ", goals=" + getGoals() +
            "}";
    }
}
