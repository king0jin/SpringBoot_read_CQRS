package naver.et0709.spring_cqrs_read;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

//웹 애플리케이션이 시작하자 마자 MySQL의 데이터를 Mongo DB로 복사
@Component
@RequiredArgsConstructor
public class StartListener implements ApplicationListener<ApplicationStartedEvent> {
    private final BookRepository bookRepository;
    private final MongoClient mongo;
    @Override
    //애플리케이션이 시작하면 한 번만 호출되는 메서드
    public void onApplicationEvent(ApplicationStartedEvent event) {
        //MySQL의 데이터 가져오기
        java.util.List<Book> books = bookRepository.findAll();
        //MongoDB 연결
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        //데이터베이스, 컬렉션 연결
        MongoDatabase mongoDatabase = mongoClient.getDatabase("springcqrs");
        MongoCollection<Document> mongo_books = mongoDatabase.getCollection("books");
        //기존의 데이터 삭제
        mongo_books.drop();

        //빈 컬렉션을 가져온다
        mongo_books = mongoDatabase.getCollection("books");

        //컬렉션에 데이터 복사
        for(Book book : books) {
            Document mongoBook = new Document();
            mongoBook.append("bid", book.getBid());
            mongoBook.append("title", book.getTitle());
            mongoBook.append("author", book.getAuthor());
            mongoBook.append("category", book.getCategory());
            mongoBook.append("pages", book.getPages());
            mongoBook.append("price", book.getPrice());
            mongoBook.append("published_date", book.getPublished_Date());
            mongoBook.append("description", book.getDescription());

            mongo_books.insertOne(mongoBook);
        }

    }


}
