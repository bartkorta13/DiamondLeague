package pl.diamondleague.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Club.
 */
@Entity
@Table(name = "club")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Club implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 3, max = 50)
    @Column(name = "name", length = 50, nullable = false, unique = true)
    private String name;

    @Column(name = "logo_path")
    private String logoPath;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "favouriteClub")
    @JsonIgnoreProperties(value = { "ratings", "playerGames", "gameTeams", "favouriteClub" }, allowSetters = true)
    private Set<Player> players = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Club id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Club name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogoPath() {
        return this.logoPath;
    }

    public Club logoPath(String logoPath) {
        this.setLogoPath(logoPath);
        return this;
    }

    public void setLogoPath(String logoPath) {
        this.logoPath = logoPath;
    }

    public Set<Player> getPlayers() {
        return this.players;
    }

    public void setPlayers(Set<Player> players) {
        if (this.players != null) {
            this.players.forEach(i -> i.setFavouriteClub(null));
        }
        if (players != null) {
            players.forEach(i -> i.setFavouriteClub(this));
        }
        this.players = players;
    }

    public Club players(Set<Player> players) {
        this.setPlayers(players);
        return this;
    }

    public Club addPlayer(Player player) {
        this.players.add(player);
        player.setFavouriteClub(this);
        return this;
    }

    public Club removePlayer(Player player) {
        this.players.remove(player);
        player.setFavouriteClub(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Club)) {
            return false;
        }
        return getId() != null && getId().equals(((Club) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Club{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", logoPath='" + getLogoPath() + "'" +
            "}";
    }
}
