package naver.et0709.spring_cqrs_read;

import com.mongodb.client.*;
import org.bson.Document;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

//서비스와 URL매핑을 위한 Controller클래스
@RestController
public class BookController {
    //읽기 서비스와 URL매핑
    @GetMapping("/cqrs/book")
    public ResponseEntity<List> getBooks(){
        //MongoDB 연결
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        //데이터베이스, 컬렉션 연결
        MongoDatabase mongoDatabase = mongoClient.getDatabase("springcqrs");
        MongoCollection<Document> mongo_books = mongoDatabase.getCollection("books");

        //빈 List객체 생성
        List<Document> list = new ArrayList<Document>();
        try{
            //MongoDB의 books컬렉션의 데이터를 순차적으로 조회
            try(MongoCursor<Document> cur = mongo_books.find().iterator()){
                //컬렉션에 데이터가 있동안 반복 수행
                while (cur.hasNext()){
                    //커서가 가르키고 있는 데이터를 가져온다
                    Document doc = cur.next();
                    //가져온 데이터를 list에 저장
                    list.add(doc);
                }
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }finally{
            mongoClient.close();
        }
        return ResponseEntity.status(HttpStatus.OK).body(list);
    }

}
