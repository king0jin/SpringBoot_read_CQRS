# SpringBoot_read_CQRS

## SpringBoot 데이터 읽 프로젝트
### 1. SpringBoot 프로젝트 생성 
+ 의존성
  + Lombok
  + Spring Boot DevTools
  + Spring Web
  + Spring Data JPA
  + MySQL Driver
  + Spring Data MongoDB
  + Spring for Apache Kafka (이후에 Kafka연결)


### 2. 프로젝트 설정 변경
main/source/application.properties를 삭제하고 application.yaml을 생성하여 설정 변경
+ 서버 연결 포트 설정
+ 데이터베이스 연결 설정
+ jpa 설정


### 3. CORS설정을 위한 WebConfig 클래스 생성
웹 애플리케이션이 다른 출처(origin)에서 요청을 허용하기 위한 클래스이다
+ Spring MVC 설정을 커스터마이징하기 위해 WebMvcConfigurer 인터페이스를 implements한다
  + addCorsMappings() 메서드를 재정의하여 CORS 설정을 추가
    + registry.addMapping("/**")
      + 모든 URL 패턴 (/**)에 대해 CORS 설정
    + .allowedOrigins("http://localhost:3000")
      + http://localhost:3000에서 오는 요청만 허용


### 4. Entity 클래스 추가
데이터베이스와 연동이되는 클래스 이다
+ **Entity = Table**


### 5. Repository 인터페이스 생성 
데이터베이스에 실질적인 CURD 작업을 수행하기 위한 인스턴스를 생성하기 위한 Repository 인터페이스이다
+ Interface를 인터페이스로 생성하여 CURD 작업을 수행한다


### 6. StartListener 클래스 생성
웹 애플리케이션이 시작하자마자 MySQL데이터를 가져와서 MongoDB로 복사하는 클래스이다
+ ApplicationListener 인터페이스의 onApplicationEvent 메서드를 오버라이딩
  + onApplicationEvent 메서드 : 단 한번만 실행되는 메서드
    + 1) MySQL데이터 가져오기
    + 2) MongoDB 연결
    + 3) MongoDB 데이터베이스, 컬렉션 연결
    + 4) 기존 데이터 객체 초기화
    + 5) MySQL데이터 복사


![image](https://github.com/user-attachments/assets/162518fd-c050-4356-bc3e-bc749f92c9aa)


### 7. Controller 클래스 생성
URL로 읽기 요청이 오면 요청을 수행하는 Controller 클래스이다
+ 읽기 작업은 MongoDB의 데이터를 커서로 읽어온다


### 8. 데이터 읽기 수행
+ 브라우저로 서버를 접속하여 확인 확인
![image](https://github.com/user-attachments/assets/539feee9-6083-496b-a25b-226bb68607ea)


---
## Kafka 연결
Kafka를 연결하여 데이터를 읽기를할 때 전송된 Topic의 데이터를 데이터베이스에 저장


### 1. Kafka사용을 위한 의존성 추가 - rebuild수행
+ build.gradle - dependencies
  + **implementation 'org.springframework.kafka:spring-kafka'**
  + **implementation 'org.json:json:20190722'**


### 2. Kafka에 대한 정보 추가
Kafka사용을 위한 Kafka에 대한 정보를 SpringBoot 프로젝트 설정 파일에 추가
+ application.yaml


### 3. Kafka 환경 설정 클래스 생성
+ KafkaConfiguration


### 4. 메세지 수신 Consumer 클래스 생성
+ 특정 Kafka 토픽의 메시지 읽기 설정 : **@KafkaListener(topics = "cqrs-topic", groupId = "spring-write")**
+ Kafka로 부터 수신한 메세지 처리 메소드 : **public void consume(String message)**
  + 1) 수신한 JSON문자열 메세지를 객체로 변환
  + 2) MongoDB 연결
  + 3) MongoDB 데이터베이스, 컬렉션 연결
  + 4) Document 타입인 빈 객체 생성
  + 5) 객체로 변환된 메세지를 컬렉션의 키를 기준으로 구분하여 빈 객체에 저장
  + 6) MongoDB에 삽입
  + 7) MongoDB 연결 종료
