package sk.tuke.gamestudio.commentservice.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Entity
@NamedQuery( name = "Comment.getComments", query = "SELECT c FROM Comment c WHERE c.game=:game ORDER BY c.commentedOn DESC")
@NamedQuery( name = "Comment.reset", query = "DELETE FROM Comment")
public class Comment implements Serializable  {
    @Id
    @GeneratedValue
    private int ident;

    private String game;
    private String player;
    private String comment;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="Europe/Bratislava")
    private Date commentedOn;
    private String avatar;

    public Comment(String game, String player, String comment, Date commentedOn, String avatar) {
        this.game = game;
        this.player = player;
        this.comment = comment;
        this.commentedOn = commentedOn;
        this.avatar = avatar;
    }
}
