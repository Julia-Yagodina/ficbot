package tg.bots.bot_with_fics.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@Entity
@Table(name = "user_states")
public class UserState {
    @Id
    @Column(name = "id")
    private long userId;
    @Column(name= "state")
    private String state;


}
