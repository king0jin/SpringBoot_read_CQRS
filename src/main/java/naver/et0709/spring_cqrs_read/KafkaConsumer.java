package naver.et0709.spring_cqrs_read;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.json.JSONObject;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

//서비스 계층의 Bean으로 등록
@Service
public class KafkaConsumer {
    //특정 Kafka 토픽의 메시지 읽기 설정
    @KafkaListener(topics = "cqrs-topic", groupId = "spring-write")

    //Kafka로 부터 수신한 메세지 처리 메소드
    public void consume(String message) throws Exception{
        //수신한 메세지 JSON 문자열 객체로 변환
        JSONObject messageObj = new JSONObject(message);
        //MongoDB 연결
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        //데이터베이스, 컬렉션 연결
        MongoDatabase mongoDatabase = mongoClient.getDatabase("springcqrs");
        MongoCollection<Document> mongo_books = mongoDatabase.getCollection("books");

        //빈 Document타입인 mongoBook객체 생성
        Document mongoBook = new Document();
        //mongoBook객체에 수신한 메세지을 Key값으로 구분해서 삽입
        mongoBook.append("bid", messageObj.getLong("bid"));
        mongoBook.append("title", messageObj.getString("title"));
        mongoBook.append("author", messageObj.getString("author"));
        mongoBook.append("category", messageObj.getString("category"));
        mongoBook.append("pages", messageObj.getInt("pages"));
        mongoBook.append("price", messageObj.getInt("price"));
        mongoBook.append("published_date", messageObj.getString("published_date"));
        mongoBook.append("description", messageObj.getString("description"));
        //컬렉션에 mongoBook객체 삽입
        mongo_books.insertOne(mongoBook);
        //MongoDB 연결 종료
        mongoClient.close();
    }
}
