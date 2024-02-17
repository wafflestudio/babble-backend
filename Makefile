#HEAD: [ Manual ]
help:
	@#HELP: usage: prints the manual for all the commands in the current Makefile
	@cat Makefile | \
		grep -E -e '^\s*@?#(HEAD|HELP):' -e '^[a-zA-Z][a-zA-Z_]+:' | \
		sed -E "s/(^[a-zA-Z][^:]*):[^#]*(.*)[ ]*$$/    \1:/g" | \
		sed -E "s/[^#]*@#HELP: ?(.*$$)/      - \1/g" | \
		sed -E "s/[^#]*#HEAD: ?(.*)$$/\1/g"

#HEAD:
#HEAD: [ Docker ]

docker_run_local:
	@#HELP: clean build a docker image and run it
	@#HELP: requirements: download and run Docker Desktop
	./gradlew build
	docker build . -t babble-server
	@docker rm -f babble-server || true
	docker run -p 8080:8080 --env-file ./.env.dev --name babble-server babble-server
