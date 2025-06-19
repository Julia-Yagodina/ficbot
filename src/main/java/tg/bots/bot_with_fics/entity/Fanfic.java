package tg.bots.bot_with_fics.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Entity
@Table(name="fanfics")
@AllArgsConstructor
@NoArgsConstructor
public class Fanfic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name="title", nullable = false)
    private String title;
    @Column(name="link", nullable = false)
    private String link;
    @Column(name="author", nullable = false)
    private String author;
    @Column(name="description", nullable = false)
    private String description;
    @Column(name="raiting", nullable = false)
    private String raiting;
    @Column(name="is_check", nullable = false)
    private boolean isCheck;
    @Column(name="mood", nullable = false)
    private String mood;
    @Column(name="pairing", nullable = false)
    private String pairing;





    public String getPost() {
        return "[ *" + shielding(title) + "* ]" + "(" + shielding(link) + ")\n\n" +
                shielding("✍\uFE0F  ") + "автор/переводчик: *" + shielding(author) + "*\n\n" +
                shielding("💕 ") + "пейринг: *" + shielding(pairing) + "*\n\n" +
                shielding("❗  ") + "rating: *" + shielding(raiting) + "*\n\n" +
                shielding(description);

    }

    private String shielding(String str) {
        return str.replaceAll("([_*\\[\\]()~`>#+=|{}.!-])", "\\\\$1");
    }
}
