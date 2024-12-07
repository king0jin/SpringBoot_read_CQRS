package naver.et0709.spring_cqrs_read;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

//데이터베이스와 연동되는 클래스
@Entity
//테이블 이름
@Table(name="book")
@ToString
@Getter
@Builder
//모든 속성을 대입받아서 생성하는 생성자
@AllArgsConstructor
//매개변수 없는 생성자를 생성
@NoArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long bid;

    @Column(length = 50, nullable = false)
    private String title;

    @Column(length = 50, nullable = false)
    private String author;

    @Column(length = 50, nullable = false)
    private String category;

    @Column
    private int pages;

    @Column
    private int price;

    @Column
    private Date published_Date;

    @Column(length = 50, nullable = false)
    private String description;

}
