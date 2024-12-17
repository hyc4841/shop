# shop
JPA를 이용한 쇼핑몰 API 서버

jwt를 이용한 로그인 구현

restful api 설계

새로안 사실들
- ControllerAdvice로 필터에서 발생한 예외를 api로 처리하지 못한다. 필터가 컨트롤러 보다 먼저 실행되기 때문


jwt 흐름
- 로그인 될 때 리프레시 토큰이 redis에 저장되며, 만약 이전에 리프레시가 저장되어 있으면 갱신된다.
- 