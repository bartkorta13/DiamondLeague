package pl.diamondleague.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * A Rating.
 */
@Entity
@Table(name = "rating")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Rating implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "date", nullable = false)
    private Instant date;

    @NotNull
    @Min(value = 1)
    @Max(value = 99)
    @Column(name = "attack", nullable = false)
    private Integer attack;

    @NotNull
    @Min(value = 1)
    @Max(value = 99)
    @Column(name = "defense", nullable = false)
    private Integer defense;

    @NotNull
    @Min(value = 1)
    @Max(value = 99)
    @Column(name = "engagement", nullable = false)
    private Integer engagement;

    @NotNull
    @Min(value = 70)
    @Max(value = 99)
    @Column(name = "overall", nullable = false)
    private Integer overall;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "ratings", "playerGames", "gameTeams", "favouriteClub" }, allowSetters = true)
    private Player player;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Rating id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDate() {
        return this.date;
    }

    public Rating date(Instant date) {
        this.setDate(date);
        return this;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public Integer getAttack() {
        return this.attack;
    }

    public Rating attack(Integer attack) {
        this.setAttack(attack);
        return this;
    }

    public void setAttack(Integer attack) {
        this.attack = attack;
    }

    public Integer getDefense() {
        return this.defense;
    }

    public Rating defense(Integer defense) {
        this.setDefense(defense);
        return this;
    }

    public void setDefense(Integer defense) {
        this.defense = defense;
    }

    public Integer getEngagement() {
        return this.engagement;
    }

    public Rating engagement(Integer engagement) {
        this.setEngagement(engagement);
        return this;
    }

    public void setEngagement(Integer engagement) {
        this.engagement = engagement;
    }

    public Integer getOverall() {
        return this.overall;
    }

    public Rating overall(Integer overall) {
        this.setOverall(overall);
        return this;
    }

    public void setOverall(Integer overall) {
        this.overall = overall;
    }

    public Player getPlayer() {
        return this.player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Rating player(Player player) {
        this.setPlayer(player);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Rating)) {
            return false;
        }
        return getId() != null && getId().equals(((Rating) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Rating{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", attack=" + getAttack() +
            ", defense=" + getDefense() +
            ", engagement=" + getEngagement() +
            ", overall=" + getOverall() +
            "}";
    }
}
