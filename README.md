 # <온라인 속 개인 서재   *Booker*> 
 
### 내가 읽었던, 읽고 싶은 책들을 BOOKER로 정리해보세요~ 
### 다른 사람과 독후감을 공유할 수 있습니다!
<hr>

### 프로젝트 기간
2023.09.12 ~ 2023.11.12 (2개월)
### 개발 인원
2인 팀프로젝트 - FE(1명),BE(1명)
### 핵심 로직
- **JWT를 활용한 인증, 인가** 구현(Interceptor 사용)
- 무한 스크롤링을 위해 **Slice 페이징 처리**
- 등록 및 수정에 대한 **Validation 및 각종 Exception 관리**
- 연관관계 매핑 정책을 통해 불필요한 쿼리 개선(**N+1 문제 해결**)
- 소셜 로그인(**OAuth 2.0**)
- **AWS를 활용해 인프라 구축**(EC2, RDS, S3)
<hr>

![Skills](https://github.com/ahj30420/Booker/assets/79964990/5d45b681-4777-4b1f-b28e-24f76c201108)
<hr>

## 📌시스템 아키텍처
![Booker_아키텍처](https://github.com/ahj30420/Booker/assets/79964990/9c039353-d0b7-40a9-a98f-45102506f7e3)
<hr>

## ⚙️ERD
![ERD](https://github.com/ahj30420/Booker/assets/79964990/5983a4df-b1d8-435b-bd66-596f299c8980)
<hr>

## 📝로직 시퀀스 다이어그램
![Booker_시퀀스 다이어그램](https://github.com/ahj30420/Booker/assets/79964990/f5fbb4de-4f98-45af-96ab-0ad1f93376ff)
<hr>

## 💻핵심 기능 소개
### 로그인
- **OAuth 프로토콜**을 활용하여 구글,네이버 소셜 로그인을 구현하였습니다.
 
![로그인](https://github.com/ahj30420/Booker/assets/79964990/737caa8b-a31b-412e-beaa-fb3186a27d40)
<hr>

### 회원가입
- 회원가입 시 입력 값을 **Bean Validatio**을 통해 검증하였으며 **ValidationMessage.properties** 파일로 메시지를 관리하였습니다.
- 비밀번호는 **BCryptPasswordEncoder**를 통해 암호화하여 관리했습니다.

![회원가입](https://github.com/ahj30420/Booker/assets/79964990/dab91c58-dbbd-4fe7-8714-7eb812ebea64)
<hr>

### 프로필 등록 및 수정
- 프로필 등록 및 수정 시 **관심 분야는 최대 5까지 선택 가능**합니다.
- **선택된 관심 분야의 수에 따라 여러 개의 insert 쿼리가 날라가는 문제 발생**
- **JdbcTemplate BatchUpdate**를 활용하여 한 번에 여러 관심 분야를 저장하는 방식으로 **성능 최적화**

![프로필](https://github.com/ahj30420/Booker/assets/79964990/7c2a6fcc-de49-4f74-a402-c4f2abf78ad4)
<hr>

### 개인서재
- 책 목록 정보를 한 번에 전송하면 **대량의 트래픽이 발생**할 수 있습니다.
- 이를 해결하기 위해, **무한 스크롤링을 통해 데이터를 페이지별로 전송하고, 필요할 때 추가 데이터를 요청하는 방식으로 구현**했습니다.

![개인 서재](https://github.com/ahj30420/Booker/assets/79964990/475d3caf-e307-4fc8-bdc3-86243d634355)
<hr>

### 책 추천 페이지
- 베스트 셀러와 조회된 유저는 **페이징 처리하여 무한 스크롤링 적용**
- 취향이 비슷한 유저 조회 시 **페이징 처리를 해야 되기 때문에 컬렉션 페치 조인X**
- **관심 분야 조회를 Lazy fetch하고 Batch Size를 조절하여 N+1 문제 해결**

![책 추천](https://github.com/ahj30420/Booker/assets/79964990/fb8cf271-6280-4f8c-a4c0-a6b2439f71fa)
<hr>

### 책 상세 페이지
- 책에 대한 기본 적인 정보와 독후감 목록을 확인 할 수 있습니다.
- 독서 현황 및 독서 삭제가 가능합니다.

![책 상세 페이지](https://github.com/ahj30420/Booker/assets/79964990/8f91f2ed-2fcd-42c6-bd6c-c213595828b5)
<hr>

### 독후감 상세 페이지
- 독후감 내용을 확인 할 수 있고 수정 및 삭제 가능합니다.

![독후감 상세 페이지](https://github.com/ahj30420/Booker/assets/79964990/9b129d66-d6de-4c71-8038-3f307783e3b5)
<br></br>
이 밖에 팔로잉 기능, 쪽지 기능 등이 있습니다.
<hr>

## 👨‍💻프로젝트 회고
### 👍만족스러운 점
- JPA를 활용하여 **ORM 기술의 이해와 활용 방법을 습득**했습니다.
- AWS를 활용해 프로젝트를 배포하면서 **클라우드 서비스 및 인프라 구축**에 한 걸음 다가갈 수 있었습니다.
- JWT를 활용하여 **토큰의 개념과 인증, 인가에 대한 이해를 향상**시켰습니다.
- API명세서와 개발 일지 등의 **시각적 자료가 협업에서 얼마나 중요**한지를 다시 한 번 깨달았습니다.
<br></br>
### 👎아쉬운 점
- Spring Boot의 Interceptor를 사용한 인증, 인가 구현이 아쉽습니다.**Spring Security를 활용하여 좀 더 강력하고 다양한 보안 기능을 적용할 수 있었다면 더욱 효율적으로 보안을 관리**할 수 있었을 것으로 생각됩니다.
- **Github Action과 같은 자동화된 CI/CD 환경을 구축**하지 못한 점이 아쉽습니다.
- **Swagger와 같은 API 명세서 자동화 기술**을 활용하지 못한 점이 아쉽습니다.
