# Babble : 위치 기반 익명 채팅 서비스

## Setup

프로젝트 저장소를 클론합니다. Gradle 기반 프로젝트이므로 IntelliJ를 사용한다면 build.gradle 파일을 선택하여 프로젝트를 실행합니다.

```bash
git clone https://github.com/wafflestudio/babble-server
```

로컬에서 해당 프로젝트를 실행하기 위해 필요한 환경변수들은 `.env.dev` 파일에서 관리합니다.
해당 파일 생성 후 아래와 같이 dev 환경에서 실행하기 위해 필요한 환경변수들을 추가합니다. ([application.yml](./src/main/resources/application.yml) 참고)

```
PROFILE=dev
CLIENT_ID=XXXXXXXXXXXX
REDIRECT_URI=http://localhost:0000/xxxx
```

[Docker Desktop](https://www.docker.com/products/docker-desktop/)을 설치하고 실행합니다.
아래 make 명령어를 통해 도커 이미지를 하나 빌드하고 컨테이너를 실행합니다. 필요에 따라 docker 명령어들에 --platform 옵션을 추가해줍니다.

```bash
make docker_run_dev
```

## API Document

API 명세는 개발 서버 실행 후 Swagger UI 페이지를 참고합니다.

- http://localhost:8080/swagger-ui/

별도로 Postman을 셋업할 필요 없이,
해당 페이지를 통해 실제로 개발 서버로 요청을 보내고, 응답을 확인할 수 있습니다.

### Authorize

카카오 소셜로그인을 통해 받은 인가 코드로 로그인에 성공하면 엑세스 토큰을 발급받습니다.

Authorize 버튼을 통해 해당 토큰을 페이지에 등록하면, 모든 요청들의 Authorization 헤더에 해당 토큰이 자동으로 추가됩니다.

```
Authorization: Bearer xxx.yyy.zzz
```
