package pl.diamondleague.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A Game.
 */
@Entity
@Table(name = "game")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Game implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "date", nullable = false)
    private Instant date;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "game")
    @JsonIgnoreProperties(value = { "playerGames", "captain", "game" }, allowSetters = true)
    private Set<GameTeam> gameTeams = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "games" }, allowSetters = true)
    private Stadium stadium;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Game id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDate() {
        return this.date;
    }

    public Game date(Instant date) {
        this.setDate(date);
        return this;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public Set<GameTeam> getGameTeams() {
        return this.gameTeams;
    }

    public void setGameTeams(Set<GameTeam> gameTeams) {
        if (this.gameTeams != null) {
            this.gameTeams.forEach(i -> i.setGame(null));
        }
        if (gameTeams != null) {
            gameTeams.forEach(i -> i.setGame(this));
        }
        this.gameTeams = gameTeams;
    }

    public Game gameTeams(Set<GameTeam> gameTeams) {
        this.setGameTeams(gameTeams);
        return this;
    }

    public Game addGameTeam(GameTeam gameTeam) {
        this.gameTeams.add(gameTeam);
        gameTeam.setGame(this);
        return this;
    }

    public Game removeGameTeam(GameTeam gameTeam) {
        this.gameTeams.remove(gameTeam);
        gameTeam.setGame(null);
        return this;
    }

    public Stadium getStadium() {
        return this.stadium;
    }

    public void setStadium(Stadium stadium) {
        this.stadium = stadium;
    }

    public Game stadium(Stadium stadium) {
        this.setStadium(stadium);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Game)) {
            return false;
        }
        return getId() != null && getId().equals(((Game) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Game{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            "}";
    }
}
