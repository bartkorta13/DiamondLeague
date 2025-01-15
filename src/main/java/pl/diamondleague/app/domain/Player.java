package pl.diamondleague.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import pl.diamondleague.app.domain.enumeration.Position;

/**
 * A Player.
 */
@Entity
@Table(name = "player")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Player implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 2, max = 40)
    @Column(name = "first_name", length = 40, nullable = false)
    private String firstName;

    @NotNull
    @Size(min = 2, max = 40)
    @Column(name = "last_name", length = 40, nullable = false)
    private String lastName;

    @NotNull
    @Size(min = 3, max = 40)
    @Column(name = "nickname", length = 40, nullable = false, unique = true)
    private String nickname;

    @Min(value = 100)
    @Max(value = 250)
    @Column(name = "height")
    private Integer height;

    @Min(value = 1900)
    @Max(value = 2050)
    @Column(name = "year_of_birth")
    private Integer yearOfBirth;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "preferred_position", nullable = false)
    private Position preferredPosition;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "player")
    @JsonIgnoreProperties(value = { "player" }, allowSetters = true)
    private Set<Rating> ratings = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "player")
    @JsonIgnoreProperties(value = { "player", "gameTeam" }, allowSetters = true)
    private Set<PlayerGame> playerGames = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "captain")
    @JsonIgnoreProperties(value = { "playerGames", "captain", "game" }, allowSetters = true)
    private Set<GameTeam> gameTeams = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "players" }, allowSetters = true)
    private Club favouriteClub;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Player id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public Player firstName(String firstName) {
        this.setFirstName(firstName);
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public Player lastName(String lastName) {
        this.setLastName(lastName);
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getNickname() {
        return this.nickname;
    }

    public Player nickname(String nickname) {
        this.setNickname(nickname);
        return this;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Integer getHeight() {
        return this.height;
    }

    public Player height(Integer height) {
        this.setHeight(height);
        return this;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getYearOfBirth() {
        return this.yearOfBirth;
    }

    public Player yearOfBirth(Integer yearOfBirth) {
        this.setYearOfBirth(yearOfBirth);
        return this;
    }

    public void setYearOfBirth(Integer yearOfBirth) {
        this.yearOfBirth = yearOfBirth;
    }

    public Position getPreferredPosition() {
        return this.preferredPosition;
    }

    public Player preferredPosition(Position preferredPosition) {
        this.setPreferredPosition(preferredPosition);
        return this;
    }

    public void setPreferredPosition(Position preferredPosition) {
        this.preferredPosition = preferredPosition;
    }

    public Set<Rating> getRatings() {
        return this.ratings;
    }

    public void setRatings(Set<Rating> ratings) {
        if (this.ratings != null) {
            this.ratings.forEach(i -> i.setPlayer(null));
        }
        if (ratings != null) {
            ratings.forEach(i -> i.setPlayer(this));
        }
        this.ratings = ratings;
    }

    public Player ratings(Set<Rating> ratings) {
        this.setRatings(ratings);
        return this;
    }

    public Player addRating(Rating rating) {
        this.ratings.add(rating);
        rating.setPlayer(this);
        return this;
    }

    public Player removeRating(Rating rating) {
        this.ratings.remove(rating);
        rating.setPlayer(null);
        return this;
    }

    public Set<PlayerGame> getPlayerGames() {
        return this.playerGames;
    }

    public void setPlayerGames(Set<PlayerGame> playerGames) {
        if (this.playerGames != null) {
            this.playerGames.forEach(i -> i.setPlayer(null));
        }
        if (playerGames != null) {
            playerGames.forEach(i -> i.setPlayer(this));
        }
        this.playerGames = playerGames;
    }

    public Player playerGames(Set<PlayerGame> playerGames) {
        this.setPlayerGames(playerGames);
        return this;
    }

    public Player addPlayerGame(PlayerGame playerGame) {
        this.playerGames.add(playerGame);
        playerGame.setPlayer(this);
        return this;
    }

    public Player removePlayerGame(PlayerGame playerGame) {
        this.playerGames.remove(playerGame);
        playerGame.setPlayer(null);
        return this;
    }

    public Set<GameTeam> getGameTeams() {
        return this.gameTeams;
    }

    public void setGameTeams(Set<GameTeam> gameTeams) {
        if (this.gameTeams != null) {
            this.gameTeams.forEach(i -> i.setCaptain(null));
        }
        if (gameTeams != null) {
            gameTeams.forEach(i -> i.setCaptain(this));
        }
        this.gameTeams = gameTeams;
    }

    public Player gameTeams(Set<GameTeam> gameTeams) {
        this.setGameTeams(gameTeams);
        return this;
    }

    public Player addGameTeam(GameTeam gameTeam) {
        this.gameTeams.add(gameTeam);
        gameTeam.setCaptain(this);
        return this;
    }

    public Player removeGameTeam(GameTeam gameTeam) {
        this.gameTeams.remove(gameTeam);
        gameTeam.setCaptain(null);
        return this;
    }

    public Club getFavouriteClub() {
        return this.favouriteClub;
    }

    public void setFavouriteClub(Club club) {
        this.favouriteClub = club;
    }

    public Player favouriteClub(Club club) {
        this.setFavouriteClub(club);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Player)) {
            return false;
        }
        return getId() != null && getId().equals(((Player) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Player{" +
            "id=" + getId() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", nickname='" + getNickname() + "'" +
            ", height=" + getHeight() +
            ", yearOfBirth=" + getYearOfBirth() +
            ", preferredPosition='" + getPreferredPosition() + "'" +
            "}";
    }
}
