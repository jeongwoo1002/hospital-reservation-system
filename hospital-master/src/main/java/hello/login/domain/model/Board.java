package hello.login.domain.model;

import hello.login.domain.dto.BoardForm;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Lob
    private String content;

    private int count;  // 조회수

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE)
    private List<Reply> reply;

    @CreationTimestamp
    private Timestamp createDate;

    //==글쓰기==//
    public static Board save(BoardForm boardForm, User user) {
        return Board.builder()
                .title(boardForm.getTitle())
                .content(boardForm.getContent())
                .count(0)
                .user(user)
                .build();
    }

    //==글수정==//
    public void update(BoardForm boardForm) {
        this.title = boardForm.getTitle();
        this.content = boardForm.getContent();
    }
}
