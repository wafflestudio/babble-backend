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
```

[Docker Desktop](https://www.docker.com/products/docker-desktop/)을 설치하고 실행합니다.
아래 make 명령어를 통해 도커 이미지를 하나 빌드하고 컨테이너를 실행합니다. 필요에 따라 docker 명령어들에 --platform 옵션을 추가해줍니다.

```bash
make docker_run_dev
```
