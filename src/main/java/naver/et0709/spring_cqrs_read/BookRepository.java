package naver.et0709.spring_cqrs_read;
import org.springframework.data.jpa.repository.JpaRepository;
//book 테이블의 CRUD 작업 수행 인스턴스 생성을 위한 인터페이스
public interface BookRepository extends JpaRepository<Book, Long> {
}
